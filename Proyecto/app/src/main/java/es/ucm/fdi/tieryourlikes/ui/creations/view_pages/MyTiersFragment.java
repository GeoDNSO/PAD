package es.ucm.fdi.tieryourlikes.ui.creations.view_pages;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.ui.creations.CreationsViewModel;

public class MyTiersFragment extends Fragment {

    private CreationsViewModel mViewModel;
    private View root;

    public static MyTiersFragment newInstance() {
        return new MyTiersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root =  inflater.inflate(R.layout.my_tiers_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CreationsViewModel.class);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CreationsViewModel.class);
        // TODO: Use the ViewModel
    }

}