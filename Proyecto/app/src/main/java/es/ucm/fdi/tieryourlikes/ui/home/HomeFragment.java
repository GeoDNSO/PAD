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

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.model.serializers.TemplateSerializer;

public class HomeFragment extends Fragment implements TemplatesListAdapter.OnItemClickListener {

    private View root;

    private HomeViewModel mViewModel;

    private LinearLayout topCategoriesLinearLayout;

    private RecyclerView mostDoneRecycleView;
    private RecyclerView mostRecentRecycleView;
    private TemplatesListAdapter templatesListAdapter;

    private List<Template> mostDoneList;
    private List<Template> mostRecentList;

    private int page = 1, count = 5;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.home_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        /*topCategoriesLinearLayout = root.findViewById(R.id.top_categories_linearLayout_home);

        for(int i = 0; i < 4; ++i){
            View v = inflater.inflate(R.layout.top_categories_item_fragment, container, false);

            TextView tv = v.findViewById(R.id.top_categories_text_view_item);
            ImageView iv = v.findViewById(R.id.top_categories_image_view_item);

            iv.setImageResource(R.drawable.ic_baseline_save_24);
            tv.setText("Categoria" + i);

            topCategoriesLinearLayout.addView(v);
        }*/

        init();

        /*for(int i = 0; i < 6; ++i){
            mostDoneList.add(new Template("i" + i, "sad", "sad", "sadad", new ArrayList<>(), new ArrayList<>()));
            yourTemplatesList.add(new Template("i" + i, "sad", "sad", "sadad", new ArrayList<>(), new ArrayList<>()));
        }*/

        mostDoneView();
        mostRecentView();

        mViewModel.getMostDoneTemplates(page, count);
        mViewModel.getListTemplates(page, count);

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
                Log.d("HOME FRAGMENT", listApiResponse.getObject().toString());
                mostDoneView();
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

    private void init() {
        mostDoneRecycleView = root.findViewById(R.id.most_done_recycle_view_home);
        mostRecentRecycleView = root.findViewById(R.id.most_recent_recycle_view_home);
        mostDoneList = new ArrayList<>();
        mostRecentList = new ArrayList<>();
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