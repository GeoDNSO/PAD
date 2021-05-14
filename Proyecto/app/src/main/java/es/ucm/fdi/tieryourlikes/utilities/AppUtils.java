package es.ucm.fdi.tieryourlikes.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import es.ucm.fdi.tieryourlikes.R;
import es.ucm.fdi.tieryourlikes.model.TierRow;
import es.ucm.fdi.tieryourlikes.networking.SimpleRequest;
import es.ucm.fdi.tieryourlikes.ui.tier.TierFragment;
import es.ucm.fdi.tieryourlikes.ui.tier.listeners.TierElementTouchListener;
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


    @SuppressLint("ClickableViewAccessibility")
    public static ImageView loadTierImageViewFromAPI(String url, Activity activity, View root){
        ImageView imageView = new ImageView(activity);

        int size = activity.getResources().getDimensionPixelSize(R.dimen.tier_row_images);

        //Configuración  necesario para que tenga el tamaño fijo definido en R.dimen.tier_row_images
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(size,size);
        imageView.setLayoutParams(parms);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        try{
            Glide.with(activity)
                    .load(SimpleRequest.getImageDirectory() + url)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .apply(new RequestOptions().override(size))
                    .error(R.drawable.ic_baseline_error_24)
                    .listener(new CustomRequestListener(root, size, url, imageView))
                    .into(imageView);

        }catch (Exception exception){
        }

        imageView.setOnTouchListener(new TierElementTouchListener(url));
        //imageView.setOnDragListener(new TierElementDragListener());
        imageView.bringToFront();

        return imageView;
    }

    //Apaño para que glide cargue correctamente las imagenes
    static class CustomRequestListener implements RequestListener<Drawable> {
        private int tries = 0;
        private static final int MAX_TRIES = 100000;

        private View root;
        private int size;
        private String image_url;
        private ImageView imageView;

        public CustomRequestListener(View root, int size, String image_url, ImageView imageView) {
            this.root = root;
            this.size = size;
            this.image_url = image_url;
            this.imageView = imageView;
        }

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            if(tries < MAX_TRIES){
                try{
                    Glide.with(root)
                            .load(SimpleRequest.getImageDirectory() + image_url)
                            .placeholder(R.drawable.ic_launcher_foreground)
                            .apply(new RequestOptions().override(size))
                            .error(R.drawable.ic_baseline_error_24)
                            //.listener(this)
                            .into(imageView);
                }catch (Exception exception){

                }

            }
            tries++;
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            return false;
        }
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
