package com.ar.tdp2fiuba.hoycomo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.model.Review;
import com.google.gson.Gson;

public class ReviewActivity extends AppCompatActivity {

    public static final String ARG_FULL_REVIEW = "review";
    private Review mReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent() != null && getIntent().getStringExtra(ARG_FULL_REVIEW) != null) {
            String serializedReview = getIntent().getStringExtra(ARG_FULL_REVIEW);
            mReview = new Gson().fromJson(serializedReview, Review.class);

            displayFullReview();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void displayFullReview() {
        ((TextView) findViewById(R.id.review_username)).setText(mReview.getUser().getFirstName() + ' ' + mReview.getUser().getLastName());
        ((TextView) findViewById(R.id.review_rating)).setText(String.format("%.1f", mReview.getRating()));
        ((TextView) findViewById(R.id.review_timestamp)).setText(mReview.getTimestamp());
        ((TextView) findViewById(R.id.review_body)).setText(mReview.getBody());
    }
}
