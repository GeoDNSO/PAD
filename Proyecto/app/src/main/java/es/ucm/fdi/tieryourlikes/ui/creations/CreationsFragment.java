package es.ucm.fdi.tieryourlikes.ui.creations;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.ui.creations.view_pages.MyTemplatesFragment;
import es.ucm.fdi.tieryourlikes.ui.creations.view_pages.MyTiersFragment;
import es.ucm.fdi.tieryourlikes.utilities.DefaultTabAdapter;

public class CreationsFragment extends Fragment {

    private CreationsViewModel mViewModel;

    private View root;


    private List<String> tabTitlesList;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    protected int page = 1, limit = 3, quantum = 3;

    public static CreationsFragment newInstance() {
        return new CreationsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.creations_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CreationsViewModel.class);

        setHasOptionsMenu(true);

        initUI();

        prepareViewPager();

        return root;
    }

    private void prepareViewPager() {
        DefaultTabAdapter recoTabAdapter = new DefaultTabAdapter(getChildFragmentManager());
        tabLayout.setupWithViewPager(viewPager);

        for(int i = 0; i < tabTitlesList.size(); i++){
            Fragment placeListFragment = getInstance(tabTitlesList.get(i), null);
            recoTabAdapter.addFragment(placeListFragment, tabTitlesList.get(i));
        }

        viewPager.setAdapter(recoTabAdapter);
    }

    private void initUI(){
        tabLayout = root.findViewById(R.id.creations_tab_layout);
        viewPager = root.findViewById(R.id.creations_view_pager);

        tabTitlesList = new ArrayList<>();
        tabTitlesList.add(getString(R.string.my_tiers));
        tabTitlesList.add(getString(R.string.my_templates));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CreationsViewModel.class);
        // TODO: Use the ViewModel
    }

    //Simula una factoria muy sencilla...
    public Fragment getInstance(String type, String category){

        String myTiers =  getString(R.string.my_tiers);
        String myTemplates = getString(R.string.my_templates);

        if(type.equals(myTiers)){
            return new MyTiersFragment();
        }
        if(type.equals(myTemplates)){
            return new MyTemplatesFragment();
        }
        return null;
    }

}