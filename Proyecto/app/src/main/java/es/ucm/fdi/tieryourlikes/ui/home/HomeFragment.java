package es.ucm.fdi.tieryourlikes.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.ucm.fdi.tieryourlikes.App;
import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.Category;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.ui.home.adapters.CategoriesListAdapter;
import es.ucm.fdi.tieryourlikes.ui.home.adapters.TemplatesListAdapter;
import es.ucm.fdi.tieryourlikes.ui.tier.TierFragment;

public class HomeFragment extends Fragment implements TemplatesListAdapter.OnItemClickListener {

    private View root;

    private HomeViewModel mViewModel;

    private RecyclerView categoriesRecycleView;
    private RecyclerView mostDoneRecycleView;
    private RecyclerView mostRecentRecycleView;

    private TemplatesListAdapter templatesListAdapter;
    private CategoriesListAdapter categoriesListAdapter;

    private List<Template> mostDoneList;
    private List<Template> mostRecentList;
    private List<Pair<List<Template>, Category>> listOfListTemplatesList;

    private TextView noResutsMostDone;
    private TextView noResutsMostRecent;

    private int page = 1, count = 3;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.home_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        setHasOptionsMenu(true);

        init();

        mostDoneView();
        mostRecentView();
        mostPopularCategoriesView();

        observers();

        mViewModel.getMostDoneTemplates(page, count);
        mViewModel.getListTemplates(page, count);
        mViewModel.getMostPopularCategories(page, count);

        Button button = root.findViewById(R.id.button_trial);
        button.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                    Navigation.findNavController(root).navigate(R.id.trialFragment);
              }
          });

        return root;
    }

    private void observers() {
        mViewModel.getListTemplateMostRecentResponse().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<Template>>>() {
            @Override
            public void onChanged(ApiResponse<List<Template>> listApiResponse) {
                if(listApiResponse.getResponseStatus() == ResponseStatus.ERROR) {
                    Toast.makeText(getActivity(), "Hubo un error:" + listApiResponse.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }
                mostRecentList = listApiResponse.getObject();

                if(mostRecentList.size() == 0){
                    noResutsMostRecent.setVisibility(View.VISIBLE);
                }
                else{
                    noResutsMostRecent.setVisibility(View.GONE);
                }

                mostRecentView();
            }
        });

        mViewModel.getListTemplateMostDoneResponse().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<Template>>>() {
            @Override
            public void onChanged(ApiResponse<List<Template>> listApiResponse) {
                if(listApiResponse.getResponseStatus() == ResponseStatus.ERROR) {
                    Toast.makeText(getActivity(), "Hubo un error:" + listApiResponse.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }
                mostDoneList = listApiResponse.getObject();

                if(mostDoneList.size() == 0){
                    noResutsMostDone.setVisibility(View.VISIBLE);
                }
                else{
                    noResutsMostDone.setVisibility(View.GONE);
                }

                mostDoneView();
            }
        });

        mViewModel.getListCategoryMostPopularResponse().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<Category>>>() {
            @Override
            public void onChanged(ApiResponse<List<Category>> listApiResponse) {
                if(listApiResponse.getResponseStatus() == ResponseStatus.ERROR) {
                    Toast.makeText(getActivity(), "Hubo un error:" + listApiResponse.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Pair<MutableLiveData<ApiResponse<List<Template>>>, Category>> pairList = mViewModel.generateDynamicMLV();

                dynamicObserver(pairList);

                for(Pair<MutableLiveData<ApiResponse<List<Template>>>, Category> p: pairList) {
                    mViewModel.getListTemplatesCategory(page,count, p.second.getName(), p.first);
                }

            }
        });
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.home_fragment_menu, menu);

        MenuItem createTemplate = menu.findItem(R.id.create_template_menu_item);

        createTemplate.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Navigation.findNavController(root).navigate(R.id.templateFragment);
                return true;
            }
        });
    }

    private void dynamicObserver(List<Pair<MutableLiveData<ApiResponse<List<Template>>>, Category>> pairList) {
        for(Pair<MutableLiveData<ApiResponse<List<Template>>>, Category> p: pairList){
            p.first.observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<Template>>>() {
                @Override
                public void onChanged(ApiResponse<List<Template>> listApiResponse) {
                    listOfListTemplatesList.add(new Pair<>(listApiResponse.getObject(), p.second));
                    if(listOfListTemplatesList.size() == pairList.size()) {
                        mostPopularCategoriesView();
                    }
                }
            });
        }
    }

    private void mostRecentView() {
        templatesListAdapter = new TemplatesListAdapter(getActivity(), mostRecentList, this);
        mostRecentRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mostRecentRecycleView.setAdapter(templatesListAdapter);
    }

    private void mostDoneView() {
        templatesListAdapter = new TemplatesListAdapter(getActivity(), mostDoneList, this);
        mostDoneRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        mostDoneRecycleView.setAdapter(templatesListAdapter);
    }

    private void mostPopularCategoriesView() {

        List<Pair<List<Template>, Category>> pairList = listOfListTemplatesList;

        Collections.sort(pairList, new Comparator<Pair<List<Template>, Category>>() {
            @Override
            public int compare(Pair<List<Template>, Category> o1, Pair<List<Template>, Category> o2) {
                return o1.second.getName().compareTo(o2.second.getName());
            }
        });

        categoriesListAdapter = new CategoriesListAdapter(getActivity(), pairList, this);
        categoriesRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoriesRecycleView.setAdapter(categoriesListAdapter);
    }

    private void init() {
        mostDoneRecycleView = root.findViewById(R.id.most_done_recycle_view_home);
        mostRecentRecycleView = root.findViewById(R.id.most_recent_recycle_view_home);
        categoriesRecycleView = root.findViewById(R.id.categories_recycle_view_home);
        noResutsMostDone = root.findViewById(R.id.no_results_most_used_home);
        noResutsMostRecent = root.findViewById(R.id.no_results_most_recent_home);
        mostDoneList = new ArrayList<>();
        mostRecentList = new ArrayList<>();
        listOfListTemplatesList = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onItemClickListener(int position, List<Template> list) {
        String templateName = list.get(position).getTitle();
        Toast.makeText(getContext(), templateName, Toast.LENGTH_SHORT).show();

        Template template = list.get(position);

        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstants.BUNDLE_TEMPLATE, template);
        Navigation.findNavController(root).navigate(R.id.tierFragment, bundle);
    }

}