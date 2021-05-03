package es.ucm.fdi.tieryourlikes.repositories;

import com.google.gson.Gson;

import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.Tier;
import es.ucm.fdi.tieryourlikes.networking.SimpleRequest;
import es.ucm.fdi.tieryourlikes.rxjava_utils.CallObservableCreator;
import io.reactivex.Observable;
import okhttp3.Request;

public class TierRepository {

    public Observable<ApiResponse<Tier>> uploadTier(Tier tier) {
        String postBodyString = new Gson().toJson(tier);

        String route = "/createTierDone/";

        SimpleRequest simpleRequest = new SimpleRequest();
        Request request = simpleRequest.buildRequest(postBodyString,
                AppConstants.METHOD_POST, route);

        return new CallObservableCreator<>(Tier.class).get(simpleRequest, request);
    }

}
