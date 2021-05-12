package es.ucm.fdi.tieryourlikes.ui.creations.view_pages;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tieryourlikes.App;
import es.ucm.fdi.tieryourlikes.AppConstants;
import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.ApiResponse;
import es.ucm.fdi.tieryourlikes.model.ResponseStatus;
import es.ucm.fdi.tieryourlikes.model.Template;
import es.ucm.fdi.tieryourlikes.ui.creations.CreationsViewModel;
import es.ucm.fdi.tieryourlikes.ui.creations.adapters.TiersListAdapter;

public class MyTiersFragment extends Fragment implements TiersListAdapter.OnItemClickListener {

    private CreationsViewModel mViewModel;
    private View root;

    private RecyclerView rvTiers;
    private List<Template> tiers = new ArrayList<>();

    private TiersListAdapter templatesListAdapter;
    private int page = 1, count = 3;

    public static MyTiersFragment newInstance() {
        return new MyTiersFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root =  inflater.inflate(R.layout.my_tiers_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CreationsViewModel.class);

        String username = App.getInstance().getUsername();
        rvTiers = root.findViewById(R.id.my_tiers_recycle_view);

        tiersView();
        observers();

        mViewModel.getTemplatesUsedByUser(page, count, username);

        return root;
    }


    private void observers() {
        mViewModel.getListTiersResponse().observe(getViewLifecycleOwner(), new Observer<ApiResponse<List<Template>>>() {
            @Override
            public void onChanged(ApiResponse<List<Template>> listApiResponse) {
                if(listApiResponse.getResponseStatus() == ResponseStatus.ERROR) {
                    Toast.makeText(getActivity(), "Hubo un error:" + listApiResponse.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }
                tiers = listApiResponse.getObject();
                tiersView();
            }
        });

    }


    private void tiersView() {
        templatesListAdapter = new TiersListAdapter(getActivity(), tiers, this);
        rvTiers.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvTiers.setAdapter(templatesListAdapter);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CreationsViewModel.class);
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