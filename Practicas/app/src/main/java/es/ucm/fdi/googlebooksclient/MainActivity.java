package es.ucm.fdi.googlebooksclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.googlebooksclient.api.BookLoader;

public class MainActivity extends AppCompatActivity {

    public static int BOOK_LOADER_ID = 1;

    //Elementos de la Vista
    private TextView resultsTitle;
    private EditText etTitle;
    private EditText etAuthor;
    private RadioGroup radioGroup;

    //Variables que usará MainActivity
    private BookLoaderCallbacks bookLoaderCallbacks;
    private BookListAdapter bookListAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        bookLoaderCallbacks = new BookLoaderCallbacks();
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        if(loaderManager.getLoader(BOOK_LOADER_ID) != null){
            loaderManager.initLoader(BOOK_LOADER_ID,null, bookLoaderCallbacks);
        }

        bookListAdapter = new BookListAdapter(new ArrayList<>());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(bookListAdapter);

        //bookLoaderCallbacks.setContext(this);//Para poder usar BookLoader le pasamos el contexto
    }

    private void initUI() {
        resultsTitle = findViewById(R.id.tvResultTitle);
        etAuthor = findViewById(R.id.editTextAuthors);
        etTitle = findViewById(R.id.editTextBook);
        radioGroup = findViewById(R.id.radioGroup);
        recyclerView = findViewById(R.id.recyclerView);

        resultsTitle.setVisibility(View.GONE);
    }

    public void searchBooks(View view){

        //Conseguir el radioButton seleccionado
        RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());

        //Parámetros a enviar para la API
        String queryString = etAuthor.getText().toString() + "+" + etTitle.getText().toString();
        if(etAuthor.getText().toString().equals("")){
            queryString = etTitle.getText().toString();
        }
        if(etTitle.getText().toString().equals("")){
            queryString = etAuthor.getText().toString();
        }

        String printType = radioButton.getText().toString();

        //Crear bundle
        Bundle queryBundle = new Bundle();
        queryBundle.putString(BookLoaderCallbacks.EXTRA_QUERY, queryString);
        queryBundle.putString(BookLoaderCallbacks.EXTRA_PRINT_TYPE, printType);

        LoaderManager.getInstance(this)
                .restartLoader(BOOK_LOADER_ID, queryBundle, bookLoaderCallbacks);
    }

    public void updateBooksResultList(List<BookInfo> bookInfos){
        if(bookInfos == null || bookInfos.isEmpty())
            return;
        resultsTitle.setVisibility(View.VISIBLE);

        bookListAdapter.setBookInfoList(bookInfos);
        bookListAdapter.notifyDataSetChanged();
        //recyclerView.setAdapter(bookListAdapter);
    }


    public class BookLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<BookInfo>> {
        public static final String EXTRA_QUERY = "EXTRA_QUERY";
        public static final String EXTRA_PRINT_TYPE = "EXTRA_PRINT_TYPE";

        @NonNull
        @Override
        public Loader<List<BookInfo>> onCreateLoader(int id, @Nullable Bundle args) {
            return new BookLoader(MainActivity.this, args.getString(EXTRA_QUERY), args.getString(EXTRA_PRINT_TYPE));
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<BookInfo>> loader, List<BookInfo> data) {
            updateBooksResultList((data));
        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<BookInfo>> loader) {

        }


    }
}