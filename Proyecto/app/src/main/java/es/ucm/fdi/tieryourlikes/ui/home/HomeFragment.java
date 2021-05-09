package es.ucm.fdi.tieryourlikes.ui.home;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.Category;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.ui.home.adapters.CategoriesListAdapter;
import es.ucm.fdi.tieryourlikes.ui.home.adapters.TemplatesListAdapter;

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
    private List<Category> mostPopularCategoriesList;
    private List<List<Template>> categoriesTemplateList;


    private int page = 1, count = 3;
    private int i = 0;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.home_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        init();

        mostDoneView();
        mostRecentView();
        mostPopularCategoriesView();

        mViewModel.getMostDoneTemplates(page, count);
        mViewModel.getListTemplates(page, count);
        mViewModel.getMostPopularCategories(page, count);

        observers();

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
                mostDoneView();
            }
        });

        mViewModel.getListCategoryMostPopularResponse().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<Category>>>() {
            @Override
            public void onChanged(ApiResponse<List<Category>> listApiResponse) {
                mostPopularCategoriesList = listApiResponse.getObject();
                mViewModel.getListTemplatesCategory(page, count, mostPopularCategoriesList.get(i).getName());
                ++i;
            }
        });

        mViewModel.getMlvListTemplateCategoriesResponse().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<Template>>>() {
            @Override
            public void onChanged(ApiResponse<List<Template>> listApiResponse) {
                categoriesTemplateList.add(listApiResponse.getObject());
                if(mostPopularCategoriesList.size() <= i){
                    mostPopularCategoriesView();
                    i = 0;
                }
                else {
                    mViewModel.getListTemplatesCategory(page, count, mostPopularCategoriesList.get(i).getName());
                    ++i;
                }
            }
        });
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
        categoriesListAdapter = new CategoriesListAdapter(getActivity(), mostPopularCategoriesList, categoriesTemplateList, this);
        categoriesRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoriesRecycleView.setAdapter(categoriesListAdapter);
    }

    private void init() {
        mostDoneRecycleView = root.findViewById(R.id.most_done_recycle_view_home);
        mostRecentRecycleView = root.findViewById(R.id.most_recent_recycle_view_home);
        categoriesRecycleView = root.findViewById(R.id.categories_recycle_view_home);
        mostDoneList = new ArrayList<>();
        mostRecentList = new ArrayList<>();
        mostPopularCategoriesList = new ArrayList<>();
        categoriesTemplateList = new ArrayList<>();
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
        //mViewModel.
    }
}