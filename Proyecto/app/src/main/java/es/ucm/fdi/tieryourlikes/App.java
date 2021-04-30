package es.ucm.fdi.tieryourlikes;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.io.FileNotFoundException;

import es.ucm.fdi.tieryourlikes.ui.tier.TierFragment;
import es.ucm.fdi.tieryourlikes.model.User;


public class App {

    private SessionManager sessionManager;

    private static App app;

    private Context context;
    private Menu menu;
    private BottomNavigationView bottomNavigationView;
    private NavigationView drawerNavigationView;

    private MainActivity mainActivity;

    private App(Context context) {
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    public static App getInstance(Context ctx) {
        if (app == null)
            app = new App(ctx);
        app.context = ctx;
        app.sessionManager.setContext(ctx);
        return app;
    }

    public static App getInstance() {
        return app;
    }

    public boolean isLogged() {
        return sessionManager.isLogged();
    }

    public void setMenu(Menu menu) {
    }

    public void logout() {
        sessionManager.logout();
    }

    public void setBottomNavigationView(BottomNavigationView bottomNavView) {
        this.bottomNavigationView = bottomNavView;
    }

    public void setUserSession(User user) {
        sessionManager.setLogged(true);
        sessionManager.setUserInfo(user);
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void saveTierImage(Context context, Bitmap bitmap) {
        try {
            MediaManager.saveImage(bitmap, context, AppConstants.GALLERY_FOLDER);
            Toast.makeText(context, context.getString(R.string.saved_tier_message), Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.error_saved_tier_message), Toast.LENGTH_SHORT).show();
        }
    }
}
