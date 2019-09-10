package android.example.newsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = NewsLoader.class.getName();
    private static final String GUARDIAN_API = "http://content.guardianapis.com/search?q=debates&api-key=test";
    private NewsAdapter adapter;
    private TextView emptyTextView;
    private ProgressBar loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new NewsAdapter(this,new ArrayList<News>());
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        emptyTextView = (TextView) findViewById(R.id.empty_text_view);
        listView.setEmptyView(emptyTextView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = adapter.getItem(position);
                Uri webpage = Uri.parse(news.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        loadingSpinner = (ProgressBar) findViewById(R.id.loading_spinner);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()){
            getSupportLoaderManager().initLoader(1,null,this);
        }

        else {
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);
            emptyTextView.setText(R.string.no_internet);
        }



    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int i, @Nullable Bundle bundle) {
        Log.v("MainActivity","onCreateLoader");
        return new NewsLoader(this,GUARDIAN_API);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> news) {
        Log.v("MainActivity","onFinishLoader");
        emptyTextView.setText(R.string.no_earthquake_found);
        loadingSpinner.setVisibility(View.GONE);
        adapter.clear();
        if (news != null && !news.isEmpty()){
            adapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {
        Log.v("MainActivity","onLoaderReset");
        adapter.clear();
    }

    public static class NewsLoader extends AsyncTaskLoader<List<News>>{

        private String mUrl;
        public NewsLoader(@NonNull Context context,String url) {
            super(context);
            mUrl = url;
        }

        @Nullable
        @Override
        public List<News> loadInBackground() {
            Log.v("MainActivity","LoadInBackground");
            if(mUrl == null){
                return null;
            }
            List<News> newsList = QueryUtils.fetchNewsData(mUrl);
            return newsList;
        }

        @Override
        protected void onStartLoading() {
            Log.v("MainActivity","onStartLoading");
            super.onStartLoading();
            forceLoad();
        }
    }
}
