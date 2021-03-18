package es.ucm.fdi.tieryourlikes;

import android.content.Context;
import android.view.Menu;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

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

    public boolean isLogged() {
        return false;
    }

    public void setMenu(Menu menu) {
    }

    public void logout() {
        sessionManager.logout();
    }

    public void setBottomNavigationView(BottomNavigationView bottomNavView) {
        this.bottomNavigationView = bottomNavView;
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
