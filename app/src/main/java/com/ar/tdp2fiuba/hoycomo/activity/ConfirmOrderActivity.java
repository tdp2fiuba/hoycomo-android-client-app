package com.ar.tdp2fiuba.hoycomo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.model.DelayTime;
import com.ar.tdp2fiuba.hoycomo.model.Order;
import com.ar.tdp2fiuba.hoycomo.service.OrderService;

public class ConfirmOrderActivity extends AppCompatActivity {

    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (savedInstanceState == null) {
            mOrder = OrderService.getMyCurrentOrder();
            displayStoreInfo();
            displayOrderInfo();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void sendOrder(View v) {
        // TODO: 6/5/18 Send Order to API.
    }

    private void displayStoreInfo() {
        ((TextView) findViewById(R.id.confirm_order_store_name)).setText(mOrder.getStore().getName());
        ((TextView) findViewById(R.id.confirm_order_store_address)).setText(mOrder.getStore().getAddress().getName());
    }

    private void displayOrderInfo() {
        ((TextView) findViewById(R.id.confirm_order_total_price)).setText("$" + String.valueOf(mOrder.getPrice()));

        setEstimatedTime();
    }

    private void setEstimatedTime() {
        TextView estimatedTimeView = (TextView) findViewById(R.id.confirm_order_estimated_time);

        DelayTime storeDelayTime = mOrder.getStore().getDelayTime();
        if (storeDelayTime != null) {
            String minDelayTime = storeDelayTime.getMin() != null ? storeDelayTime.getMin().toString() : null;
            String maxDelayTime = storeDelayTime.getMax().toString();
            String estimatedTime = minDelayTime != null ?
                    getResources().getString(R.string.minutes_range)
                            .replace(":min", minDelayTime)
                            .replace(":max", maxDelayTime) :
                    getResources().getString(R.string.up_to_minutes)
                            .replace(":max", maxDelayTime);
            estimatedTimeView.setText(estimatedTime);
        } else {
            estimatedTimeView.setText("-");
        }
    }
}
