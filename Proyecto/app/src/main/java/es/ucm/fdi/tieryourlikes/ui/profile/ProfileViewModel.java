package es.ucm.fdi.tieryourlikes.ui.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.User;
import es.ucm.fdi.tieryourlikes.repositories.UserRepository;
import es.ucm.fdi.tieryourlikes.rxjava_utils.GeneralSubscriber;

public class ProfileViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private UserRepository userRepository;
    MutableLiveData<ApiResponse<JsonObject>> APIresponseProfile;
    MutableLiveData<ApiResponse<User>> APIresponseUpdate;

    public ProfileViewModel(){
        userRepository = new UserRepository();
        APIresponseProfile = new MutableLiveData<ApiResponse<JsonObject>>();
    }

    public void userProfile (String username){

        GeneralSubscriber<JsonObject> generalSubscriber = new GeneralSubscriber<JsonObject>();
        generalSubscriber.setMutableLiveDataToModify(APIresponseProfile);
        generalSubscriber.setObservable(userRepository.userProfile(username));
        generalSubscriber.subscribe();

    }

    public void userUpdate(User user){
        GeneralSubscriber<User> generalSubscriber = new GeneralSubscriber<User>();
        generalSubscriber.setMutableLiveDataToModify(APIresponseUpdate);
        generalSubscriber.setObservable(userRepository.userUpdate(user));
        generalSubscriber.subscribe();
    }

    public MutableLiveData<ApiResponse<JsonObject>> getAPIresponseProfile() {
        return APIresponseProfile;
    }

    public MutableLiveData<ApiResponse<User>> getAPIresponseUpdate() {
        return APIresponseUpdate;
    }
}