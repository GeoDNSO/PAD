package es.ucm.fdi.googlebooksclient.api;

import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import es.ucm.fdi.googlebooksclient.BookInfo;
import es.ucm.fdi.googlebooksclient.Utils;

public class BookAPI {

    //CUANDO NO HAY RESULTADO --> "totalItems": 0

    final String MAX_RESULTS = "40";
    final String API_BOOK_URL = "https://www.googleapis.com/books/v1/volumes?q=%s&" +
            "printType=%s&maxResults=%s";

    final String API_BOOK_URL_2 = "https://www.googleapis.com/books/v1/volumes?q=%s&" +
            "printType=%s&maxResults=%s&startIndex=%s";

    final int READ_TIMEOUT = 10000;
    final int CONNECT_TIMEOUT = 15000;
    final String GET_METHOD = "GET";

    private int i = 0;


    public List<BookInfo> getBookInfoJson(String queryString, String printType){

        String requestURL = String.format(API_BOOK_URL, queryString, printType, MAX_RESULTS);

        Log.i("BOOK_API", "Se enviará una petición a la URL: " + requestURL);

        URL url = null;
        InputStream is = null;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(requestURL);

            /* Open a connection to that URL. */
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod(GET_METHOD);
            urlConnection.setDoInput(true);

            urlConnection.connect();

            int response = urlConnection.getResponseCode();

            is = urlConnection.getInputStream();
            String contentAsString = convertIsToString(is);

            return  BookInfo.fromJsonResponse(contentAsString);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
            Utils.closeInputStream(is);
        }

        return null;
    }

    public String convertIsToString(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line + "\n");
        }
        if (builder.length() == 0) {
            return null;
        }
        return builder.toString();
    }

    public void newSearch(){
        i = 0;
    }

}
