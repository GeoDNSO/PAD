package es.ucm.fdi.tieryourlikes.repositories;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.model.Tier;
import es.ucm.fdi.tieryourlikes.networking.SimpleRequest;
import es.ucm.fdi.tieryourlikes.rxjava_utils.CallObservableCreator;
import io.reactivex.Observable;
import okhttp3.Request;

public class TemplateRepository {


    public Observable<ApiResponse<Tier>> uploadTier(Tier tier) {
        String postBodyString = new Gson().toJson(tier);

        String route = "/createTierDone/";

        SimpleRequest simpleRequest = new SimpleRequest();
        Request request = simpleRequest.buildRequest(postBodyString,
                AppConstants.METHOD_POST, route);

        return new CallObservableCreator<Tier>().get(simpleRequest, request);
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

        return new CallObservableCreator<Template>().getList(simpleRequest, request);
    }

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
