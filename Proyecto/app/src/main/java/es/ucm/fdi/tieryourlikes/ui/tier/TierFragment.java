package es.ucm.fdi.tieryourlikes.ui.tier;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.tieryourlikes.App;
import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.model.Tier;
import es.ucm.fdi.tieryourlikes.model.TierRow;
import es.ucm.fdi.tieryourlikes.ui.tier.listeners.TierRowDragListener;
import es.ucm.fdi.tieryourlikes.utilities.AppUtils;
import es.ucm.fdi.tieryourlikes.utilities.CustomFlexboxLayout;

public class TierFragment extends Fragment {

    private TierViewModel mViewModel;
    private View root;

    private Template template;
    private Tier tier;

    private RecyclerView recyclerView;
    private CustomFlexboxLayout flexboxContainer;
    private TierAdapter tierAdapter;

    public static TierFragment newInstance() {
        return new TierFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.tier_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(TierViewModel.class);
        template = (Template) getArguments().getParcelable(AppConstants.BUNDLE_TEMPLATE);

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        actionBar.setTitle(template.getTitle());
        setHasOptionsMenu(true);

        initUI();
        observers();

        loadTierIfUserUsedIt();//Revisar en BD si existe el tier --> si no existe _id = -1


        buttonPruebConfig(); //Boton para probar la representacion interna del tier
        //Button button = root.findViewById(R.id.tierPrueba);
        //button.setVisibility(View.GONE);

        return root;
    }

    private void loadTierIfUserUsedIt() {
        Map<String, String> filters = new HashMap<>();
        filters.put(AppConstants.DB_CREATOR_USERNAME_KEY, App.getInstance().getUsername());
        filters.put(AppConstants.DB_TEMPLATE_ID, template.getId());
        mViewModel.getTier(filters);
    }

    private void observers() {
        mViewModel.getMlvTierSavedResponse().observe(getViewLifecycleOwner(), new Observer<ApiResponse<Tier>>() {
            @Override
            public void onChanged(ApiResponse<Tier> tierApiResponse) {
                Context context = TierFragment.this.getContext();
                if(tierApiResponse.getResponseStatus() == ResponseStatus.ERROR){
                    Toast.makeText(context, context.getString(R.string.error_getting_saved_tier_message), Toast.LENGTH_LONG).show();
                }
                else{
                    Tier tierResponse = tierApiResponse.getObject();
                    if(!tierResponse.getId().equals("-1")){
                        tier = tierResponse;
                        fillLayoutWithTier();
                        Toast.makeText(context, context.getString(R.string.ok_saved_tier_message), Toast.LENGTH_LONG).show();
                    }
                }

                if(tier == null){
                    buildTierFromTemplate();
                    fillContainer();
                }

                tierAdapter = new TierAdapter(getActivity(), root, tier.getTierRows());
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(tierAdapter);
            }
        });

        mViewModel.getMlvTierResponse().observe(getViewLifecycleOwner(), new Observer<ApiResponse<Tier>>() {
            @Override
            public void onChanged(ApiResponse<Tier> tierApiResponse) {
                Context context = TierFragment.this.getContext();
                if(tierApiResponse.getResponseStatus() == ResponseStatus.ERROR){
                    Toast.makeText(context, context.getString(R.string.error_upload_tier_message), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(context, context.getString(R.string.ok_upload_tier_message), Toast.LENGTH_LONG).show();
            }
        });

        mViewModel.getMlvDeleteResponse().observe(getViewLifecycleOwner(), new Observer<ApiResponse<JsonObject>>() {
            @Override
            public void onChanged(ApiResponse<JsonObject> jsonObjectApiResponse) {
                if(jsonObjectApiResponse.getResponseStatus() == ResponseStatus.ERROR){
                    Toast.makeText(getContext(), getString(R.string.error_delete_template_message), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getContext(), getString(R.string.success_delete_template), Toast.LENGTH_LONG).show();
                Navigation.findNavController(root).navigate(R.id.homeFragment);
            }
        });
    }

    //if tier null...
    private void fillLayoutWithTier() {
        flexboxContainer.setOnDragListener(new TierRowDragListener());
        List<String> list = this.tier.getContainer();
        flexboxContainer.setTierRow(new TierRow("Container","#000000", list));
        for(String image : list){
            ImageView imageView = AppUtils.loadTierImageViewFromAPI(image, getActivity(), root);
            flexboxContainer.addView(imageView);
        }
    }
    private void fillContainer(){
        //Preparar y configurar el contenedor desde el que se van a mover las imagenes
        flexboxContainer.setOnDragListener(new TierRowDragListener());
        List<String> list = this.template.getContainer();
        flexboxContainer.setTierRow(new TierRow("Container","#806BE4", list));
        for(String image : list){
            ImageView imageView = AppUtils.loadTierImageViewFromAPI(image, getActivity(), root);

            flexboxContainer.addView(imageView);
        }
    }

    private void initUI() {
        recyclerView = root.findViewById(R.id.tierMakerRecyclerView);
        flexboxContainer = root.findViewById(R.id.tierFlexContainer);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TierViewModel.class);
        // TODO: Use the ViewModel
    }



    private void buildTierFromTemplate(){
        this.tier = new Tier("-1", template.getId(), App.getInstance().getUsername(),
                template.getContainer(), TierRow.getListFromString(template.getTierRows())
                , "");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tier_fragment_menu, menu);

        App app = App.getInstance(getContext());
        MenuItem saveTierItem = menu.findItem(R.id.save_tier_item);

        saveTierItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                recyclerView.setDrawingCacheEnabled(true);
                Bitmap tierBitmap = recyclerView.getDrawingCache();
                //Guarda el tier como una imagen en el movil
                App.getInstance().saveTierImage(TierFragment.this.getContext(), tierBitmap);

                if(App.getInstance().isLogged())
                    mViewModel.uploadTier(tier);
                else{
                    Toast.makeText(getContext(), getString(R.string.no_logged_upload_tier_msg), Toast.LENGTH_SHORT);
                }

                return true;
            }
        });

        MenuItem deleteTierItem = menu.findItem(R.id.delete_tier_item);

        if(app.isAdmin()){
            deleteTierItem.setVisible(true);
        }else{
            deleteTierItem.setVisible(false);
        }

        deleteTierItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mViewModel.deleteTemplate(template.getId());
                return true;
            }
        });

    }

    //Función para revisar la representación del tier
    private void buttonPruebConfig() {
        Button button = root.findViewById(R.id.tierPrueba);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flexboxContainer.getTierRow();

                Log.d("BUTTON_TIER_RESULT", flexboxContainer.getTierRow().toString());
                for(TierRow tierRow : tier.getTierRows())
                    Log.d("BUTTON_TIER_RESULT", tierRow.toString());
            }
        });
    }

}