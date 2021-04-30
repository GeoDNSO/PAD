package es.ucm.fdi.tieryourlikes.ui.template;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.TierRow;
import es.ucm.fdi.tieryourlikes.ui.template.listeners.TierRowDragListener;
import es.ucm.fdi.tieryourlikes.utilities.CustomFlexboxLayout;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {

    private Activity activity;
    private List<TierRow> list;

    public TemplateAdapter(Activity activity, List<TierRow> list){
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public TemplateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tier_row, parent, false);

        return new TemplateAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateAdapter.ViewHolder holder, int position) {
        TierRow tierRow = list.get(position);
        holder.tvTierRow.setText(tierRow.getRowName());
        holder.flexboxLayout.setTierRow(tierRow);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTierRow;
        private CustomFlexboxLayout flexboxLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTierRow = itemView.findViewById(R.id.tvTierRow);
            flexboxLayout = itemView.findViewById(R.id.tierRowFlexBoxLayout);

            flexboxLayout.setOnDragListener(new TierRowDragListener());
        }
    }
}
