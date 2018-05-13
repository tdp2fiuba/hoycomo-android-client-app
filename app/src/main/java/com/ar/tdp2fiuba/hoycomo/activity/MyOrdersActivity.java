package com.ar.tdp2fiuba.hoycomo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.adapter.MyOrderAdapter;
import com.ar.tdp2fiuba.hoycomo.model.Order;
import com.ar.tdp2fiuba.hoycomo.model.User;
import com.ar.tdp2fiuba.hoycomo.service.OrderService;
import com.ar.tdp2fiuba.hoycomo.service.UserAuthenticationManager;
import com.ar.tdp2fiuba.hoycomo.utils.view.RecyclerViewEmptySupport;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Collections;

public class MyOrdersActivity extends AppCompatActivity {

    private User mUser;
    private MyOrderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (UserAuthenticationManager.isUserLoggedIn(this)) {
            mUser = UserAuthenticationManager.getUser(this);
        }

        populateOrders();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void populateOrders() {
        RecyclerViewEmptySupport recyclerView = (RecyclerViewEmptySupport) findViewById(R.id.my_orders_item_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setEmptyView(findViewById(R.id.my_orders_empty_view));
        mAdapter = new MyOrderAdapter(this);
        recyclerView.setAdapter(mAdapter);

        retrieveActiveOrders();
    }

    private void retrieveActiveOrders() {
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
                            mAdapter.add(gson.fromJson(response.getJSONObject(i).toString(), Order.class));
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
                Toast.makeText(MyOrdersActivity.this, R.string.error_no_orders, Toast.LENGTH_LONG).show();
            }
        };
        startLoading();
        OrderService.getActiveOrders(mUser.getUserId(), successListener, errorListener);
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
