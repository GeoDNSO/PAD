package es.ucm.fdi.tieryourlikes.rxjava_utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
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

    private Class<T> objectClass;

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

    private ApiResponse<List<T>> getList(String responseString){
        JsonObject jo = (JsonObject) JsonParser.parseString(responseString);
        JsonArray arr = jo.get("list").getAsJsonArray();

        List<T> outputList = new Gson().fromJson(arr, new ListWithElements<T>(objectClass));

        return new ApiResponse<>(outputList, ResponseStatus.SUCCESS, null);
    }

    // https://stackoverflow.com/questions/27253555/com-google-gson-internal-linkedtreemap-cannot-be-cast-to-my-class
    // https://stackoverflow.com/questions/14503881/strange-behavior-when-deserializing-nested-generic-classes-with-gson/14506181#14506181
    //Useful: https://stackoverflow.com/questions/26203634/gson-fromjson-deserialize-generics/26203635#26203635
    private class ListWithElements<T> implements ParameterizedType {

        private Class<T> elementsClass;

        public ListWithElements(Class<T> elementsClass) {
            this.elementsClass = elementsClass;
        }

        public Type[] getActualTypeArguments() {
            return new Type[] {elementsClass};
        }

        public Type getRawType() {
            return List.class;
        }

        public Type getOwnerType() {
            return null;
        }
    }
}