package es.ucm.fdi.tieryourlikes.ui.register;

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
import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.User;
import es.ucm.fdi.tieryourlikes.ui.login.LoginViewModel;

public class RegisterFragment extends Fragment {

    private View root;
    private RegisterViewModel mViewModel;

    private TextView tvRegistro;

    private TextInputLayout layoutNombre;
    private TextInputLayout layoutApellidos;
    private TextInputLayout layoutUsername;
    private TextInputLayout layoutEmail;
    private TextInputLayout layoutPassword;
    private TextInputLayout layoutPassword2;

    private TextInputEditText inputNombre;
    private TextInputEditText inputApellidos;
    private TextInputEditText inputUsername;
    private TextInputEditText inputEmail;
    private TextInputEditText inputPassword;
    private TextInputEditText inputPassword2;

    private Button registerButton;
    private TextView tvLogeate;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.register_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        initUI();
        listeners();
        observers();

        return root;
    }

    private void initUI() {
        tvLogeate = root.findViewById(R.id.textViewLoginAqui);
        layoutNombre = root.findViewById(R.id.textInputLayoutNombreRegistro);
        inputNombre = root.findViewById(R.id.editTextNombreRegistro);
        layoutApellidos = root.findViewById(R.id.textInputLayoutApellidosRegistro);
        inputApellidos = root.findViewById(R.id.editTextApellidosRegistro);
        layoutEmail = root.findViewById(R.id.textInputLayoutEmailRegistro);
        inputEmail = root.findViewById(R.id.editTextEmailRegistro);
        layoutUsername = root.findViewById(R.id.textInputLayoutUsernameRegistro);
        inputUsername = root.findViewById(R.id.editTextUsernameRegistro);
        layoutPassword = root.findViewById(R.id.textInputLayoutPasswordRegistro);
        inputPassword = root.findViewById(R.id.editTextPasswordRegistro);
        layoutPassword2 = root.findViewById(R.id.textInputLayoutPassword2Registro);
        inputPassword2 = root.findViewById(R.id.editTextPassword2Registro);
        registerButton = root.findViewById(R.id.buttonRegistro);
    }

    private void listeners() {
        tvLogeate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.loginFragment);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String p1 = inputPassword.getText().toString();
                String p2 = inputPassword2.getText().toString();
                if(p1.equals(p2)){
                    User user = createUser(inputUsername.getText().toString(),
                            inputPassword.getText().toString(),
                            inputEmail.getText().toString(),
                            "ic_icons8_user_male.xml");
                    mViewModel.userRegister(user);
                }
                else {
                    inputPassword.setText("");
                    inputPassword2.setText("");
                    layoutPassword.setError(getString(R.string.password_not_equal_2));
                    layoutPassword2.setError(getString(R.string.password_not_equal_2));
                    Toast.makeText(getActivity(), "ERROR: Las contraseñas no coinciden" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private User createUser(String username, String pass, String email, String iconURL){
        return new User(username, pass, email, iconURL, "", AppConstants.NORMAL_USER);
    }

    private void observers(){
        mViewModel.getAPIresponseRegister().observe(getViewLifecycleOwner(), new Observer<ApiResponse<User>>() {
            @Override
            public void onChanged(ApiResponse<User> userApiResponse) {
                if(userApiResponse.getResponseStatus() == ResponseStatus.ERROR) {
                    Toast.makeText(getActivity(), "Hubo un error:" + userApiResponse.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }
                //bien --> enviarle a la pestaña de login o se puede tambien iniciar sesion directamente
                //User user = userApiResponse.getObject();
                //App.getInstance(getContext()).setUserSession(user);
                Navigation.findNavController(root).navigate(R.id.loginFragment);
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        // TODO: Use the ViewModel
    }

}