package com.ar.tdp2fiuba.hoycomo.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.service.firebase.HoyComoFirebaseMessagingService;

public class RateStoreActivity extends AppCompatActivity {

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
        // TODO: 19/5/18 Implement call to API
        Toast.makeText(this, R.string.thanks_rate, Toast.LENGTH_SHORT).show();
        finish();
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

}
