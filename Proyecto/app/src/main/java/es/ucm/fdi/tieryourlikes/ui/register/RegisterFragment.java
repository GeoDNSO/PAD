package es.ucm.fdi.tieryourlikes.ui.register;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.ui.login.LoginViewModel;

public class RegisterFragment extends Fragment {

    private View root;
    private RegisterViewModel mViewModel;

    //creo que solo hacen falta los layouts pero pongo ambos por si acaso
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

        //HACER EL FIND DEL RESTO
        tvLogeate = root.findViewById(R.id.textViewLoginAqui);
        layoutNombre = root.findViewById(R.id.textInputLayoutNombreRegistro);
        layoutApellidos = root.findViewById(R.id.textInputLayoutApellidosRegistro);
        layoutEmail = root.findViewById(R.id.textInputLayoutEmailRegistro);
        layoutUsername = root.findViewById(R.id.textInputLayoutUsernameRegistro);
        layoutPassword = root.findViewById(R.id.textInputLayoutPasswordRegistro);
        layoutPassword2 = root.findViewById(R.id.textInputLayoutPassword2Registro);

        registerButton = root.findViewById(R.id.buttonRegistro);

        tvLogeate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.loginFragment);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        // TODO: Use the ViewModel
    }


}