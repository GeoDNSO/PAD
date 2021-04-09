package es.ucm.fdi.googlebooksclient;

import android.view.View;

import androidx.core.widget.NestedScrollView;

public class NestedScrollListener implements NestedScrollView.OnScrollChangeListener {
    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){

            //page++;

        }
    }
}
