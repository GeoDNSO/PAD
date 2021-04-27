package es.ucm.fdi.tieryourlikes.ui.tier;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.model.Tier;
import es.ucm.fdi.tieryourlikes.model.TierRow;
import es.ucm.fdi.tieryourlikes.ui.template.TemplateAdapter;
import es.ucm.fdi.tieryourlikes.ui.template.listeners.TierElementDragListener;
import es.ucm.fdi.tieryourlikes.ui.template.listeners.TierElementTouchListener;
import es.ucm.fdi.tieryourlikes.ui.template.listeners.TierRowDragListener;
import es.ucm.fdi.tieryourlikes.utilities.AppUtils;
import okhttp3.internal.Util;

public class TierFragment extends Fragment {

    private TierViewModel mViewModel;
    private View root;

    private Template template;
    private Tier tier;

    private RecyclerView recyclerView;
    private FlexboxLayout flexboxContainer;
    private TemplateAdapter templateAdapter;

    public static TierFragment newInstance() {
        return new TierFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.tier_fragment, container, false);
        
        initUI();

        defaultTierAndTemplate();//Crear template y tier de ejemplo
        fillContainer();


        templateAdapter = new TemplateAdapter(getActivity(), tier.getTierRows());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(templateAdapter);


        return root;
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
            imageView.setOnDragListener(new TierElementDragListener());
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
        container.add("https://e00-expansion.uecdn.es/assets/multimedia/imagenes/2019/06/27/15616371314021.jpg");
        container.add("https://mk0lanoticiapwmx1x6a.kinstacdn.com/wp-content/uploads/2020/08/5-razones-para-comer-sandia-todos-los-dias.jpg");
        List<String> tierRowsString = new ArrayList<>();
        tierRowsString.add("A");
        tierRowsString.add("B");
        tierRowsString.add("C");

        //Glide.with(getActivity()).load("https://picsum.photos/200")
        Template template = new Template("Frutas", "Frutas", "frutero",
                container, tierRowsString);

        this.template = template;

        Tier tier = new Tier("3w4rw345", "w45645", "user2", container, TierRow.getListFromString(tierRowsString));
        this.tier = tier;
    }

}