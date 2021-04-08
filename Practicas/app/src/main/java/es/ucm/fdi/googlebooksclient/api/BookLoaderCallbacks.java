package es.ucm.fdi.googlebooksclient.api;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import es.ucm.fdi.googlebooksclient.MainActivity;

public class BookLoaderCallbacks implements LoaderManager.LoaderCallbacks<String> {
    public static final String EXTRA_QUERY = "EXTRA_QUERY";
    public static final String EXTRA_PRINT_TYPE = "EXTRA_PRINT_TYPE";

    private MainActivity context;

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        return new BookLoader(context, args.getString(EXTRA_QUERY), args.getString(EXTRA_PRINT_TYPE));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    public void setContext(MainActivity context) {
        this.context = context;
    }

    public void updateFromActivityMain(){
        //context.
    }

}
