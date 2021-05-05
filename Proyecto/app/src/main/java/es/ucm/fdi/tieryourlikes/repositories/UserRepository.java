package es.ucm.fdi.tieryourlikes.repositories;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.User;
import es.ucm.fdi.tieryourlikes.model.serializers.UserSerializer;
import es.ucm.fdi.tieryourlikes.networking.SimpleRequest;
import es.ucm.fdi.tieryourlikes.rxjava_utils.CallObservableCreator;

import io.reactivex.Observable;

import okhttp3.Request;

public class UserRepository {

    public Observable<ApiResponse<User>> userLogin(User user) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new UserSerializer()) //MIrar clase TemplateSerializer que es quien lo convierte a JSON
                .setPrettyPrinting()
                .create();

        String postBodyString = gson.toJson(user); // que es el post body string?

        String route = "/login/";

        SimpleRequest simpleRequest = new SimpleRequest();
        Request request = simpleRequest.buildRequest(postBodyString,
                AppConstants.METHOD_POST,route);

        return new CallObservableCreator<>(User.class).get(simpleRequest, request);
    }

    public Observable<ApiResponse<User>> userRegister(User user) {

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new UserSerializer()) //MIrar clase TemplateSerializer que es quien lo convierte a JSON
                .setPrettyPrinting()
                .create();

        String postBodyString = gson.toJson(user);

        String route = "/registerUser/";

        SimpleRequest simpleRequest = new SimpleRequest();
        Request request = simpleRequest.buildRequest(postBodyString,
                AppConstants.METHOD_POST, route);

        return new CallObservableCreator<>(User.class).get(simpleRequest, request);
    }

    public Observable<ApiResponse<JsonObject>> userProfile(String username) {

        String postBodyString = ""; //metodo GET no es necesario el body

        String route = "/getUserStats/";

        String finalURL = route + "?";
        Uri builtURI = Uri.parse(finalURL).buildUpon()
                .appendQueryParameter(AppConstants.DB_USERNAME_KEY, String.valueOf(username))
                .build();

        SimpleRequest simpleRequest = new SimpleRequest();
        Request request = simpleRequest.buildRequest(postBodyString,
                AppConstants.METHOD_GET, builtURI.toString());

        return new CallObservableCreator<>(JsonObject.class).getJson(simpleRequest, request);
    }

}