package es.ucm.fdi.tieryourlikes.repositories;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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



}