package es.ucm.fdi.tieryourlikes.ui.iconDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tieryourlikes.R;

public class IconDialog extends BottomSheetDialogFragment {

    private IconDialogObserver observer;
    private View view;
    private List<ImageView> listaIconos;

    //COGER TODOS LOS IMAGEVIEWS
    private ImageView icon00;
    private ImageView icon01;
    private ImageView icon02;
    private ImageView icon03;
    private ImageView icon04;
    private ImageView icon05;
    private ImageView icon06;
    private ImageView icon07;


    public static IconDialog newInstance(IconDialogObserver observer) {
        return new IconDialog(observer);
    }
    public IconDialog(IconDialogObserver observer){
        this.observer = observer;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.icons_fragment, container, false);

        initIcons();
        addIconToList();

        // get the views and attach the listener
        for(ImageView v: listaIconos){
            v.setOnClickListener(v1 -> observer.onItemClicked(v1));
        }

        return view;
    }

    private void initIcons() {
        icon00 = view.findViewById(R.id.icon00);
        icon01 = view.findViewById(R.id.icon01);
        icon02 = view.findViewById(R.id.icon02);
        icon03 = view.findViewById(R.id.icon03);
        icon04 = view.findViewById(R.id.icon04);
        icon05 = view.findViewById(R.id.icon05);
        icon06 = view.findViewById(R.id.icon06);
        icon07 = view.findViewById(R.id.icon07);

    }

    private void addIconToList() {
        listaIconos = new ArrayList<>();
        listaIconos.add(icon00);
        listaIconos.add(icon01);
        listaIconos.add(icon02);
        listaIconos.add(icon03);
        listaIconos.add(icon04);
        listaIconos.add(icon05);
        listaIconos.add(icon06);
        listaIconos.add(icon07);
    }

    public interface IconDialogObserver {
        void onItemClicked(View v);
    }

}
