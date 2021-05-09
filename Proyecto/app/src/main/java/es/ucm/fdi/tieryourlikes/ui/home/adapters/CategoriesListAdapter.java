package es.ucm.fdi.tieryourlikes.ui.home.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.Category;
import es.ucm.fdi.tieryourlikes.model.Template;

public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>{

    private Activity activity;
    private List<Category> list;
    private TemplatesListAdapter templatesListAdapter;
    private List<List<Template>> templateList;
    private TemplatesListAdapter.OnItemClickListener onItemClickListener;

    public CategoriesListAdapter(Activity activity, List<Category> list, List<List<Template>> templateList, TemplatesListAdapter.OnItemClickListener onItemClickListener){
        this.activity = activity;
        this.list = list;
        this.templateList = templateList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CategoriesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_home_item_fragment, parent, false);

        return new CategoriesListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesListAdapter.ViewHolder holder, int position) {
        Category category = list.get(position);
        List<Template> templates = templateList.get(position);
        holder.categoryName.setText(category.getName());
        templatesListAdapter = new TemplatesListAdapter(activity, templates, onItemClickListener);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(templatesListAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView categoryName;
        private RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categories_text_view_item_home);
            recyclerView = itemView.findViewById(R.id.categories_recycle_view_item_home);
        }
    }

}
