package es.ucm.fdi.tieryourlikes;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private final String SHARED_PRIVATE_FILE = "pref";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        this.prefs = context.getSharedPreferences(SHARED_PRIVATE_FILE, Context.MODE_PRIVATE);
        this.editor = prefs.edit();
    }

    public void setContext(Context context){
        this.context = context;
        this.prefs = context.getSharedPreferences(SHARED_PRIVATE_FILE, Context.MODE_PRIVATE);
        this.editor = prefs.edit();
    }

    public void setUsername(/*String username*/) {
        /*editor.putString(AppConstants.USERNAME, username).commit();*/
    }

    public String getUsername() {
        /*
        String username = prefs.getString(AppConstants.USERNAME,"");
        return username;
        */
         return "GETUSERNAME NO HECHO";
    }

    public void setLogged(boolean logged){
        editor.putBoolean(AppConstants.LOGGED, logged).commit();
    }

    public boolean isLogged(){
        return prefs.getBoolean(AppConstants.LOGGED, false);
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }

    public void setUserInfo(/*TUser user*/) {
        /*
        editor.putString(AppConstants.USERNAME, user.getUsername());
        editor.putString(AppConstants.NAME, user.getName());
        editor.putString(AppConstants.SURNAME, user.getSurname());
        editor.putString(AppConstants.CITY, user.getCity());
        editor.putString(AppConstants.PASSWORD, user.getPassword());
        editor.putString(AppConstants.EMAIL, user.getEmail());
        editor.putString(AppConstants.GENDER, user.getGender());
        editor.putString(AppConstants.BIRTH_DATE, user.getBirthDate());
        editor.putString(AppConstants.PROFILE_PICTURE, "AA");//TODO Usuario con imagen de perfil??
        editor.putString(AppConstants.ADMIN, user.getRol());
        editor.commit();
        */
    }


}

