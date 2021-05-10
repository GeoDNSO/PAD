package es.ucm.fdi.tieryourlikes.ui.tier.listeners;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;

public class TierElementTouchListener implements View.OnTouchListener {

    private String elementUrl;

    public TierElementTouchListener(String elementUrl) {
        this.elementUrl = elementUrl;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        ClipData data = ClipData.newPlainText("Prueba", elementUrl);

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDragAndDrop(data, shadowBuilder, view, 0);
            view.setVisibility(View.GONE);

            return true;
        }
        else {
            return false;
        }
    }
}
