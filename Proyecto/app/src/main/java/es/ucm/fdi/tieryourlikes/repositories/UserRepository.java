package es.ucm.fdi.tieryourlikes.repositories;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.model.User;
import es.ucm.fdi.tieryourlikes.model.serializers.TemplateSerializer;
import es.ucm.fdi.tieryourlikes.model.serializers.UserSerializer;
import es.ucm.fdi.tieryourlikes.networking.SimpleRequest;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import okhttp3.Request;
import okhttp3.Response;

public class UserRepository {

    class MyObservable<T> {

        public Observable<ApiResponse<T>> get(SimpleRequest simpleRequest, Request request) {

            return Observable.create(new ObservableOnSubscribe<ApiResponse<T>>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<ApiResponse<T>> emitter) throws Exception {

                    try (Response response = simpleRequest.createCall(request).execute()) {
                        String responseString = response.body().string();
                        ApiResponse<T> apiResponse = null;

                        if (!response.isSuccessful()) {
                            apiResponse = new ApiResponse<>(null, ResponseStatus.ERROR, responseString);
                        } else {
                            Log.d("JSON", responseString);

                            Gson gson = new Gson();
                            Type typeClass = new TypeToken<T>(){}.getType();
                            JsonObject jo = (JsonObject)JsonParser.parseString(responseString);
                            T object = new Gson().fromJson(jo, typeClass);

                            Log.d("OBJECT",  object.toString());

                            apiResponse = new ApiResponse<T>(object, ResponseStatus.SUCCESS, null);
                        }

                        emitter.onNext(apiResponse);

                    } catch (IOException e) {
                        emitter.onError(e);
                    } finally {
                        emitter.onComplete();
                    }
                }
            });
        }

    }

    public Observable<ApiResponse<User>> userLogin(User user) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new UserSerializer()) //MIrar clase TemplateSerializer que es quien lo convierte a JSON
                .setPrettyPrinting()
                .create();

        String postBodyString = gson.toJson(user); // que es el post body string?
        Log.d("U", user.toString());
        Log.d("PBS", postBodyString);
        String route = "/login/";

        SimpleRequest simpleRequest = new SimpleRequest();
        Request request = simpleRequest.buildRequest(postBodyString,
                AppConstants.METHOD_POST,route);

        return new UserRepository.MyObservable<User>().get(simpleRequest, request);
    }

}


