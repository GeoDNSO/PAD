package es.ucm.fdi.tieryourlikes.ui.template.listeners;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.flexbox.FlexboxLayout;

import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.utilities.AppDrawable;
import es.ucm.fdi.tieryourlikes.utilities.CustomFlexboxLayout;

public class TierRowDragListener implements View.OnDragListener {

    public TierRowDragListener() {
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();

        String uri = "";
        if(event.getClipData() != null){
            uri = event.getClipData().getItemAt(0).getText().toString();
            Log.d("DragElement", uri);
        }

        //IDs de los recursos usados para cambiar el fondo según se deje la elemento encima o no
        int normal_background = R.drawable.tier_row_normal_background;
        int active_background = R.drawable.tier_row_active_background;

        //Vistas y Layouts envueltos en el evento de arrastre
        View view = (View) event.getLocalState();
        ViewGroup owner = (ViewGroup) view.getParent();
        FlexboxLayout container = (FlexboxLayout) v;


        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                v.setBackgroundResource(active_background);

                view.setAlpha(0.5f);

                owner.removeView(view);
                container.addView(view);
                view.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                view.setAlpha(1);
                v.setBackgroundResource(normal_background);
                break;
            case DragEvent.ACTION_DROP:
                // Dropped, reassign View to ViewGroup
                view.setAlpha(1);
                owner.removeView(view);
                container.addView(view);
                view.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                view.setAlpha(1);
                v.setBackgroundResource(normal_background);
            default:
                break;
        }
        return true;
    }
}
