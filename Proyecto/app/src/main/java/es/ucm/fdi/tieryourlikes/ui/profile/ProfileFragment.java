package es.ucm.fdi.tieryourlikes.ui.profile;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;

import es.ucm.fdi.tieryourlikes.App;
import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.User;
import es.ucm.fdi.tieryourlikes.ui.iconDialog.IconDialog;

public class ProfileFragment extends Fragment implements IconDialog.IconDialogObserver {

    private View root;
    private ProfileViewModel mViewModel;

    private TextView tvEmail;
    private TextView tvUsername;
    private TextView tvTemplatesCount;
    private TextView tvTiersCount;
    private ImageView ivIcon;
    private Button buttonIcon;
    private IconDialog dialog;

    private int iconID;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.profile_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        tvEmail = root.findViewById(R.id.tvEmailPerfil);
        tvUsername = root.findViewById(R.id.tvUsernamePerfil);
        tvTemplatesCount = root.findViewById(R.id.tvTemplatesCount);
        tvTiersCount = root.findViewById(R.id.tvTiersC);
        buttonIcon = root.findViewById(R.id.buttonIcon);
        ivIcon = root.findViewById(R.id.imageView);

        String username = App.getInstance().getUsername();
        String email = App.getInstance().getEmail();
        mViewModel.userProfile(username);

        tvUsername.setText(username);
        tvEmail.setText(email);

        observers();

        buttonIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (dialog == null){
                    dialog = IconDialog.newInstance(ProfileFragment.this);
                }
                dialog.show(getParentFragmentManager(), "icons_fragment");
            }
        });

        return root;
    }

    private void observers(){
        mViewModel.getAPIresponseProfile().observe(getViewLifecycleOwner(), new Observer<ApiResponse<JsonObject>>() {
            @Override
            public void onChanged(ApiResponse<JsonObject> userApiResponse) {
                Log.d("TAG2", "ENTRO");
                if(userApiResponse.getResponseStatus() == ResponseStatus.ERROR) {
                    Toast.makeText(getActivity(), "Hubo un error:" + userApiResponse.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }
                //bien --> mostrar los datos en los text view
                //User user = userApiResponse.getObject();
                JsonObject stats = userApiResponse.getObject();
                Log.d("JSON", stats.toString());
                String template_count = stats.get("templates_count").toString();
                String tiers_count = stats.get("tiers_count").toString();

                tvTemplatesCount.setText(template_count);
                tvTiersCount.setText(tiers_count);

            }
        });

        /*mViewModel.getAPIresponseUpdate().observe(getViewLifecycleOwner(), new Observer<ApiResponse<User>>() {
            @Override
            public void onChanged(ApiResponse<User> userApiResponse) {
                Log.d("TAG2", "ENTRO");
                if(userApiResponse.getResponseStatus() == ResponseStatus.ERROR) {
                    Toast.makeText(getActivity(), "Hubo un error:" + userApiResponse.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }
                //bien --> coger datos del usuario y pasarselo a la app para que cree sesion y redirigir a pagina principal
                Toast.makeText(getActivity(), "Icono cambiado correctamente", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onItemClicked(View v) {
        String tag = v.getTag().toString();
        //poner ivIcon con el nuevo icono seleccionado
        int leftIdx = tag.lastIndexOf('/');
        int rightIdx = tag.lastIndexOf('.');
        String tagBueno = tag.substring(leftIdx + 1, rightIdx);
        iconID = getActivity().getResources().getIdentifier(tagBueno, "drawable", getActivity().getPackageName());
        ivIcon.setImageResource(iconID);
        /*//hacer set del nuevo iconURL en el user
        User user = App.getInstance().getUser();
        user.setIconURL(tag);
        //llamar al view model para hacer update del usuario
        mViewModel.userUpdate(user);*/
        dialog.dismiss();
    }
}