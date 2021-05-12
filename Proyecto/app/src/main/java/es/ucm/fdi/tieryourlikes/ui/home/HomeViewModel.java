package es.ucm.fdi.tieryourlikes.ui.home;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.Category;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.repositories.CategoriesRepository;
import es.ucm.fdi.tieryourlikes.repositories.TemplateRepository;
import es.ucm.fdi.tieryourlikes.rxjava_utils.GeneralSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {

    private TemplateRepository templateRepository;
    private CategoriesRepository categoriesRepository;
    private MutableLiveData<ApiResponse<List<Template>>> mlvListTemplateMostRecentResponse;
    private MutableLiveData<ApiResponse<List<Template>>> mlvListTemplateMostDoneResponse;
    private MutableLiveData<ApiResponse<List<Template>>> mlvListTemplateCategoriesResponse;
    private MutableLiveData<ApiResponse<List<Category>>> mlvListCategoryMostPopularResponse;
    private List<Pair<MutableLiveData<ApiResponse<List<Template>>>, Category>> listPairMLV;


    public HomeViewModel() {
        templateRepository = new TemplateRepository();
        categoriesRepository = new CategoriesRepository();
        mlvListTemplateMostRecentResponse = new MutableLiveData<>();
        mlvListTemplateMostDoneResponse = new MutableLiveData<>();
        mlvListCategoryMostPopularResponse = new MutableLiveData<>();
        mlvListTemplateCategoriesResponse = new MutableLiveData<>();
        listPairMLV = new ArrayList<>();
    }

    public void getMostDoneTemplates(int page, int count) {
        GeneralSubscriber<List<Template>> generalSubscriber = new GeneralSubscriber<List<Template>>();
        generalSubscriber.setMutableLiveDataToModify(mlvListTemplateMostDoneResponse);
        generalSubscriber.setObservable(templateRepository.getMostDoneTemplates(page, count));
        generalSubscriber.subscribe();
    }

    public void getMostPopularCategories(int page, int count) {
        GeneralSubscriber<List<Category>> generalSubscriber = new GeneralSubscriber<List<Category>>();
        generalSubscriber.setMutableLiveDataToModify(mlvListCategoryMostPopularResponse);
        generalSubscriber.setObservable(categoriesRepository.getMostPopularCategories(page, count));
        generalSubscriber.subscribe();
    }

    public void getListTemplates(int page, int count) {
        GeneralSubscriber<List<Template>> generalSubscriber = new GeneralSubscriber<List<Template>>();
        generalSubscriber.setMutableLiveDataToModify(mlvListTemplateMostRecentResponse);
        generalSubscriber.setObservable(templateRepository.listTemplates(page, count, null));
        generalSubscriber.subscribe();
    }

    public void getListTemplatesCategory(int page, int count, Map<String, String> filter) {
        GeneralSubscriber<List<Template>> generalSubscriber = new GeneralSubscriber<List<Template>>();
        generalSubscriber.setMutableLiveDataToModify(mlvListTemplateCategoriesResponse);
        generalSubscriber.setObservable(templateRepository.listTemplates(page, count, filter));
        generalSubscriber.subscribe();
    }

    public void getListTemplatesCategory(int page, int count, Map<String, String> filter, MutableLiveData<ApiResponse<List<Template>>> mlv) {
        GeneralSubscriber<List<Template>> generalSubscriber = new GeneralSubscriber<List<Template>>();
        generalSubscriber.setMutableLiveDataToModify(mlv);
        generalSubscriber.setObservable(templateRepository.listTemplates(page, count, filter));
        generalSubscriber.subscribe();
    }

    public List<Pair<MutableLiveData<ApiResponse<List<Template>>>, Category>> generateDynamicMLV() {
        listPairMLV = new ArrayList<>();
        for(Category category: mlvListCategoryMostPopularResponse.getValue().getObject()) {
            listPairMLV.add(new Pair<>(new MutableLiveData<>(), category));
        }
        return listPairMLV;
    }

    public LiveData<ApiResponse<List<Template>>> getListTemplateMostRecentResponse() {
        return mlvListTemplateMostRecentResponse;
    }

    public LiveData<ApiResponse<List<Template>>> getListTemplateMostDoneResponse() {
        return mlvListTemplateMostDoneResponse;
    }

    public LiveData<ApiResponse<List<Category>>> getListCategoryMostPopularResponse() {
        return mlvListCategoryMostPopularResponse;
    }


    // TODO: Implement the ViewModel

}