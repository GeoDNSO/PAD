package es.ucm.fdi.tieryourlikes.repositories;

import android.net.Uri;

import java.util.List;

import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.Category;
import es.ucm.fdi.tieryourlikes.networking.SimpleRequest;
import es.ucm.fdi.tieryourlikes.rxjava_utils.CallObservableCreator;
import io.reactivex.Observable;
import okhttp3.Request;

public class CategoriesRepository {
    public Observable<ApiResponse<List<Category>>> getCategories() {
        String route = "/getCategories/";
        SimpleRequest simpleRequest = new SimpleRequest();
        Request request = simpleRequest.buildRequest("", AppConstants.METHOD_GET, route);
        return new CallObservableCreator<>(Category.class).getList(simpleRequest, request);
    }


    public Observable<ApiResponse<List<Category>>> getMostPopularCategories(int page, int count) {
        String postBodyString = ""; //Metodo GET, no es necesairo un Body
        String route = "/getPopularCategories/";

        String finalURL = route + "?";

        Uri uri = Uri.parse(finalURL).buildUpon()
                .appendQueryParameter(AppConstants.DB_PAGE, String.valueOf(page))
                .appendQueryParameter(AppConstants.DB_LIMIT, String.valueOf(count))
                .build();

        SimpleRequest simpleRequest = new SimpleRequest();
        Request request = simpleRequest.buildRequest(postBodyString,
                AppConstants.METHOD_GET, uri.toString());
        return new CallObservableCreator<>(Category.class).getList(simpleRequest, request);
    }

}
