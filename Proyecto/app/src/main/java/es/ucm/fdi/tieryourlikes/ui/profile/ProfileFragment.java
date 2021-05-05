package es.ucm.fdi.tieryourlikes.ui.profile;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import es.ucm.fdi.tieryourlikes.App;
import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;

public class ProfileFragment extends Fragment {

    private View root;
    private ProfileViewModel mViewModel;

    private TextView tvEmail;
    private TextView tvUsername;
    private TextView tvTemplatesCount;
    private TextView tvTiersCount;
    private ImageView ivIcon;

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

        String username = App.getInstance().getUsername();
        String email = App.getInstance().getEmail();
        mViewModel.userProfile(username);

        tvUsername.setText(username);
        tvEmail.setText(email);

        observers();

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
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}