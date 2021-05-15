package es.ucm.fdi.tieryourlikes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import java.util.Locale;

import es.ucm.fdi.tieryourlikes.model.User;

import static android.content.ContentValues.TAG;

public class SessionManager {

    private final String SHARED_PRIVATE_FILE = "pref";
    private SharedPreferences prefs;
    private SharedPreferences langPrefs;
    private SharedPreferences.Editor editor;
    private Context context;
    private User sessionUser;

    public SessionManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(SHARED_PRIVATE_FILE, Context.MODE_PRIVATE);
        this.editor = prefs.edit();

        this.langPrefs = context.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = langPrefs.getString(AppConstants.APP_LANG, "es");
        setLocale(language);

        setSessionUserData();
    }

    public void setContext(Context context){
        this.context = context;
        this.prefs = context.getSharedPreferences(SHARED_PRIVATE_FILE, Context.MODE_PRIVATE);
        this.editor = prefs.edit();
        setSessionUserData();
    }

    public void logout() {
        this.editor.clear();
        this.editor.commit();
        setSessionUserData();
    }

    public String getUsername() {
        String username = prefs.getString(AppConstants.USERNAME,"");
        return username;
    }

    public boolean isAdmin(){
        return (this.sessionUser != null && this.sessionUser.getRol().equals(AppConstants.ADMIN_USER));
    }

    public String getEmail() {
        String email = prefs.getString(AppConstants.EMAIL,"");
        return email;
    }

    public void setLogged(boolean logged){
        editor.putBoolean(AppConstants.LOGGED, logged).commit();
    }

    public boolean isLogged(){
        return prefs.getBoolean(AppConstants.LOGGED, false);
    }

    public void setUserInfo(User user) {
        editor.putString(AppConstants.USERNAME, user.getUsername());
        editor.putString(AppConstants.PASSWORD, user.getPassword());
        editor.putString(AppConstants.EMAIL, user.getEmail());
        editor.putString(AppConstants.DB_ICON_KEY, user.getIcon());
        editor.putString(AppConstants.DB_CREATION_TIME, user.getCreationTime());
        editor.putString(AppConstants.DB_ROL, user.getRol());

        editor.commit();
        setSessionUserData();
    }

    private void setSessionUserData(){
        String username =  prefs.getString(AppConstants.USERNAME,"");
        String email = prefs.getString(AppConstants.EMAIL,"");
        String password = prefs.getString(AppConstants.PASSWORD, "");
        String iconTag = prefs.getString(AppConstants.DB_ICON_KEY, "");
        String creation_time = prefs.getString(AppConstants.DB_CREATION_TIME, "");
        String rol = prefs.getString(AppConstants.DB_ROL, "");
        this.sessionUser = new User(username, password, email, iconTag, creation_time, rol);
    }

    public User getUser(){
        if(this.sessionUser == null){
            setSessionUserData();
        }
        return this.sessionUser;
    }


    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);

        Configuration configuration = new Configuration();
        configuration.setLocale(locale);

        Resources resources = context.getResources();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        SharedPreferences.Editor editor = context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString(AppConstants.APP_LANG, lang);
        editor.apply();
    }

    public void loadLocale(){
        String language = langPrefs.getString(AppConstants.APP_LANG, "es");
        setLocale(language);
    }

}

