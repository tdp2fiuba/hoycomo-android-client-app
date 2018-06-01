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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.model.Address;
import com.ar.tdp2fiuba.hoycomo.model.Order;
import com.ar.tdp2fiuba.hoycomo.model.User;
import com.ar.tdp2fiuba.hoycomo.service.GeocodingService;
import com.ar.tdp2fiuba.hoycomo.service.OrderService;
import com.ar.tdp2fiuba.hoycomo.service.UserAuthenticationManager;
import com.ar.tdp2fiuba.hoycomo.utils.DateUtils;
import com.ar.tdp2fiuba.hoycomo.utils.SharedPreferencesUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ar.tdp2fiuba.hoycomo.utils.SharedPreferencesConstants.SHP_USER;

public class ConfirmOrderActivity extends AppCompatActivity {

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private final LatLngBounds CABABounds = new LatLngBounds(
            new LatLng(-34.691381, -58.537996),
            new LatLng(-34.530064, -58.327278)
    );

    private static final String TAG = "ConfirmOrder";

    private Order mOrder;
    private Address mPreviousUserAddress;

    private Place mUserPlacePicked;

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
                findViewById(R.id.confirm_order_floor_apartment_input_container).setVisibility(View.VISIBLE);
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getAddress());
                mUserPlacePicked = place;
                ((TextView) findViewById(R.id.confirm_order_user_address)).setText(place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void validateAndSendOrder(View v) {
        validateAddress();
    }

    private void validateAddress() {
        if (mUserPlacePicked != null || mPreviousUserAddress != null) {
            Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    sendOrder();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.e(TAG, "Delivery address validation failed!");
                    stopLoading();
                    Toast.makeText(ConfirmOrderActivity.this, R.string.error_only_caba, Toast.LENGTH_LONG).show();
                }
            };
            startLoading();
            if (mUserPlacePicked != null) {
                GeocodingService.validateAddress(mUserPlacePicked.getAddress().toString(), successListener, errorListener);
            } else {
                GeocodingService.validateAddress(mPreviousUserAddress.getName(), successListener, errorListener);
            }
        } else {
            Toast.makeText(this, R.string.didnt_set_delivery_address, Toast.LENGTH_LONG).show();
        }
    }

    private void sendOrder() {
        if (mUserPlacePicked != null) {
            updateUserAddress();
        }

        mOrder.setDescription(getNotes());

        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                stopLoading();
                Toast.makeText(ConfirmOrderActivity.this, R.string.order_send_success, Toast.LENGTH_SHORT).show();
                OrderService.clearOrder();

                setResult(RESULT_OK);
                finish();
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e(TAG, "Error on sending Order to server");
                stopLoading();
                Toast.makeText(ConfirmOrderActivity.this, R.string.order_send_error, Toast.LENGTH_SHORT).show();
            }
        };
        OrderService.sendOrder(mOrder.toRequest(), successListener, errorListener);
    }

    private void displayStoreInfo() {
        ((TextView) findViewById(R.id.confirm_order_store_name)).setText(mOrder.getStore().getName());
        ((TextView) findViewById(R.id.confirm_order_store_address)).setText(mOrder.getStore().getAddress().getName());
    }

    private void displayUserAddress() {
        if (UserAuthenticationManager.isUserLoggedIn(this)) {
            String serializedUser = SharedPreferencesUtils.load(this, SHP_USER, null);
            if (serializedUser != null) {
                User user = new Gson().fromJson(serializedUser, User.class);
                if (user.getAddress() != null && user.getAddress().getName() != null) {
                    mPreviousUserAddress = user.getAddress();
                    ((TextView) findViewById(R.id.confirm_order_user_address)).setText(mPreviousUserAddress.getName());
                    findViewById(R.id.confirm_order_floor_apartment_input_container).setVisibility(View.GONE);
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

    private void startLoading() {
        findViewById(R.id.confirm_order_progress_bar).setVisibility(View.VISIBLE);
    }

    private void stopLoading() {
        findViewById(R.id.confirm_order_progress_bar).setVisibility(View.GONE);
    }

    private void setEstimatedTime() {
        TextView estimatedTimeView = (TextView) findViewById(R.id.confirm_order_estimated_time);

        Double storeDelayTime = mOrder.getStore().getDelayTime();
        if (storeDelayTime != null) {
            Integer avgDelayTime = DateUtils.secToRoundedMin(storeDelayTime);
            String estimatedTime = getResources().getString(R.string.fixed_minutes).replace(":min", Integer.toString(avgDelayTime));
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

    private void updateUserAddress() {
        EditText floorInput = (EditText) findViewById(R.id.confirm_order_floor_input);
        EditText apartmentInput = (EditText) findViewById(R.id.confirm_order_apartment_input);
        String floor = floorInput.getText() != null ? floorInput.getText().toString() : null;
        String apartment = apartmentInput.getText() != null ? apartmentInput.getText().toString() : null;

        String addressName = mUserPlacePicked.getAddress().toString();
        if (floor != null) {
            addressName += " " + floor;
        }
        if (apartment != null) {
            addressName += " " + apartment;
        }

        Address newUserAddress = new Address(
                addressName,
                mUserPlacePicked.getLatLng().latitude,
                mUserPlacePicked.getLatLng().longitude
        );
        if (mOrder.getAddress() == null || !newUserAddress.equals(mOrder.getAddress())) {
            mOrder.setAddress(newUserAddress);
            OrderService.setAsCurrentOrder(mOrder);

            String serializedUser = SharedPreferencesUtils.load(this, SHP_USER, null);
            if (serializedUser != null) {
                User user = new Gson().fromJson(serializedUser, User.class);
                user.setAddress(newUserAddress);
                SharedPreferencesUtils.update(this, SHP_USER, new Gson().toJson(user));
            }
        }
    }

    private String getNotes() {
        EditText notesInput = (EditText) findViewById(R.id.confirm_order_notes_input);
        if (notesInput.getText() != null) {
            return notesInput.getText().toString();
        }
        return null;
    }
}
