package es.ucm.fdi.tieryourlikes.ui.template;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.model.Tier;
import es.ucm.fdi.tieryourlikes.repositories.TemplateRepository;
import es.ucm.fdi.tieryourlikes.rxjava_utils.GeneralSubscriber;

public class TemplateViewModel extends ViewModel {

    private TemplateRepository templateRepository;
    private MutableLiveData<ApiResponse<Template>> mlvTemplateResponse;

    public TemplateViewModel() {
        templateRepository = new TemplateRepository();
        mlvTemplateResponse = new MutableLiveData<>();
    }

    public void createTemplate(Template template) {
        GeneralSubscriber<Template> generalSubscriber = new GeneralSubscriber<Template>();
        generalSubscriber.setMutableLiveDataToModify(mlvTemplateResponse);
        generalSubscriber.setObservable(templateRepository.createTemplate(template));
        generalSubscriber.subscribe();
    }

    public LiveData<ApiResponse<Template>> getTemplateResponse() {
        return mlvTemplateResponse;
    }

    // TODO: Implement the ViewModel
}