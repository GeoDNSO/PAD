package es.ucm.fdi.tieryourlikes.ui.tier;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.model.Tier;
import es.ucm.fdi.tieryourlikes.repositories.TemplateRepository;
import es.ucm.fdi.tieryourlikes.repositories.TierRepository;
import es.ucm.fdi.tieryourlikes.rxjava_utils.GeneralSubscriber;

public class TierViewModel extends ViewModel {

    private TierRepository tierRepository;

    private MutableLiveData<ApiResponse<Template>> mlvTemplateResponse;
    private MutableLiveData<ApiResponse<Tier>> mlvTierResponse;

    public TierViewModel() {
        tierRepository = new TierRepository();
        mlvTemplateResponse = new MutableLiveData<>();
        mlvTierResponse = new MutableLiveData<>();
    }

    public void uploadTier(Tier tier) {
        GeneralSubscriber<Tier> generalSubscriber = new GeneralSubscriber<Tier>();
        generalSubscriber.setMutableLiveDataToModify(mlvTierResponse);
        generalSubscriber.setObservable(tierRepository.uploadTier(tier));
        generalSubscriber.subscribe();
    }

    public MutableLiveData<ApiResponse<Template>> getMlvTemplateResponse() {
        return mlvTemplateResponse;
    }

    public MutableLiveData<ApiResponse<Tier>> getMlvTierResponse() {
        return mlvTierResponse;
    }

}