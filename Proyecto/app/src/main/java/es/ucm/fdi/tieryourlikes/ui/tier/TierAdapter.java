package es.ucm.fdi.tieryourlikes.ui.tier;

import android.app.Activity;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.TierRow;
import es.ucm.fdi.tieryourlikes.networking.SimpleRequest;
import es.ucm.fdi.tieryourlikes.ui.tier.listeners.TierRowDragListener;
import es.ucm.fdi.tieryourlikes.utilities.AppUtils;
import es.ucm.fdi.tieryourlikes.utilities.CustomColorPicker;
import es.ucm.fdi.tieryourlikes.utilities.CustomFlexboxLayout;
import petrov.kristiyan.colorpicker.ColorPicker;

public class TierAdapter extends RecyclerView.Adapter<TierAdapter.ViewHolder> {

    private Activity activity;
    private List<TierRow> list;
    private View root;

    private List<Integer> colors;

    public TierAdapter(Activity activity, View root, List<TierRow> list){
        this.activity = activity;
        this.root = root;
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
        if(!tierRow.hasDefaultColor())
            color = Integer.valueOf(tierRow.getColor());

        holder.tvTierRow.setBackgroundColor(color);
        holder.tvTierRow.setTextColor(AppUtils.contrastColor(color, activity));
        tierRow.setColor(Integer.toString(color));

        List<String> row_images = tierRow.getImageUrls();
        for(String image : row_images){
            ImageView imageView = AppUtils.loadTierImageViewFromAPI(image, activity, root);
            holder.flexboxLayout.addView(imageView);
        }
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
                    //ColorPicker colorPicker = AppUtils.getTierColorPicker(activity, tvTierRow, tierRow);
                    CustomColorPicker colorPicker = AppUtils.getCustomTierColorPicker(activity, tvTierRow, tierRow);
                    colorPicker.show();

                    return false;
                }
            });
        }
    }
}
