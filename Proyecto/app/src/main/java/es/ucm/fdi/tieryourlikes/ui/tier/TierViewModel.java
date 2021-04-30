package es.ucm.fdi.tieryourlikes.ui.tier;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.model.Tier;
import es.ucm.fdi.tieryourlikes.repositories.TemplateRepository;
import es.ucm.fdi.tieryourlikes.rxjava_utils.GeneralSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TierViewModel extends ViewModel {

    private TemplateRepository templateRepository;

    private MutableLiveData<ApiResponse<Template>> mlvTemplateResponse;
    private MutableLiveData<ApiResponse<Tier>> mlvTierResponse;

    public TierViewModel() {
        templateRepository = new TemplateRepository();
        mlvTemplateResponse = new MutableLiveData<>();
        mlvTierResponse = new MutableLiveData<>();
    }

    public void uploadTier(Tier tier) {
        GeneralSubscriber<Tier> generalSubscriber = new GeneralSubscriber<Tier>();
        generalSubscriber.setMutableLiveDataToModify(mlvTierResponse);
        generalSubscriber.setObservable(templateRepository.uploadTier(tier));
        generalSubscriber.subscribe();

        /*
        Disposable dis = templateRepository.uploadTier(tier)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResponse<Tier>>() {
                    @Override
                    public void accept(ApiResponse<Tier> s) throws Exception {
                        mlvTierResponse.setValue(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        ApiResponse<Tier> apiResponse = new ApiResponse<>(null, ResponseStatus.ERROR, throwable.getMessage());
                        mlvTierResponse.setValue(apiResponse);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });
                */
    }

    public MutableLiveData<ApiResponse<Template>> getMlvTemplateResponse() {
        return mlvTemplateResponse;
    }

    public MutableLiveData<ApiResponse<Tier>> getMlvTierResponse() {
        return mlvTierResponse;
    }

}