package es.ucm.fdi.tieryourlikes.utilities;

import android.app.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class AppUtils {

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
