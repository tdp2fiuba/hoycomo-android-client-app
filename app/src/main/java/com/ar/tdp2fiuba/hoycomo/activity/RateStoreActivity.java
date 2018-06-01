package com.ar.tdp2fiuba.hoycomo.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.model.Review;
import com.ar.tdp2fiuba.hoycomo.service.StoreService;
import com.ar.tdp2fiuba.hoycomo.service.firebase.HoyComoFirebaseMessagingService;

import org.json.JSONObject;

public class RateStoreActivity extends AppCompatActivity {

    public static final String ARG_STORE_ID_TO_RATE = "store_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_store);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton sendFab = (FloatingActionButton) findViewById(R.id.rate_store_send_button);
        sendFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRate();
            }
        });

        dismissNotification();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void sendRate() {
        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                stopLoading();
                Toast.makeText(RateStoreActivity.this, R.string.thanks_rate, Toast.LENGTH_LONG).show();
                finish();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopLoading();
                error.printStackTrace();
                Toast.makeText(RateStoreActivity.this, R.string.error_couldnt_post_review, Toast.LENGTH_LONG).show();
            }
        };
        startLoading();
        StoreService.postReview(getIntent().getStringExtra(ARG_STORE_ID_TO_RATE), buildReviewToPost(), successListener, errorListener);
    }

    private void dismissNotification() {
        if (getIntent() != null) {
            final int notificationId = getIntent().getIntExtra(HoyComoFirebaseMessagingService.ARG_NOTIFICATION_ID, -1);
            if (notificationId != -1) {
                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(notificationId);
            }
        }
    }

    private void startLoading() {
        findViewById(R.id.rate_store_content).setVisibility(View.GONE);
        findViewById(R.id.rate_store_send_button).setVisibility(View.GONE);

        findViewById(R.id.rate_store_loading).setVisibility(View.VISIBLE);
    }

    private void stopLoading() {
        findViewById(R.id.rate_store_content).setVisibility(View.VISIBLE);
        findViewById(R.id.rate_store_send_button).setVisibility(View.VISIBLE);

        findViewById(R.id.rate_store_loading).setVisibility(View.GONE);
    }

    private Review buildReviewToPost() {
        AppCompatRatingBar ratingBar = findViewById(R.id.rate_store_rating_bar);
        Double rating = Double.parseDouble(Float.toString(ratingBar.getRating()));

        EditText commentsInput = findViewById(R.id.rate_store_input_comments);
        String comments = !TextUtils.isEmpty(commentsInput.getText()) ? commentsInput.getText().toString() : null;

        return new Review(rating, comments);
    }

}
