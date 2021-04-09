package es.ucm.fdi.googlebooksclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.googlebooksclient.api.BookLoader;
import es.ucm.fdi.googlebooksclient.api.BookLoaderCallbacks;

public class MainActivity extends AppCompatActivity {

    public static int BOOK_LOADER_ID = 1;

    //Elementos de la Vista
    private NestedScrollView nestedScrollView;
    private TextView resultsTitle;
    private EditText etTitle;
    private EditText etAuthor;
    private RadioGroup radioGroup;
    private ProgressBar progressBar;
    private TextView tvNoResults;

    //Variables que usará MainActivity
    private BookLoaderCallbacks bookLoaderCallbacks;
    private BookListAdapter bookListAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        bookLoaderCallbacks = new BookLoaderCallbacks(this);
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        if(loaderManager.getLoader(BOOK_LOADER_ID) != null){
            loaderManager.initLoader(BOOK_LOADER_ID,null, bookLoaderCallbacks);
        }

        bookListAdapter = new BookListAdapter(this, new ArrayList<>());

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
        progressBar = findViewById(R.id.mainActivityProgressBar);
        tvNoResults = findViewById(R.id.tvNoResults);
        nestedScrollView = findViewById(R.id.mainActivityNSV);

        resultsTitle.setVisibility(View.GONE);

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if(scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                progressBar.setVisibility(View.VISIBLE);
                LoaderManager.getInstance(MainActivity.this).getLoader(BOOK_LOADER_ID).startLoading();
            }
        });
    }

    public void updateBooksResultList(List<BookInfo> bookInfos){
        resultsTitle.setVisibility(View.VISIBLE);
        if(bookInfos == null || bookInfos.isEmpty()){
            tvNoResults.setVisibility(View.VISIBLE);
            return;
        }

        bookListAdapter.setBookInfoList(bookInfos);
        bookListAdapter.notifyDataSetChanged();
    }

    public void searchBooks(View view){

        //Resetear la lista para poder ver la progressBar
        bookListAdapter.clearList();
        bookListAdapter.notifyDataSetChanged();

        tvNoResults.setVisibility(View.GONE);

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

    public void setProgressBarVisibility(int visibility){
        this.progressBar.setVisibility(visibility);
    }
}