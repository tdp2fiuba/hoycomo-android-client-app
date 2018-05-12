package com.ar.tdp2fiuba.hoycomo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.adapter.MyOrderItemAdapter;
import com.ar.tdp2fiuba.hoycomo.model.Order;
import com.ar.tdp2fiuba.hoycomo.service.OrderService;

public class MyOrderActivity extends AppCompatActivity {

    private static final int REQ_CODE_CONFIRMATION = 1;

    public static final String ARG_SENT = "sent";
    private boolean sent = false;

    private Order mOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mOrder = OrderService.getMyCurrentOrder();
        if (getIntent() != null) {
            sent = getIntent().getBooleanExtra(ARG_SENT, false);
            invalidateOptionsMenu();
        }

        displayStoreInfo();
        populateOrderItems();
        updateSubtotal();

        displayStatus();
        displayButtons();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_order_menu, menu);
        if (sent) {
            menu.findItem(R.id.my_order_menu_cancel).setVisible(false);
        } else {
            menu.findItem(R.id.my_order_menu_cancel).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.my_order_menu_cancel:
                if (!sent) {
                    OrderService.clearOrder();
                    Toast.makeText(this, R.string.order_cancelled, Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_CONFIRMATION) {
            if (resultCode == RESULT_OK) {
                finish();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addMoreItems(View v) {
        finish();
    }

    public void continueToConfirmation(View v) {
        Intent intent = new Intent(this, ConfirmOrderActivity.class);
        startActivityForResult(intent, REQ_CODE_CONFIRMATION);
    }

    private void displayStoreInfo() {
        ((TextView) findViewById(R.id.my_order_store_name)).setText(mOrder.getStore().getName());
        ((TextView) findViewById(R.id.my_order_store_address)).setText(mOrder.getStore().getAddress().getName());
    }

    private void populateOrderItems() {
        if (OrderService.isThereCurrentOrder()) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_order_item_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
            MyOrderItemAdapter adapter = new MyOrderItemAdapter(OrderService.getMyCurrentOrder().getItems());
            recyclerView.setAdapter(adapter);
            recyclerView.setNestedScrollingEnabled(false);
        }
    }

    private void updateSubtotal() {
        TextView subtotalView = (TextView) findViewById(R.id.my_order_subtotal);
        subtotalView.setText(
                subtotalView.getText().toString().replace(":monto", "$" + String.valueOf(OrderService.getMyCurrentOrder().getPrice()))
        );
    }

    private void displayStatus() {
        TextView status = (TextView) findViewById(R.id.my_order_status);
        if (sent) {
            status.setVisibility(View.VISIBLE);
            status.setText(mOrder.getStatus().toString(this).toUpperCase());
        } else {
            status.setVisibility(View.GONE);
        }
    }

    private void displayButtons() {
        Button addMoreFoodButton = (Button) findViewById(R.id.my_order_add_more_food_button);
        Button continueButton = (Button) findViewById(R.id.my_order_continue_button);
        if (sent) {
            addMoreFoodButton.setVisibility(View.GONE);
            continueButton.setVisibility(View.GONE);
        } else {
            addMoreFoodButton.setVisibility(View.VISIBLE);
            continueButton.setVisibility(View.VISIBLE);
        }
    }
}
