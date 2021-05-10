package es.ucm.fdi.tieryourlikes.utilities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.TierRow;
import petrov.kristiyan.colorpicker.ColorPicker;

public class CustomColorPicker {

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
        //return  Color.argb(1, d, d, d); //No se puede usar Color.valueOf, Color.rgb porque tiene que ser API > 26...
    }


}
