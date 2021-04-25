package es.ucm.fdi.tieryourlikes.repositories;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.networking.SimpleRequest;
import es.ucm.fdi.tieryourlikes.utilities.AppUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class TemplateRepository {


class MyObservable<T>{

    public Observable<ApiResponse<List<T>>> get(SimpleRequest simpleRequest, Request request)
    {

        return  Observable.create(new ObservableOnSubscribe<ApiResponse<List<T>>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ApiResponse<List<T>>> emitter) throws Exception {

                try (Response response = simpleRequest.createCall(request).execute()) {
                    String responseString = response.body().string();
                    ApiResponse<List<T>> apiResponse = null;

                    if(!response.isSuccessful()){
                        apiResponse = new ApiResponse<>(null, ResponseStatus.ERROR, responseString);
                    }
                    else {
                        Gson gson = new Gson();
                        Type typeClass = new TypeToken<List<T>>(){}.getType();

                        JsonObject jo = (JsonObject)JsonParser.parseString(responseString);
                        JsonElement je = jo.get("list");
                        JsonArray arr = je.getAsJsonArray();
                        Log.d("TAG", "subscribe: " + arr.toString());


                        List<T> outputList = gson.fromJson(arr, typeClass);


                        apiResponse = new ApiResponse<>(outputList, ResponseStatus.SUCCESS, null);
                    }

                    emitter.onNext(apiResponse);

                } catch (IOException e){
                    emitter.onError(e);
                }finally {
                    emitter.onComplete();
                }
            }
        });
    }

}


    public Observable<ApiResponse<List<Template>>> listTemplates(int page, int quantity, String filter) {

        String postBodyString = ""; //Metodo GET, no es necesairo un Body

        String route = "/listTemplates/";

        String finalURL = route + "?";
        Uri builtURI = Uri.parse(finalURL).buildUpon()
                .appendQueryParameter(AppConstants.DB_PAGE, String.valueOf(page))
                .appendQueryParameter(AppConstants.DB_LIMIT, String.valueOf(quantity))
                .build();

        Log.d("REPO", builtURI.toString());

        SimpleRequest simpleRequest = new SimpleRequest();
        Request request = simpleRequest.buildRequest(postBodyString,
                AppConstants.METHOD_GET, builtURI.toString());

        return new MyObservable<Template>().get(simpleRequest, request);
    }

    /*
    public Observable<ApiResponse<String>> listTemplates(int page, int quantity, String filter) {

        String postBodyString = ""; //Metodo GET, no es necesairo un Body

        String route = "/listTemplates/";

        String finalURL = route + "?";
        Uri builtURI = Uri.parse(finalURL).buildUpon()
                .appendQueryParameter(AppConstants.DB_PAGE, String.valueOf(page))
                .appendQueryParameter(AppConstants.DB_LIMIT, String.valueOf(quantity))
                .build();

        Log.d("REPO", builtURI.toString());

        SimpleRequest simpleRequest = new SimpleRequest();
        Request request = simpleRequest.buildRequest(postBodyString,
                AppConstants.METHOD_GET, builtURI.toString());


        return  Observable.create(new ObservableOnSubscribe<ApiResponse<String>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ApiResponse<String>> emitter) throws Exception {

                try (Response response = simpleRequest.createCall(request).execute()) {
                    String responseString = response.body().string();
                    ApiResponse<String> apiResponse = null;

                    if(!response.isSuccessful()){
                        apiResponse = new ApiResponse<>(null, ResponseStatus.ERROR, responseString);
                    }
                    else {
                        apiResponse = new ApiResponse<>(responseString, ResponseStatus.SUCCESS, null);
                    }

                    emitter.onNext(apiResponse);

                } catch (IOException e){
                    emitter.onError(e);
                }finally {
                    emitter.onComplete();
                }
            }
        });
    }*/

    //@TODO FALTA AÑADIR EL FILTER y no añadirlo si esta vacio
    private String createBodyString(int page, int quantity, String filter){

        JSONObject bodyString = new JSONObject();

        try {
            bodyString.put("page", page);
            bodyString.put("quantity", quantity);
            //bodyString.put("filter", filter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return bodyString.toString();
    }
}
