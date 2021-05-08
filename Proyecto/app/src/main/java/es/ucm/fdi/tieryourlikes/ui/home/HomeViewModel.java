package es.ucm.fdi.tieryourlikes.ui.home;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.repositories.TemplateRepository;
import es.ucm.fdi.tieryourlikes.rxjava_utils.GeneralSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {

    private TemplateRepository templateRepository;
    private MutableLiveData<ApiResponse<List<Template>>> mlvListTemplateMostRecentResponse;
    private MutableLiveData<ApiResponse<List<Template>>> mlvListTemplateMostDoneResponse;

    public HomeViewModel() {
        templateRepository = new TemplateRepository();
        mlvListTemplateMostRecentResponse = new MutableLiveData<>();
        mlvListTemplateMostDoneResponse = new MutableLiveData<>();
    }

    public void getMostDoneTemplates(int page, int count) {
        GeneralSubscriber<List<Template>> generalSubscriber = new GeneralSubscriber<List<Template>>();
        generalSubscriber.setMutableLiveDataToModify(mlvListTemplateMostDoneResponse);
        generalSubscriber.setObservable(templateRepository.getMostDoneTemplates(page, count));
        generalSubscriber.subscribe();
    }

    public void getListTemplates(int page, int count) {
        GeneralSubscriber<List<Template>> generalSubscriber = new GeneralSubscriber<List<Template>>();
        generalSubscriber.setMutableLiveDataToModify(mlvListTemplateMostRecentResponse);
        generalSubscriber.setObservable(templateRepository.listTemplates(page, count, ""));
        generalSubscriber.subscribe();
    }

    public LiveData<ApiResponse<List<Template>>> getListTemplateMostRecentResponse() {
        return mlvListTemplateMostRecentResponse;
    }

    public LiveData<ApiResponse<List<Template>>> getListTemplateMostDoneResponse() {
        return mlvListTemplateMostDoneResponse;
    }

    // TODO: Implement the ViewModel

}