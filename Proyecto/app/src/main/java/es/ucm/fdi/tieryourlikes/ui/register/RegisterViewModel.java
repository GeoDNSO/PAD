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
    }

    public MutableLiveData<ApiResponse<User>> getAPIresponseRegister() {
        return APIresponseRegister;
    }
}