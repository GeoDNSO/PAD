package es.ucm.fdi.tieryourlikes.rxjava_utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.User;
import es.ucm.fdi.tieryourlikes.networking.SimpleRequest;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import okhttp3.Request;
import okhttp3.Response;

public class CallObservableCreator<T> {

    Class<T> objectClass;

    public CallObservableCreator(Class<T> objectClass) {
        this.objectClass = objectClass;
    }

    public Observable<ApiResponse<List<T>>> getList(SimpleRequest simpleRequest, Request request) {
        return Observable.create(new CallSchema<ApiResponse<List<T>>>(simpleRequest, request){
            @Override
            protected void onSuccessful(String responseString, ObservableEmitter<ApiResponse<List<T>>> emitter) {
                ApiResponse<List<T>> apiResponse = getList(responseString);
                emitter.onNext(apiResponse);
            }
        });
    }

    public Observable<ApiResponse<T>> get(SimpleRequest simpleRequest, Request request){
        return Observable.create(new CallSchema<ApiResponse<T>>(simpleRequest, request){
            @Override
            protected void onSuccessful(String responseString, ObservableEmitter<ApiResponse<T>> emitter) {
                ApiResponse<T> apiResponse = getObject(responseString);
                emitter.onNext(apiResponse);
            }
        });
    }

    public Observable<ApiResponse<JsonObject>> getJson(SimpleRequest simpleRequest, Request request){
        return Observable.create(new CallSchema<ApiResponse<JsonObject>>(simpleRequest, request){
            @Override
            protected void onSuccessful(String responseString, ObservableEmitter<ApiResponse<JsonObject>> emitter) {
                ApiResponse<JsonObject> apiResponse = getJsonObject(responseString);
                emitter.onNext(apiResponse);
            }
        });
    }

    abstract class CallSchema<T> implements ObservableOnSubscribe<T>{
        private SimpleRequest simpleRequest;
        private Request request;

        public CallSchema(SimpleRequest simpleRequest, Request request) {
            this.simpleRequest = simpleRequest;
            this.request = request;
        }

        @Override
        public void subscribe(@NonNull ObservableEmitter<T> emitter) throws Exception {
            try (Response response = simpleRequest.createCall(request).execute()) {
                String responseString = response.body().string();

                if(!response.isSuccessful())
                    onUnsuccessful(responseString, emitter);
                else
                    onSuccessful(responseString, emitter);

            } catch (IOException e){
                emitter.onError(e);
            }finally {
                emitter.onComplete();
            }
        }

        protected void onUnsuccessful(String responseString, ObservableEmitter<T> emitter){
            ApiResponse<T> apiResponse = null;
            apiResponse = new ApiResponse<>(null, ResponseStatus.ERROR, responseString);
            emitter.onNext((T) apiResponse);
        }

        protected abstract void onSuccessful(String responseString, ObservableEmitter<T> emitter);
    }

    private ApiResponse<T> getObject(String responseString){

        JsonObject jo = (JsonObject)JsonParser.parseString(responseString);
        T object = new Gson().fromJson(jo, objectClass);

        return  new ApiResponse<>(object, ResponseStatus.SUCCESS, null);
    }

    private ApiResponse<JsonObject> getJsonObject(String responseString){

        JsonObject jo = (JsonObject)JsonParser.parseString(responseString);

        return  new ApiResponse<>(jo, ResponseStatus.SUCCESS, null);
    }

    private ApiResponse<List<T>> getList(String responseString){
        Type typeClass = new TypeToken<List<T>>(){}.getType();

        JsonObject jo = (JsonObject) JsonParser.parseString(responseString);
        JsonArray arr = jo.get("list").getAsJsonArray();

        List<T> outputList = new Gson().fromJson(arr, typeClass);

        return new ApiResponse<>(outputList, ResponseStatus.SUCCESS, null);
    }
}