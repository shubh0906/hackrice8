package com.shopiholik.app;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shopiholik.app.com.shopiholik.app.search.SearchActivity;
import com.shopiholik.app.model.MainCard;
import com.shopiholik.app.model.OfferData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author agrawroh
 * @version v1.0
 */
public class HomePageActivity extends AppCompatActivity implements HomePageAdapter.HomePageAdapterListener {
    private RecyclerView recyclerView;
    private HomePageAdapter adapter;
    private List<MainCard> homePageEntriesList;
    private static List<OfferData> offerDataList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Get Offers */
        if (null == offerDataList && null != getIntent().getExtras()) {
            offerDataList = getIntent().getExtras().getParcelableArrayList("content");
        }

        /* Collapsing ToolBar */
        initCollapsingToolbar();

        /* Get State */
        recyclerView = findViewById(R.id.recycler_view);
        homePageEntriesList = new ArrayList<>();
        adapter = new HomePageAdapter(this, homePageEntriesList, this);

        /* Initiate Recycler View */
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        /* Create Cards */
        createCards();
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Creating Cards.
     */
    private void createCards() {
        int[] covers = new int[]{
                R.drawable.fashionx,
                R.drawable.entertainment,
                R.drawable.ecommerce,
                R.drawable.travel,
                R.drawable.food,
                R.drawable.utilities};

        MainCard card = new MainCard("fashion", "Our models suggest you to ACT NOW as this might be the best deal which you would get in the upcoming month(s).", covers[0]);
        homePageEntriesList.add(card);

        card = new MainCard("entertainment", "We suggest you to wait a bit longer since the deals look a little bleak. You might get better deals in October.", covers[1]);
        homePageEntriesList.add(card);

        card = new MainCard("ecommerce", "Our models suggest you to ACT NOW as this might be the best deal which you would get in the upcoming month(s).", covers[2]);
        homePageEntriesList.add(card);

        card = new MainCard("travel", "Our models suggest you to ACT NOW as this might be the best deal which you would get in the upcoming month(s).", covers[3]);
        homePageEntriesList.add(card);

        card = new MainCard("food", "We suggest you to wait a bit longer since the deals look a little bleak. You might get better deals in October.", covers[4]);
        homePageEntriesList.add(card);

        card = new MainCard("utilities", "We suggest you to wait a bit longer since the deals look a little bleak. You might get better deals in October.", covers[5]);
        homePageEntriesList.add(card);

        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        private GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(@NonNull int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onCardSelected(MainCard mainCard) {
        Toast.makeText(getApplicationContext(), "Selected: " + mainCard.getName(), Toast.LENGTH_LONG).show();
        Intent i = new Intent(getApplicationContext(), SearchActivity.class);
        i.putExtra("content", new ArrayList<>(offerDataList));
        i.putExtra("query", mainCard.getName());
        startActivity(i);
    }
}
