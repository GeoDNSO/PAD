package es.ucm.fdi.tieryourlikes.ui.login;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.model.Tier;
import es.ucm.fdi.tieryourlikes.model.User;
import es.ucm.fdi.tieryourlikes.repositories.UserRepository;
import es.ucm.fdi.tieryourlikes.rxjava_utils.GeneralSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private UserRepository userRepository;
    MutableLiveData<ApiResponse<User>> APIresponseLogin;

    public LoginViewModel(){
        userRepository = new UserRepository();
        APIresponseLogin = new MutableLiveData<ApiResponse<User>>();
    }

    public void userLogin (User user){

        GeneralSubscriber<User> generalSubscriber = new GeneralSubscriber<User>();
        generalSubscriber.setMutableLiveDataToModify(APIresponseLogin);
        generalSubscriber.setObservable(userRepository.userLogin(user));
        generalSubscriber.subscribe();

        //Lo de arriba es igual a lo de abajo, si te resulta raro o dificil o quieres hacer otras cosas
        // en el viewmodel hazlo como abajo
        /*
        Disposable dis = userRepository.userLogin(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ApiResponse<User>>() {
                    @Override
                    public void accept(ApiResponse<User> s) throws Exception {
                        Log.d("A", "accept");
                        APIresponseLogin.setValue(s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("A", "Usuario o contraseña erroneos");
                        throwable.printStackTrace();

                        ApiResponse<User> apiResponse = new ApiResponse<>(null, ResponseStatus.ERROR, throwable.getMessage());
                        APIresponseLogin.setValue(apiResponse);
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.d("H", "ON COMPLETE");
                    }
                });

         */
    }

    public MutableLiveData<ApiResponse<User>> getAPIresponseLogin() {
        return APIresponseLogin;
    }
}