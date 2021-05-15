package es.ucm.fdi.tieryourlikes.utilities;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

import es.ucm.fdi.tieryourlikes.R;

public class AppLanguages {

    private static String[] languages;
    private static Map<String, String> langMap;

    public AppLanguages(Activity activity) {
        String spanish = activity.getString(R.string.spanish);
        String english = activity.getString(R.string.english);
        String french = activity.getString(R.string.french);

        languages = new String[]{spanish, english, french};

        langMap = new HashMap<>();
        langMap.put(spanish, "es");
        langMap.put(english, "en");
        langMap.put(french, "fr");
    }

    public static String[] getLanguages(){
        return languages;
    }

    public static String getLangTag(String language){
        return langMap.get(language);
    }
}
