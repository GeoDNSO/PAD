package es.ucm.fdi.tieryourlikes.utilities;

public class AppUtils {

    public static void sleep(long milis){
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
