package es.ucm.fdi.tieryourlikes.ui.creations;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.repositories.CategoriesRepository;
import es.ucm.fdi.tieryourlikes.repositories.TemplateRepository;
import es.ucm.fdi.tieryourlikes.rxjava_utils.GeneralSubscriber;

public class CreationsViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private TemplateRepository templateRepository;
    private MutableLiveData<ApiResponse<List<Template>>> mlvListTiers;
    private MutableLiveData<ApiResponse<List<Template>>> mlvListTemplates;

    public CreationsViewModel() {
        templateRepository = new TemplateRepository();
        mlvListTiers = new MutableLiveData<>();
        mlvListTemplates = new MutableLiveData<>();
    }

    public void getUserTiers(int page, int count, String username) {
        GeneralSubscriber<List<Template>> generalSubscriber = new GeneralSubscriber<List<Template>>();
        generalSubscriber.setMutableLiveDataToModify(mlvListTiers);
        generalSubscriber.setObservable(templateRepository.getUserTiers(page, count, username));
        generalSubscriber.subscribe();
    }

    public void getUserTemplates(int page, int count, String username) {
        GeneralSubscriber<List<Template>> generalSubscriber = new GeneralSubscriber<List<Template>>();
        generalSubscriber.setMutableLiveDataToModify(mlvListTemplates);
        generalSubscriber.setObservable(templateRepository.getUserTemplates(page, count, username));
        generalSubscriber.subscribe();
    }

    public LiveData<ApiResponse<List<Template>>> getListTiersResponse() {
        return mlvListTiers;
    }

    public LiveData<ApiResponse<List<Template>>> getListTemplatesResponse() {
        return mlvListTemplates;
    }

}