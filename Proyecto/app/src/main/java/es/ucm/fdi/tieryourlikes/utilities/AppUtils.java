package es.ucm.fdi.tieryourlikes.utilities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.TierRow;
import petrov.kristiyan.colorpicker.ColorPicker;

import static android.content.ContentValues.TAG;

public class AppUtils {

    //Para usar el colorPicker normal sin el campo extra de la edición del nombre de la TierRow
    public static ColorPicker getTierColorPicker(Activity activity, TextView tvTierRow, TierRow tierRow){

        ColorPicker colorPicker = new ColorPicker(activity);
        colorPicker.setTitle(activity.getString(R.string.choose_color));
        colorPicker.disableDefaultButtons(true);

        String ok = activity.getString(R.string.color_select_ok);
        Button okButton = new Button(activity);
        okButton.setTextColor(activity.getColor(R.color.endBlue));
        okButton.setBackgroundColor(activity.getColor(R.color.white));
        colorPicker.addListenerButton(ok, okButton, new ColorPicker.OnButtonListener() {
            @Override
            public void onClick(View v, int position, int color) {
                tvTierRow.setBackgroundColor(color);
                tierRow.setColor(String.valueOf(color));
                tvTierRow.setTextColor(contrastColor(color, activity));
                colorPicker.dismissDialog();
            }
        });

        String cancel = activity.getString(R.string.color_select_cancel);
        Button cancelButton = new Button(activity);
        cancelButton.setTextColor(activity.getColor(R.color.grey));
        cancelButton.setBackgroundColor(activity.getColor(R.color.white));
        colorPicker.addListenerButton(cancel, cancelButton, new ColorPicker.OnButtonListener() {
            @Override
            public void onClick(View v, int position, int color) {
                colorPicker.dismissDialog();
            }
        });

        return colorPicker;
    }

    public static CustomColorPicker getCustomTierColorPicker(Activity activity, TextView tvTierRow, TierRow tierRow){

        CustomColorPicker colorPicker = new CustomColorPicker(activity);
        colorPicker.setTitle(activity.getString(R.string.choose_color));
        colorPicker.disableDefaultButtons(true);
        colorPicker.setLabel(tierRow.getRowName());

        String ok = activity.getString(R.string.color_select_ok);
        Button okButton = new Button(activity);
        okButton.setTextColor(activity.getColor(R.color.endBlue));
        okButton.setBackgroundColor(activity.getColor(R.color.white));
        colorPicker.addListenerButton(ok, okButton, new ColorPicker.OnButtonListener() {
            @Override
            public void onClick(View v, int position, int color) {
                if(position != -1){
                    tvTierRow.setBackgroundColor(color);
                    tierRow.setColor(String.valueOf(color));
                    tvTierRow.setTextColor(contrastColor(color, activity));
                }
                String label = colorPicker.getLabelName();

                //Si el edittext no contiene solo espacios, modificamos el textview y el tierRow
                if (label.trim().length() != 0){
                    tierRow.setRowName(label);
                    tvTierRow.setText(label);
                }
                colorPicker.dismissDialog();
            }
        });

        String cancel = activity.getString(R.string.color_select_cancel);
        Button cancelButton = new Button(activity);
        cancelButton.setTextColor(activity.getColor(R.color.grey));
        cancelButton.setBackgroundColor(activity.getColor(R.color.white));
        colorPicker.addListenerButton(cancel, cancelButton, new ColorPicker.OnButtonListener() {
            @Override
            public void onClick(View v, int position, int color) {
                colorPicker.dismissDialog();
            }
        });

        return colorPicker;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int contrastColor(Color color, Activity activity) {
        int d = 0;

        // Counting the perceptive luminance - human eye favors green color...
        double luminance = ( 0.299 * color.red() + 0.587 * color.green() + 0.114 * color.blue())/255;

        if (luminance > 0.5)
            d = 0; // bright colors - black font
        else
            d = 255; // dark colors - white font

        return (d==0) ? activity.getColor(R.color.black) : activity.getColor(R.color.white);
        //return  Color.argb(1, d, d, d); //No se puede usar Color.valueOf, Color.rgb porque tiene que ser API > 26...
    }

    public static int contrastColor(int color, Activity activity) {
        int d = 0;

        // Counting the perceptive luminance - human eye favors green color...
        double luminance = Color.luminance(color);

        if (luminance > 0.5)
            d = 0; // bright colors - black font
        else
            d = 255; // dark colors - white font

        return (d==0) ? activity.getColor(R.color.black) : activity.getColor(R.color.white);
    }


    public static void sleep(long milis){
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void setFragmentTitle(Activity activity, String title){
        //Poner el nombre del lugar en la toolbar
        AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        actionBar.setTitle(title);
    }

    public static String getValidTag(String tag){
        int leftIdx = tag.lastIndexOf('/');
        int rightIdx = tag.lastIndexOf('.');
        String validTag = tag.substring(leftIdx + 1, rightIdx);
        return validTag;
    }
}
