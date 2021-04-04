package es.ucm.fdi.tieryourlikes.utilities;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import es.ucm.fdi.tieryourlikes.R;


//TODO probablemente se borre...
public class AppDrawable {

    private static AppDrawable appDrawable;
    private static Resources resources;


    public static Drawable getDrawable(int id){
        Drawable drawable = ResourcesCompat.getDrawable(resources, id, null);
        return drawable;
    }


    public Resources getResources() {
        return resources;
    }

    public static void setResources(Resources resources) {
        AppDrawable.resources = resources;
    }
}
