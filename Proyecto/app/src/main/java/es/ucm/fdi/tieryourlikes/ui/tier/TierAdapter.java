package es.ucm.fdi.tieryourlikes.ui.tier;

import android.app.Activity;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.TierRow;
import es.ucm.fdi.tieryourlikes.ui.tier.listeners.TierRowDragListener;
import es.ucm.fdi.tieryourlikes.utilities.CustomColorPicker;
import es.ucm.fdi.tieryourlikes.utilities.CustomFlexboxLayout;
import petrov.kristiyan.colorpicker.ColorPicker;

public class TierAdapter extends RecyclerView.Adapter<TierAdapter.ViewHolder> {

    private Activity activity;
    private List<TierRow> list;

    private List<Integer> colors;

    public TierAdapter(Activity activity, List<TierRow> list){
        this.activity = activity;
        this.list = list;
        TypedArray typedArray = activity.getResources().obtainTypedArray(R.array.default_colors);
        colors = new ArrayList<>();
        for (int i = 0; i < typedArray.length(); i++) {
            colors.add(typedArray.getColor(i, 0));
        }
    }

    @NonNull
    @Override
    public TierAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tier_row, parent, false);

        return new TierAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TierAdapter.ViewHolder holder, int position) {
        TierRow tierRow = list.get(position);
        holder.tvTierRow.setText(tierRow.getRowName());
        holder.flexboxLayout.setTierRow(tierRow);
        holder.tierRow = tierRow;

        int index = position % colors.size();
        int color = colors.get(index);
        holder.tvTierRow.setBackgroundColor(color);
        holder.tvTierRow.setTextColor(CustomColorPicker.contrastColor(color, activity));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTierRow;
        private CustomFlexboxLayout flexboxLayout;
        private TierRow tierRow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTierRow = itemView.findViewById(R.id.tvTierRow);
            flexboxLayout = itemView.findViewById(R.id.tierRowFlexBoxLayout);

            flexboxLayout.setOnDragListener(new TierRowDragListener());

            tvTierRow.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ColorPicker colorPicker = CustomColorPicker.getTierColorPicker(activity, tvTierRow, tierRow);
                    colorPicker.show();

                    return false;
                }
            });
        }
    }
}
