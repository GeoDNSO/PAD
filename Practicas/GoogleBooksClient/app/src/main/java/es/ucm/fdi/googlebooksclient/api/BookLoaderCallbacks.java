package es.ucm.fdi.googlebooksclient.api;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.List;

import es.ucm.fdi.googlebooksclient.BookInfo;
import es.ucm.fdi.googlebooksclient.MainActivity;

public class BookLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<BookInfo>> {
    public static final String EXTRA_QUERY = "EXTRA_QUERY";
    public static final String EXTRA_PRINT_TYPE = "EXTRA_PRINT_TYPE";

    private MainActivity mainActivity;

    public BookLoaderCallbacks(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public Loader<List<BookInfo>> onCreateLoader(int id, @Nullable Bundle args) {
        mainActivity.setProgressBarVisibility(View.VISIBLE);
        return new BookLoader(mainActivity, args.getString(EXTRA_QUERY), args.getString(EXTRA_PRINT_TYPE));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<BookInfo>> loader, List<BookInfo> data) {
        mainActivity.updateBooksResultList((data));
        mainActivity.setProgressBarVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<BookInfo>> loader) {

    }
}
