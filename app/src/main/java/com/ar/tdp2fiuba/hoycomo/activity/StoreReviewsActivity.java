package com.ar.tdp2fiuba.hoycomo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.adapter.ReviewRecyclerViewAdapter;
import com.ar.tdp2fiuba.hoycomo.model.Review;
import com.ar.tdp2fiuba.hoycomo.model.Store;
import com.ar.tdp2fiuba.hoycomo.model.User;
import com.ar.tdp2fiuba.hoycomo.service.StoreService;
import com.ar.tdp2fiuba.hoycomo.utils.view.RecyclerViewEmptySupport;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

public class StoreReviewsActivity extends AppCompatActivity implements ReviewRecyclerViewAdapter.OnReviewInteractionListener {

    public final static String ARG_REVIEWED_STORE = "store";

    private Store mStore;
    private ReviewRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_reviews);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent() != null && getIntent().getStringExtra(ARG_REVIEWED_STORE) != null) {
            String serializedStore = getIntent().getStringExtra(ARG_REVIEWED_STORE);
            mStore = new Gson().fromJson(serializedStore, Store.class);

            displayStoreInfo();
            populateReviews();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onExpandReview(Review item) {
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra(ReviewActivity.ARG_FULL_REVIEW, new Gson().toJson(item));
        startActivity(intent);
    }

    private void displayStoreInfo() {
        ((TextView) findViewById(R.id.store_reviews_store_name)).setText(mStore.getName());
        ((TextView) findViewById(R.id.store_reviews_store_rating)).setText(String.format("%.1f", mStore.getRating()));
    }

    private void populateReviews() {
        RecyclerViewEmptySupport recyclerView = (RecyclerViewEmptySupport) findViewById(R.id.store_reviews_item_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setEmptyView(findViewById(R.id.store_reviews_empty_view));
        mAdapter = new ReviewRecyclerViewAdapter(this);
        recyclerView.setAdapter(mAdapter);

        // TODO: 30/5/18 Call actual service
        retrieveReviews();
    }

    private void retrieveReviews() {
        Response.Listener<JSONArray> successListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                stopLoading();
                if (response.length() > 0) {
                    final Gson gson = new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            .create();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            mAdapter.add(gson.fromJson(response.getJSONObject(i).toString(), Review.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopLoading();
                error.printStackTrace();
                Toast.makeText(StoreReviewsActivity.this, R.string.error_no_reviews, Toast.LENGTH_LONG).show();
            }
        };
        startLoading();
        StoreService.getReviews(mStore.getId(), successListener, errorListener);
    }

    private void startLoading() {
        if (mAdapter != null) {
            mAdapter.addLoadingFooter();
        }
    }

    private void stopLoading() {
        if (mAdapter != null) {
            mAdapter.removeLoadingFooter();
        }
    }
}
