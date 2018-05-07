package com.ar.tdp2fiuba.hoycomo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.adapter.MyOrderItemAdapter;
import com.ar.tdp2fiuba.hoycomo.model.Order;
import com.ar.tdp2fiuba.hoycomo.service.OrderService;

public class MyOrderActivity extends AppCompatActivity {

    private Order mOrder;
    private MyOrderItemAdapter mAdapter;

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
        displayStoreInfo();
        populateOrderItems();
        updateSubtotal();
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.my_order_menu_cancel:
                OrderService.clearOrder();
                Toast.makeText(this, R.string.order_cancelled, Toast.LENGTH_SHORT).show();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void continueToConfirmation(View v) {
        // TODO: 6/5/18 Go to order confirmation.
    }

    private void displayStoreInfo() {
        mOrder = OrderService.getMyCurrentOrder();
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
            mAdapter = new MyOrderItemAdapter(OrderService.getMyCurrentOrder().getItems());
            recyclerView.setAdapter(mAdapter);
            recyclerView.setNestedScrollingEnabled(false);
        }
    }

    private void updateSubtotal() {
        TextView subtotalView = (TextView) findViewById(R.id.my_order_subtotal);
        subtotalView.setText(
                subtotalView.getText().toString().replace(":monto", "$" + String.valueOf(mAdapter.getItemPriceSum()))
        );
    }
}
