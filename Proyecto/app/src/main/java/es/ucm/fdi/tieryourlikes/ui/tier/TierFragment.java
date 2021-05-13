package es.ucm.fdi.tieryourlikes.ui.tier;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tieryourlikes.App;
import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.model.Tier;
import es.ucm.fdi.tieryourlikes.model.TierRow;
import es.ucm.fdi.tieryourlikes.networking.SimpleRequest;
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

        template = (Template) getArguments().getParcelable(AppConstants.BUNDLE_TEMPLATE);

        //defaultTierAndTemplate();//Crear template y tier de ejemplo
        buildTierFromTemplate();
        fillContainer();

        templateAdapter = new TierAdapter(getActivity(), tier.getTierRows());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(templateAdapter);

        //Boton para probar la representacion interna del tier
        butonPruebConfig();


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

            try{
                Glide.with(root)
                        .load(SimpleRequest.getImageDirectory() + images)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .apply(new RequestOptions().override(size))
                        .error(R.drawable.ic_baseline_error_24)
                        .listener(new CustomRequestListener(root, size, images, imageView))
                        .into(imageView);

            }catch (Exception exception){
            }


            imageView.setOnTouchListener(new TierElementTouchListener(images));
            //imageView.setOnDragListener(new TierElementDragListener());
            imageView.bringToFront();

            flexboxContainer.addView(imageView);
        }
    }

    //Apaño para que glide cargue correctamente las imagenes
    class CustomRequestListener implements RequestListener<Drawable> {
        private int tries = 0;
        private static final int MAX_TRIES = 1000;

        private View root;
        private int size;
        private String image_url;
        private ImageView imageView;

        public CustomRequestListener(View root, int size, String image_url, ImageView imageView) {
            this.root = root;
            this.size = size;
            this.image_url = image_url;
            this.imageView = imageView;
        }

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            if(tries < MAX_TRIES){
                try{
                    Glide.with(root)
                            .load(SimpleRequest.getImageDirectory() + image_url)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .apply(new RequestOptions().override(size))
                            .error(R.drawable.ic_baseline_error_24)
                            //.listener(this)
                            .into(imageView);
                }catch (Exception exception){

                }

            }
            tries++;
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            return false;
        }
    }

    private void buildTierFromTemplate(){
        this.tier = new Tier("-1", template.getId(), App.getInstance().getUsername(),
                new ArrayList<>(template.getContainer()), TierRow.getListFromString(template.getTierRows())
                , "");
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
}