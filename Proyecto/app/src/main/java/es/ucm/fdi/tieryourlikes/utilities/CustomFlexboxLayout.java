package es.ucm.fdi.tieryourlikes.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import es.ucm.fdi.tieryourlikes.model.TierRow;

import static android.content.ContentValues.TAG;

public class CustomFlexboxLayout extends FlexboxLayout {

    private int indexAdded;
    private int indexDeleted;

    private TierRow tierRow;

    public CustomFlexboxLayout(Context context) {
        super(context);
    }

    public CustomFlexboxLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFlexboxLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void removeView(View view) {
        int i = this.indexOfChild(view);
        Log.d(TAG, "removeView: Se va a borrar el elemento " + i  + " de " + tierRow.getRowName());

        if(i == -1 || this.tierRow.getImageUrls().isEmpty()){
            super.removeView(view);
            return;
        }


        this.indexDeleted = i;
        //tierRow.getImageUrls().remove(i-1);

        super.removeView(view);
    }

    public void removeView(View view, String data) {
        Log.d(TAG, "removeView: Se va a borrar " + data + " de " + tierRow.getRowName());
        //int i = this.indexOfChild(view);
        boolean a = true;
        //this.indexDeleted = i;
        if(data != null)
            a = tierRow.getImageUrls().remove(data);
        super.removeView(view);
    }

    public void addView(View child, String data) {
        Log.d(TAG, "addView: Se va a añadir " + data + " a " + tierRow.getRowName());
        if(data != null){
            tierRow.getImageUrls().add(data);
            //indexAdded = tierRow.getImageUrls().size();
        }

        super.addView(child);
    }

    public void addView(View child, int index, String data) {
        Log.d(TAG, "addView: Se va a añadir (index "+ index+") " + data + " a " + tierRow.getRowName());
        if(index == -1)
            return;
        if(data != null){
            indexAdded = index;
            Log.d("AddView", " index recibido" + index);
            Log.d("Array", "tierRow: " + tierRow.getRowName() + " Elementos " +
                    tierRow.getImageUrls().size() + " contenido" +
                    tierRow.getImageUrls());
            tierRow.getImageUrls().set(index, data);
        }

        super.addView(child, index);
    }

    public int getIndexAdded() {
        return indexAdded;
    }

    public int getIndexDeleted() {
        return indexDeleted;
    }

    public TierRow getTierRow() {
        return tierRow;
    }

    public void setTierRow(TierRow tierRow) {
        this.tierRow = tierRow;
    }
}
