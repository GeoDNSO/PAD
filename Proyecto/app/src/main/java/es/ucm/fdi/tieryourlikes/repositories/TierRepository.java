package es.ucm.fdi.tieryourlikes.repositories;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Map;

import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.Tier;
import es.ucm.fdi.tieryourlikes.networking.SimpleRequest;
import es.ucm.fdi.tieryourlikes.rxjava_utils.CallObservableCreator;
import io.reactivex.Observable;
import okhttp3.Request;

import static android.content.ContentValues.TAG;

public class TierRepository {

    public Observable<ApiResponse<Tier>> uploadTier(Tier tier) {
        String postBodyString = new Gson().toJson(tier);

        String route = "/createTierDone/";

        SimpleRequest simpleRequest = new SimpleRequest();
        Request request = simpleRequest.buildRequest(postBodyString,
                AppConstants.METHOD_POST, route);

        return new CallObservableCreator<>(Tier.class).get(simpleRequest, request);
    }

    public Observable<ApiResponse<Tier>> getTier(Map<String, String> filters) {
        String postBodyString = "";

        String route = "/getTierDone/";
        String finalURL = route + "?";
        Uri.Builder uriBuilder = Uri.parse(finalURL).buildUpon();
        if(filters != null)
            filters.forEach(uriBuilder::appendQueryParameter);

        finalURL = uriBuilder.build().toString();

        Log.d(TAG, "getTier: " + finalURL);

        SimpleRequest simpleRequest = new SimpleRequest();
        Request request = simpleRequest.buildRequest(postBodyString,
                AppConstants.METHOD_GET, finalURL);

        return new CallObservableCreator<>(Tier.class).get(simpleRequest, request);
    }

}
