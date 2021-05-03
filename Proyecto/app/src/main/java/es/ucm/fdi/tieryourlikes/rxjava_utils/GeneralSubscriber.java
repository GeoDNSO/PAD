package es.ucm.fdi.tieryourlikes.rxjava_utils;

import androidx.lifecycle.MutableLiveData;

import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class GeneralSubscriber<T> {

    private MutableLiveData<ApiResponse<T>> mutableLiveData;
    private Observable<ApiResponse<T>> observable;

    public Disposable subscribe(){
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess(), onError(), onComplete());
    }

    public Consumer<ApiResponse<T>> onSuccess(){
        return new Consumer<ApiResponse<T>>() {
            @Override
            public void accept(ApiResponse<T> s) throws Exception {
                mutableLiveData.setValue(s);
            }
        };
    }

    public Consumer<Throwable> onError(){
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
                ApiResponse<T> apiResponse = new ApiResponse<>(null, ResponseStatus.ERROR, throwable.getMessage());
                mutableLiveData.setValue(apiResponse);
            }
    };}

    public Action onComplete(){
        return new Action() {
            @Override
            public void run() throws Exception {

            }
        };
    }

    public void setMutableLiveDataToModify(MutableLiveData<ApiResponse<T>> mutableLiveData) {
        this.mutableLiveData = mutableLiveData;
    }

    public void setObservable(Observable<ApiResponse<T>> observable) {
        this.observable = observable;
    }
}


