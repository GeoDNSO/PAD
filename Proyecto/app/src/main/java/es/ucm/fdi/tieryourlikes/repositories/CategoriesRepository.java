package es.ucm.fdi.tieryourlikes.repositories;

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
}
