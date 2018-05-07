package com.ar.tdp2fiuba.hoycomo.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.model.DelayTime;
import com.ar.tdp2fiuba.hoycomo.model.Order;
import com.ar.tdp2fiuba.hoycomo.model.User;
import com.ar.tdp2fiuba.hoycomo.service.OrderService;
import com.ar.tdp2fiuba.hoycomo.service.UserAuthenticationManager;
import com.ar.tdp2fiuba.hoycomo.utils.SharedPreferencesUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;

import static com.ar.tdp2fiuba.hoycomo.utils.SharedPreferencesConstants.SHP_USER;

public class ConfirmOrderActivity extends AppCompatActivity {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private final LatLngBounds CABABounds = new LatLngBounds(
            new LatLng(-34.691381, -58.537996),
            new LatLng(-34.530064, -58.327278)
    );

    private static final String TAG = "ConfirmOrder";

    private Order mOrder;
    private boolean userAddressOK;
    private boolean newUserAddress;

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
            displayUserAddress();
            displayOrderInfo();
            setListeners();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getAddress());
                ((TextView) findViewById(R.id.confirm_order_user_address)).setText(place.getAddress());
                userAddressOK = true;
                newUserAddress = true;
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void sendOrder(View v) {
        if (userAddressOK) {
            // TODO: 6/5/18 Send Order to API.
        } else {
            Toast.makeText(this, R.string.didnt_set_address, Toast.LENGTH_LONG).show();
        }
    }

    private void displayStoreInfo() {
        ((TextView) findViewById(R.id.confirm_order_store_name)).setText(mOrder.getStore().getName());
        ((TextView) findViewById(R.id.confirm_order_store_address)).setText(mOrder.getStore().getAddress().getName());
    }

    private void displayUserAddress() {
        userAddressOK = false;
        if (UserAuthenticationManager.isUserLoggedIn(this)) {
            String serializedUser = SharedPreferencesUtils.load(this, SHP_USER, null);
            if (serializedUser != null) {
                User user = new Gson().fromJson(serializedUser, User.class);
                if (user.getAddress() != null && user.getAddress().getName() != null) {
                    ((TextView) findViewById(R.id.confirm_order_user_address)).setText(user.getAddress().getName());
                    userAddressOK = true;
                }
            }
        }
    }

    private void displayOrderInfo() {
        ((TextView) findViewById(R.id.confirm_order_total_price)).setText("$" + String.valueOf(mOrder.getPrice()));
        setEstimatedTime();
    }

    private void setListeners() {
        setNotesListener();
        setUserAddressListener();
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

    private void setNotesListener() {
        findViewById(R.id.confirm_order_notes_label).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText notesInput = findViewById(R.id.confirm_order_notes_input);
                notesInput.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
    }

    private void setUserAddressListener() {
        findViewById(R.id.confirm_order_user_address_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlaceAutocomplete();
            }
        });
    }

    private void showPlaceAutocomplete() {
        AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .setCountry("AR")
                .build();

        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(filter)
                            .setBoundsBias(CABABounds)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            Log.e(TAG, "There was an error on Place Autocomplete invocation");
        }
    }
}
