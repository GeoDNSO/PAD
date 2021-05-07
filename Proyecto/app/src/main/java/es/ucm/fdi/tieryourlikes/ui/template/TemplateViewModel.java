package es.ucm.fdi.tieryourlikes.ui.template;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.Category;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.model.Tier;
import es.ucm.fdi.tieryourlikes.repositories.CategoriesRepository;
import es.ucm.fdi.tieryourlikes.repositories.TemplateRepository;
import es.ucm.fdi.tieryourlikes.rxjava_utils.GeneralSubscriber;

public class TemplateViewModel extends ViewModel {

    private TemplateRepository templateRepository;
    private CategoriesRepository categoriesRepository;
    private MutableLiveData<ApiResponse<Template>> mlvTemplateResponse;
    private MutableLiveData<ApiResponse<List<Category>>> mlvCategoriesResponse;

    public TemplateViewModel() {
        templateRepository = new TemplateRepository();
        categoriesRepository = new CategoriesRepository();
        mlvTemplateResponse = new MutableLiveData<>();
        mlvCategoriesResponse = new MutableLiveData<>();
    }

    public void createTemplate(Template template) {
        GeneralSubscriber<Template> generalSubscriber = new GeneralSubscriber<Template>();
        generalSubscriber.setMutableLiveDataToModify(mlvTemplateResponse);
        generalSubscriber.setObservable(templateRepository.createTemplate(template));
        generalSubscriber.subscribe();
    }

    public void getCategories() {
        GeneralSubscriber<List<Category>> generalSubscriber = new GeneralSubscriber<List<Category>>();
        generalSubscriber.setMutableLiveDataToModify(mlvCategoriesResponse);
        generalSubscriber.setObservable(categoriesRepository.getCategories());
        generalSubscriber.subscribe();
    }

    public LiveData<ApiResponse<Template>> getTemplateResponse() {
        return mlvTemplateResponse;
    }

    public MutableLiveData<ApiResponse<List<Category>>> getCategoriesResponse() {
        return mlvCategoriesResponse;
    }
}