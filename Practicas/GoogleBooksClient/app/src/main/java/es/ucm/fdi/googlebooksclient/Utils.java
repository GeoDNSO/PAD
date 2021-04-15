package es.ucm.fdi.googlebooksclient;

import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static void closeInputStream(InputStream is){
        try {
            if(is != null)
                is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String jsonUrlCorrector(String json_data) {
        json_data = json_data.replace("\"", "");
        return json_data;
    }
}
