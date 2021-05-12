package es.ucm.fdi.tieryourlikes.ui.trial;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.repositories.TemplateRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TrialViewModel extends ViewModel {
    private MutableLiveData<ApiResponse<String>> pruebaAPIResponse;
    MutableLiveData<ApiResponse<List<Template>>> pruebaAPIResponse2;
    private TemplateRepository templateRepository;

    public TrialViewModel() {
        templateRepository = new TemplateRepository();
        pruebaAPIResponse = new MutableLiveData<ApiResponse<String>>();
        pruebaAPIResponse2 = new MutableLiveData<ApiResponse<List<Template>>>();
    }

    /*
    public void listTemplates(int page, int limit, String filter){

        Disposable dis = templateRepository.listTemplates(page, limit, filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResponse<String>>() {
                    @Override
                    public void accept(ApiResponse<String> s) throws Exception {
                        Log.d("A", "accept");
                        pruebaAPIResponse.setValue(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("A", "JAJAJAJA ERROR");
                        try {
                            ApiResponse<String> apiResponse = new ApiResponse<>(null, ResponseStatus.ERROR, throwable.getMessage());
                            pruebaAPIResponse.setValue(apiResponse);
                        }catch (Exception e){
                            ApiResponse<String> apiResponse = new ApiResponse<>(null, ResponseStatus.ERROR, e.getMessage());
                            pruebaAPIResponse.setValue(apiResponse);
                        }
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d("H", "ON COMPLETE");
                    }
                });

    }
    */

    public void listTemplates(int page, int limit, Map<String, String> filter) {

        Disposable dis = templateRepository.listTemplates(page, limit, filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResponse<List<Template>>>() {
                    @Override
                    public void accept(ApiResponse<List<Template>> s) throws Exception {
                        Log.d("A", "accept");
                        pruebaAPIResponse2.setValue(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("A", "JAJAJAJA ERROR");
                        throwable.printStackTrace();

                        ApiResponse<List<Template>> apiResponse = new ApiResponse<>(null, ResponseStatus.ERROR, throwable.getMessage());
                        pruebaAPIResponse2.setValue(apiResponse);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d("H", "ON COMPLETE");
                    }
                });
    }


    public MutableLiveData<ApiResponse<String>> getPruebaAPIResponse() {
        return pruebaAPIResponse;
    }

    public MutableLiveData<ApiResponse<List<Template>>> getPruebaAPIResponse2() {
        return pruebaAPIResponse2;
    }

}
