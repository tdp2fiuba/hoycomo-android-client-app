package com.ar.tdp2fiuba.hoycomo.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
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
import com.ar.tdp2fiuba.hoycomo.service.firebase.HoyComoFirebaseMessagingService;
import com.ar.tdp2fiuba.hoycomo.utils.view.RecyclerViewEmptySupport;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.ar.tdp2fiuba.hoycomo.activity.MyOrderActivity.ARG_ORDER;

public class MyOrdersActivity extends AppCompatActivity implements MyOrderAdapter.OnMyOrdersInteractionListener {

    public static final String ARG_ORDER_ID_TO_REJECT = "order_id_to_reject";

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

        if (getIntent() != null && getIntent().getStringExtra(ARG_ORDER_ID_TO_REJECT) != null) {
            String orderIdToReject = getIntent().getStringExtra(ARG_ORDER_ID_TO_REJECT);
            openRejectOrderDeliveryDialog(orderIdToReject);
        }

        populateOrders();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onOrderTap(Order item) {
        Intent intent = new Intent(this, MyOrderActivity.class);
        intent.putExtra(ARG_ORDER, new Gson().toJson(item));
        startActivity(intent);
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

    private void openRejectOrderDeliveryDialog(final String orderIdToReject) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(R.string.my_order_not_delivered)
                .setMessage(R.string.my_order_not_delivered_body_dialog)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        rejectOrder(orderIdToReject);
                    }
                })
                .setNegativeButton(R.string.i_did_receive_it, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void rejectOrder(String orderId) {
        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissNotification();
                stopLoading();
                Toast.makeText(MyOrdersActivity.this, R.string.order_updated, Toast.LENGTH_LONG).show();
                finish();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stopLoading();
                error.printStackTrace();
                Toast.makeText(MyOrdersActivity.this, R.string.error_order_update, Toast.LENGTH_LONG).show();
            }
        };
        startLoading();
        OrderService.rejectOrder(orderId, successListener, errorListener);
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
