package es.ucm.fdi.googlebooksclient.api;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

import es.ucm.fdi.googlebooksclient.BookInfo;

public class BookLoader extends AsyncTaskLoader<List<BookInfo>> {
    private String queryString;
    private String printType;

    public BookLoader(@NonNull Context context, String queryString, String printType) {
        super(context);
        this.queryString = queryString;
        this.printType = printType;
    }

    @Nullable
    @Override
    public List<BookInfo> loadInBackground() {
        return new BookAPI().getBookInfoJson(queryString, printType);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        super.forceLoad(); //Forza la carga de los resultados
    }
}
