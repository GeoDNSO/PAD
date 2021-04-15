package es.ucm.fdi.googlebooksclient.api;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

import es.ucm.fdi.googlebooksclient.BookInfo;
import es.ucm.fdi.googlebooksclient.MainActivity;

public class BookLoader extends AsyncTaskLoader<List<BookInfo>> {
    private String queryString;
    private String printType;
    private BookAPI bookAPI;

    public BookLoader(@NonNull Context context, String queryString, String printType) {
        super(context);
        this.queryString = queryString;
        this.printType = printType;
        this.bookAPI = new BookAPI();
    }

    @Nullable
    @Override
    public List<BookInfo> loadInBackground() {
        return bookAPI.getBookInfoJson(queryString, printType);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        super.forceLoad(); //Forza la carga de los resultados
    }
}
