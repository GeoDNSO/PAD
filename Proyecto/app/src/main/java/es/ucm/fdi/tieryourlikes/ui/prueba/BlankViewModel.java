package es.ucm.fdi.tieryourlikes.ui.prueba;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.Category;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.repositories.CategoriesRepository;
import es.ucm.fdi.tieryourlikes.repositories.TemplateRepository;
import es.ucm.fdi.tieryourlikes.rxjava_utils.GeneralSubscriber;

public class BlankViewModel extends ViewModel {
    private TemplateRepository templateRepository;
    private CategoriesRepository categoriesRepository;
    private MutableLiveData<ApiResponse<List<Template>>> mlvListTemplateCategoriesResponse;
    private List<Pair<MutableLiveData<ApiResponse<List<Template>>>, Category>> listPairMLV;

    private MutableLiveData<ApiResponse<List<Category>>> mlvListCategoryMostPopularResponse;

    public BlankViewModel() {
        templateRepository = new TemplateRepository();
        categoriesRepository = new CategoriesRepository();
        mlvListCategoryMostPopularResponse = new MutableLiveData<>();
        mlvListTemplateCategoriesResponse = new MutableLiveData<>();
        listPairMLV = new ArrayList<>();
    }

    public void getMostPopularCategories(int page, int count) {
        GeneralSubscriber<List<Category>> generalSubscriber = new GeneralSubscriber<List<Category>>();
        generalSubscriber.setMutableLiveDataToModify(mlvListCategoryMostPopularResponse);
        generalSubscriber.setObservable(categoriesRepository.getMostPopularCategories(page, count));
        generalSubscriber.subscribe();
    }

    public void getListTemplatesCategory(int page, int count, String category, MutableLiveData<ApiResponse<List<Template>>> mlv) {
        GeneralSubscriber<List<Template>> generalSubscriber = new GeneralSubscriber<List<Template>>();
        generalSubscriber.setMutableLiveDataToModify(mlv);
        generalSubscriber.setObservable(templateRepository.getListTemplatesCategory(page, count, category));
        generalSubscriber.subscribe();
    }

    public LiveData<ApiResponse<List<Category>>> getListCategoryMostPopularResponse() {
        return mlvListCategoryMostPopularResponse;
    }

    public MutableLiveData<ApiResponse<List<Template>>> getMlvListTemplateCategoriesResponse() {
        return mlvListTemplateCategoriesResponse;
    }

    public List<Pair<MutableLiveData<ApiResponse<List<Template>>>, Category>> generateDynamicMLV() {
        for(Category category: mlvListCategoryMostPopularResponse.getValue().getObject()) {
            listPairMLV.add(new Pair<>(new MutableLiveData<>(), category));
        }
        return listPairMLV;
    }
}