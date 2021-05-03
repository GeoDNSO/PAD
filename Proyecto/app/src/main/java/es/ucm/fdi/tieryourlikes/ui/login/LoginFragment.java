package es.ucm.fdi.tieryourlikes.ui.login;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import es.ucm.fdi.tieryourlikes.App;
import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.User;
import es.ucm.fdi.tieryourlikes.ui.home.HomeViewModel;

public class LoginFragment extends Fragment {

    private View root;
    private LoginViewModel mViewModel;

    //creo que solo hacen falta los layouts pero pongo ambos por si acaso
    private TextView tvLogin;
    private TextInputLayout layoutUsername;
    private TextInputEditText inputUsername;
    private TextInputLayout layoutPassword;
    private TextInputEditText inputPassword;
    private Button loginButton;
    private TextView tvRegistrate;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.login_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        tvLogin = root.findViewById(R.id.textViewLogeate);
        layoutUsername = root.findViewById(R.id.textInputLayoutUsernameLogin);
        inputUsername = root.findViewById(R.id.editTextUsernameLogin);
        layoutPassword = root.findViewById(R.id.textInputLayoutPasswordLogin);
        inputPassword =root.findViewById(R.id.editTextPasswordLogin);
        loginButton = root.findViewById(R.id.buttonLogin);
        tvRegistrate = root.findViewById(R.id.textViewRegistrateAqui);

        tvRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.registerFragment);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                User user = createUser(inputUsername.getText().toString(),
                        inputPassword.getText().toString(),
                        "");
                mViewModel.userLogin(user);
            }
        });

        observers();

        return root;
    }

    private User createUser(String username, String pass, String email){
        User user = new User(username, pass, email);
        return user;
    }

    private void observers(){
        mViewModel.getAPIresponseLogin().observe(getViewLifecycleOwner(), new Observer<ApiResponse<User>>() {
            @Override
            public void onChanged(ApiResponse<User> userApiResponse) {
                Log.d("TAG2", "ENTRO");
                if(userApiResponse.getResponseStatus() == ResponseStatus.ERROR) {
                    layoutUsername.setError("El usuario o contraseña no coinciden.");
                    layoutPassword.setError("El usuario o contraseña no coinciden.");
                    Toast.makeText(getActivity(), "Hubo un error:" + userApiResponse.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }
                //bien --> coger datos del usuario y pasarselo a la app para que cree sesion y redirigir a pagina principal
                User user = userApiResponse.getObject();
                App.getInstance(getContext()).setUserSession(user);
                Navigation.findNavController(root).navigate(R.id.homeFragment);
            }
        });
    }

    /*public void errores(){
        if(inputUsername.getText().toString().trim().isEmpty()){
            //Poner el mensaje de error en el Layout
            layoutUsername.setError("Este campo es requerido");
        }else{
            //Quitar el mensaje de error del Layout
            layoutUsername.setError(null);
        }

        if(inputPassword.getText().toString().trim().isEmpty()){
            //Poner el mensaje de error en el Layout
            layoutPassword.setError("Este campo es requerido");
        }else{
            //Quitar el mensaje de error del Layout
            layoutPassword.setError(null);
        }
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        // TODO: Use the ViewModel
    }


}