package es.ucm.fdi.tieryourlikes.ui.template;

import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxLayout;

import es.ucm.fdi.tieryourlikes.R;

public class TierElementDragListener implements View.OnDragListener{
    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action = event.getAction();

        //IDs de los recursos usados para cambiar el fondo según se deje la elemento encima o no
        int normal_background = R.drawable.tier_row_normal_background;
        int active_background = R.drawable.tier_row_active_background;

        //Vistas y Layouts envueltos en el evento de arrastre
        View view = (View) event.getLocalState();
        ViewGroup owner = (ViewGroup) view.getParent();

        FlexboxLayout container = (FlexboxLayout) v.getParent();

        int i = -1;

        if(container.getChildCount() == 0){
            return true;
        }

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                container.setBackgroundResource(active_background);

                owner.removeView(view);
                view.setAlpha(0.5f);


                i = container.indexOfChild(v);
                container.addView(view, i);

                view.setVisibility(View.VISIBLE);

                break;
            case DragEvent.ACTION_DRAG_EXITED:
                view.setAlpha(1);
                container.setBackgroundResource(normal_background);
                break;
            case DragEvent.ACTION_DROP:
                // Dropped, reassign View to ViewGroup
                view.setAlpha(1);
                owner.removeView(view);

                i = container.indexOfChild(v);
                container.addView(view, i);

                view.setVisibility(View.VISIBLE);
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                view.setAlpha(1);
                container.setBackgroundResource(normal_background);
            default:
                break;
        }
        return true;
    }
}
