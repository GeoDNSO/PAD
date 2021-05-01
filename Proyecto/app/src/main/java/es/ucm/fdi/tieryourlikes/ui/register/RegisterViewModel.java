package es.ucm.fdi.tieryourlikes.ui.register;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.User;
import es.ucm.fdi.tieryourlikes.repositories.UserRepository;
import es.ucm.fdi.tieryourlikes.rxjava_utils.GeneralSubscriber;

public class RegisterViewModel extends ViewModel {
    private UserRepository userRepository;
    MutableLiveData<ApiResponse<User>> APIresponseRegister;

    public RegisterViewModel(){
        userRepository = new UserRepository();
        APIresponseRegister = new MutableLiveData<ApiResponse<User>>();
    }

    public void userRegister (User user){

        GeneralSubscriber<User> generalSubscriber = new GeneralSubscriber<User>();
        generalSubscriber.setMutableLiveDataToModify(APIresponseRegister);
        generalSubscriber.setObservable(userRepository.userRegister(user));
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
        return APIresponseRegister;
    }
}