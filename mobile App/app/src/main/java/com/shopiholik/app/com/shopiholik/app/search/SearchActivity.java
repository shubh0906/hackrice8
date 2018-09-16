package com.shopiholik.app.com.shopiholik.app.search;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shopiholik.app.LandingPage;
import com.shopiholik.app.R;
import com.shopiholik.app.com.shopiholik.app.DetailPageActivity;
import com.shopiholik.app.model.OfferData;
import com.shopiholik.app.model.OfferLogo;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author agrawroh
 * @version v1.0
 */
public class SearchActivity extends AppCompatActivity implements OfferSearchAdapter.ContactsAdapterListener {
    private static final String TAG = SearchActivity.class.getSimpleName();
    private OfferSearchAdapter mAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Decorate Toolbar */
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.toolbar_title);
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        List<OfferData> offerList = new ArrayList<>();
        mAdapter = new OfferSearchAdapter(this, offerList, this);

        /* White Notification Bar */
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new OfferDetailsItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);

        /* Fetch Offers */
        recyclerView.getRecycledViewPool().clear();
        if (null != getIntent().getExtras()) {
            List<OfferData> offerDataList = getIntent().getExtras().getParcelableArrayList("content");
            if (!CollectionUtils.isEmpty(offerDataList)) {
                offerList.clear();
                offerList.addAll(offerDataList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        /* Associate Searchable Configuration With The SearchView */
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        /* Listening To Search Query Text Change */
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /* Filter Recycler View When Query Submitted */
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                /* Filter Recycler View When Text Is Changed */
                mAdapter.getFilter().filter(query);
                return false;
            }
        });

        /* If Query Passed */
        final String query = getIntent().getStringExtra("query");
        if (null != query && !"".equals(query)) {
            searchView.post(new Runnable() {
                @Override
                public void run() {
                    searchView.setQuery(query, false);
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.action_search || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        /* Close Search View When Back Button Pressed */
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onContactSelected(OfferData offerData) {
        //Toast.makeText(getApplicationContext(), "Selected: " + offerData.getBrandName() + ", " + offerData.getOfferPercentage(), Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(), DetailPageActivity.class);
        i.putExtra("logo", offerData.getBrandeImageURL());
        startActivity(i);
    }
}
