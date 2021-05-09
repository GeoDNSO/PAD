package es.ucm.fdi.tieryourlikes.ui.prueba;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class BlankFragment extends Fragment implements TemplatesListAdapter.OnItemClickListener{

    private BlankViewModel mViewModel;
    private View root;

    private RecyclerView categoriesRecycleView;


    private CategoriesListAdapter categoriesListAdapter;

    private List<Category> mostPopularCategoriesList;
    private List<Pair<List<Template>, Category>> listOfListTemplatesList;

    private int page = 1, count = 3;

    public static BlankFragment newInstance() {
        return new BlankFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.blank_fragment, container, false);

        init();

        mostPopularCategoriesView();

        mViewModel.getMostPopularCategories(page, count);

        mViewModel.getListCategoryMostPopularResponse().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<Category>>>() {
            @Override
            public void onChanged(ApiResponse<List<Category>> listApiResponse) {
                if(listApiResponse.getResponseStatus() == ResponseStatus.ERROR) {
                    Toast.makeText(getActivity(), "Hubo un error:" + listApiResponse.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }
                mostPopularCategoriesList = listApiResponse.getObject();
                /*for(int i = 0; i< mostPopularCategoriesList.size(); ++i) {
                    mViewModel.getListTemplatesCategory(page, count, mostPopularCategoriesList.get(i).getName());
                }*/

                List<Pair<MutableLiveData<ApiResponse<List<Template>>>, Category>> pairList = mViewModel.generateDynamicMLV();

                dynamicObserver(pairList);

                for(Pair<MutableLiveData<ApiResponse<List<Template>>>, Category> p: pairList) {
                    mViewModel.getListTemplatesCategory(page,count, p.second.getName(), p.first);
                }
                //i++;

            }
        });

        mViewModel.getMlvListTemplateCategoriesResponse().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<Template>>>() {
            @Override
            public void onChanged(ApiResponse<List<Template>> listApiResponse) {
                if(listApiResponse.getResponseStatus() == ResponseStatus.ERROR) {
                    Toast.makeText(getActivity(), "Hubo un error:" + listApiResponse.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }
                //listOfListTemplatesList.add(listApiResponse.getObject());
                mostPopularCategoriesView();
                /*if(mostPopularCategoriesList.size() == i){
                    mostPopularCategoriesView();
                    //i = 0;
                }
                else {
                    i++;
                    mViewModel.getListTemplatesCategory(page, count, mostPopularCategoriesList.get(i).getName());
                    //i++;
                }*/
            }
        });

        return root;
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

    private void mostPopularCategoriesView() {
        categoriesListAdapter = new CategoriesListAdapter(getActivity(), listOfListTemplatesList, this);
        categoriesRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        categoriesRecycleView.setAdapter(categoriesListAdapter);
    }


    private void init() {
        categoriesRecycleView = root.findViewById(R.id.categories_recycle_view_home);
        mostPopularCategoriesList = new ArrayList<>();
        listOfListTemplatesList = new ArrayList<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BlankViewModel.class);
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