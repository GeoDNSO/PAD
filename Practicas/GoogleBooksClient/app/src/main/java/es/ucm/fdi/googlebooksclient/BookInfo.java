package es.ucm.fdi.googlebooksclient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookInfo {

    private String title;
    private String authors;
    private URL infoLink;

    public BookInfo(String title, String authors, URL infoLink) {
        this.title = title;
        this.authors = authors;
        this.infoLink = infoLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public URL getInfoLink() {
        return infoLink;
    }

    public void setInfoLink(URL infoLink) {
        this.infoLink = infoLink;
    }

    public static List<BookInfo> fromJsonResponse(String jsonStringList) throws JSONException, MalformedURLException {
        if(jsonStringList == null)
            return null;

        JSONObject jsonObject = new JSONObject(jsonStringList);
        int o = jsonObject.getInt("totalItems");
        if(o == 0)
            return null;

        JSONArray data = jsonObject.getJSONArray("items");
        List<BookInfo> bookInfoList = new ArrayList<>();

        for(int i = 0; i < data.length(); ++i){
            JSONObject jsonBook = data.getJSONObject(i);
            BookInfo bookInfo = toBook(jsonBook);
            bookInfoList.add(bookInfo);
        }

        return bookInfoList;

    }

    public static BookInfo toBook(JSONObject jsonObject) throws MalformedURLException {
        String title = null;
        String authors = null;
        URL infoLink = null;

        //Para evitar errores con los JSON...
        try {
            title = jsonObject.getJSONObject("volumeInfo").getString("title");
        } catch (JSONException e) {
            //e.printStackTrace();
            title = "Sin Título";
        }
        try {
            authors = jsonObject.getJSONObject("volumeInfo").getString("authors");
        } catch (JSONException e) {
            //e.printStackTrace();
            authors = "Sin Autores";
        }
        try {
            String url = jsonObject.getJSONObject("volumeInfo").getString("infoLink");
            infoLink = new URL(url);
        } catch (JSONException e) {
            //e.printStackTrace();
            infoLink = new URL("https://http.cat/404");
        }

        return new BookInfo(title, authors, infoLink);
    }

}

