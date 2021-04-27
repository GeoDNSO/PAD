package es.ucm.fdi.tieryourlikes.ui.template;

import androidx.lifecycle.ViewModelProvider;

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

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.TierRow;
import es.ucm.fdi.tieryourlikes.ui.template.listeners.TierElementDragListener;
import es.ucm.fdi.tieryourlikes.ui.template.listeners.TierElementTouchListener;
import es.ucm.fdi.tieryourlikes.ui.template.listeners.TierRowDragListener;

public class TemplateFragment extends Fragment {

    private View root;
    private TemplateViewModel mViewModel;

    private RecyclerView recyclerView;
    private FlexboxLayout flexboxLayout;
    private TemplateAdapter templateAdapter;

    private List<TierRow> tierRowList;

    public static TemplateFragment newInstance() {
        return new TemplateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.template_fragment, container, false);

        configDemo();

        recyclerView = root.findViewById(R.id.tierMakerRecyclerView);
        templateAdapter = new TemplateAdapter(getActivity(), tierRowList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(templateAdapter);

        return root;
    }

    private void configDemo(){
        flexboxLayout = root.findViewById(R.id.activityMainFlexBox);

        //Preparar y configurar el contenedor desde el que se van a mover las imagenes
        flexboxLayout.setOnDragListener(new TierRowDragListener());
        int numberOfElements = 20;
        for(int i = 0; i < numberOfElements; i++){
            ImageView imageView = new ImageView(getContext());
            if(i % 2 == 0){
                imageView.setImageResource(R.drawable.ic_baseline_tag_faces_24);
            }else{
                imageView.setImageResource(R.drawable.ic_baseline_search_24);
            }

            imageView.setOnTouchListener(new TierElementTouchListener("vacio"));
            imageView.setOnDragListener(new TierElementDragListener());

            imageView.bringToFront();
            flexboxLayout.addView(imageView);
        }

        //Crear las filas de los distintos tiers
        tierRowList = new ArrayList<>();
        for(int i = 0; i < 8; ++i){
            tierRowList.add(new TierRow("Tier"+i, new ArrayList<>()));
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(TemplateViewModel.class);
        // TODO: Use the ViewModel
    }

}