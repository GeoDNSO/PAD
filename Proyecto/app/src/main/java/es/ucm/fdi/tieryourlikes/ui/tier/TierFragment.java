package es.ucm.fdi.tieryourlikes.ui.tier;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tieryourlikes.App;
import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.model.Tier;
import es.ucm.fdi.tieryourlikes.model.TierRow;
import es.ucm.fdi.tieryourlikes.ui.tier.listeners.TierElementTouchListener;
import es.ucm.fdi.tieryourlikes.ui.tier.listeners.TierRowDragListener;
import es.ucm.fdi.tieryourlikes.utilities.CustomFlexboxLayout;

public class TierFragment extends Fragment {

    private TierViewModel mViewModel;
    private View root;

    private Template template;
    private Tier tier;

    private RecyclerView recyclerView;
    private CustomFlexboxLayout flexboxContainer;
    private TierAdapter templateAdapter;

    public static TierFragment newInstance() {
        return new TierFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.tier_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(TierViewModel.class);

        setHasOptionsMenu(true);
        initUI();
        observers();

        //TODO manejar template que se recibe
        template = (Template) getArguments().getParcelable(AppConstants.BUNDLE_TEMPLATE);
        Log.d("TIER FRAGMENT BUNDLE", template.toString());

        /*defaultTierAndTemplate();//Crear template y tier de ejemplo
        fillContainer();


        templateAdapter = new TierAdapter(getActivity(), tier.getTierRows());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(templateAdapter);

        butonPruebConfig();*/


        return root;
    }

    private void observers() {
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

    private void butonPruebConfig() {
        Button button = root.findViewById(R.id.tierPrueba);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flexboxContainer.getTierRow();

                Log.d("BUTON_TIER_RESULT", flexboxContainer.getTierRow().toString());
                for(TierRow tierRow : tier.getTierRows())
                    Log.d("BUTON_TIER_RESULT", tierRow.toString());
            }
        });
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

    @SuppressLint("ClickableViewAccessibility")
    private void fillContainer(){
        //Preparar y configurar el contenedor desde el que se van a mover las imagenes
        flexboxContainer.setOnDragListener(new TierRowDragListener());
        List<String> list = this.template.getContainer();
        flexboxContainer.setTierRow(new TierRow("Container","#806BE4", list));
        for(String images : list){
            ImageView imageView = new ImageView(getContext());

            int size = getResources().getDimensionPixelSize(R.dimen.tier_row_images);

            //Configuración  necesario para que tenga el tamaño fijo definido en R.dimen.tier_row_images
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(size,size);
            imageView.setLayoutParams(parms);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with(root)
                    .load(images)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .apply(new RequestOptions().override(size))
                    .error(R.drawable.ic_baseline_error_24)
                    .into(imageView);


            imageView.setOnTouchListener(new TierElementTouchListener(images));
            //imageView.setOnDragListener(new TierElementDragListener());
            imageView.bringToFront();

            flexboxContainer.addView(imageView);
        }
    }


    private void defaultTierAndTemplate(){
        //Crear template
        List<String> container = new ArrayList<>();
        container.add("https://static.wixstatic.com/media/a7dee3_4c558736f7b243329c59427d855d278c~mv2.jpg/v1/fill/w_1000,h_1000,al_c,q_90/a7dee3_4c558736f7b243329c59427d855d278c~mv2.jpg");
        container.add("https://www.eluniversal.com.mx/sites/default/files/2016/09/07/manzana.jpg");
        container.add("https://naranjasvitaminadas.com/wp-content/uploads/2018/10/naranja-valenciana-vitaminada.png");
        container.add("https://e00-expansion.uecdn.es/assets/multimedia/imagenes/2019/06/27/15616371314021.jpg");
        container.add("https://mk0lanoticiapwmx1x6a.kinstacdn.com/wp-content/uploads/2020/08/5-razones-para-comer-sandia-todos-los-dias.jpg");

        String image = "asdad";

        List<String> tierRowsString = new ArrayList<>();
        tierRowsString.add("A");
        tierRowsString.add("B");
        tierRowsString.add("C");

        //Glide.with(getActivity()).load("https://picsum.photos/200")
        Template template = new Template("Frutas", "Frutas", "frutero", image,
                container, tierRowsString);

        this.template = template;

        Tier tier = new Tier("3w4rw345", "w45645", "user2", container, TierRow.getListFromString(tierRowsString), "");
        this.tier = tier;
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
                App.getInstance().saveTierImage(TierFragment.this.getContext(), tierBitmap);

                mViewModel.uploadTier(tier);

                return true;
            }
        });

        MenuItem deleteTierItem = menu.findItem(R.id.delete_tier_item);
        Log.d("TIER FRAGMENT", app.getUser().toString());
        Log.d("TIER FRAGMENT 2", app.isAdmin() + "");
        //TODO hay algo que no va bien en las sesiones
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
}