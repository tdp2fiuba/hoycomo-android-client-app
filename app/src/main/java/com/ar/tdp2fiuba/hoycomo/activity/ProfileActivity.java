package com.ar.tdp2fiuba.hoycomo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.model.Address;
import com.ar.tdp2fiuba.hoycomo.model.User;
import com.ar.tdp2fiuba.hoycomo.service.GeocodingService;
import com.ar.tdp2fiuba.hoycomo.service.UserAuthenticationManager;
import com.ar.tdp2fiuba.hoycomo.utils.GeocodingUtils;
import com.ar.tdp2fiuba.hoycomo.utils.SharedPreferencesUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.ar.tdp2fiuba.hoycomo.utils.SharedPreferencesConstants.SHP_USER;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "UserProfile";

    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadUser();
        displayUserInfo();
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
                validateAndUpdateAddress(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, status.getStatusMessage());
            }
        }
    }

    private void loadUser() {
        mUser = UserAuthenticationManager.getUser(this);
    }

    private void displayUserInfo() {
        displayPicture();
        displayUsername();
        displayAddress();
    }

    private void validateAndUpdateAddress(final Place pickedPlace) {
        if (pickedPlace != null) {
            Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    ((TextView) findViewById(R.id.profile_address)).setText(pickedPlace.getName());
                    showAddressForm(pickedPlace);
                    stopLoading();
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.e(TAG, "Delivery address validation failed!");
                    stopLoading();
                    Toast.makeText(ProfileActivity.this, R.string.error_only_caba, Toast.LENGTH_LONG).show();
                }
            };
            startLoading();
            GeocodingService.validateAddress(pickedPlace.getAddress().toString(), successListener, errorListener);
        }
    }

    private void displayPicture() {
        CircleImageView userPicture = findViewById(R.id.profile_user_picture);
        Picasso.get().load(mUser.getAvatar()).into(userPicture);
    }

    private void displayUsername() {
        ((TextView) findViewById(R.id.profile_username)).setText(mUser.getFirstName() + " " + mUser.getLastName());
    }

    private void displayAddress() {
        setAddress();
        setEditAddressButtonListener();
    }

    private void setAddress() {
        if (mUser.getAddress() != null && !TextUtils.isEmpty(mUser.getAddress().getName())) {
            ((TextView) findViewById(R.id.profile_address)).setText(GeocodingUtils.extractLocalAddress(mUser.getAddress().getName()));
        }
    }

    private void setEditAddressButtonListener() {
        findViewById(R.id.profile_edit_address_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPlaceAutocomplete();
            }
        });
    }

    private void openPlaceAutocomplete() {
        AutocompleteFilter filter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .setCountry("AR")
                .build();

        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setFilter(filter)
                    .setBoundsBias(GeocodingUtils.CABABounds)
                    .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            Log.e(TAG, "There was an error on Place Autocomplete invocation");
        }
    }

    private void showAddressForm(final Place pickedPlace) {
        findViewById(R.id.profile_floor_apartment_input_container).setVisibility(View.VISIBLE);
        findViewById(R.id.profile_edit_address_button).setVisibility(View.GONE);

        findViewById(R.id.profile_confirm_changes_button).setVisibility(View.VISIBLE);
        findViewById(R.id.profile_confirm_changes_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserAddress(pickedPlace);

                findViewById(R.id.profile_floor_apartment_input_container).setVisibility(View.GONE);
                ((TextView) findViewById(R.id.profile_address)).setText(
                        GeocodingUtils.extractLocalAddress(mUser.getAddress().getName())
                );

                findViewById(R.id.profile_edit_address_button).setVisibility(View.VISIBLE);
                findViewById(R.id.profile_confirm_changes_button).setVisibility(View.GONE);

                Toast.makeText(ProfileActivity.this, R.string.your_address_was_updated, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUserAddress(final Place place) {
        Address newUserAddress = new Address(
                getNewAddressName(place),
                place.getLatLng().latitude,
                place.getLatLng().longitude
        );

        String serializedUser = SharedPreferencesUtils.load(this, SHP_USER, null);
        if (serializedUser != null) {
            mUser = new Gson().fromJson(serializedUser, User.class);
            mUser.setAddress(newUserAddress);
            SharedPreferencesUtils.update(this, SHP_USER, new Gson().toJson(mUser));
        }
    }

    private String getNewAddressName(final Place pickedPlace) {
        EditText floorInput = findViewById(R.id.profile_floor_input);
        EditText apartmentInput = findViewById(R.id.profile_apartment_input);
        String floor = floorInput.getText() != null ? floorInput.getText().toString() : null;
        String apartment = apartmentInput.getText() != null ? apartmentInput.getText().toString() : null;
        return GeocodingUtils.appendFloorAndApartment(pickedPlace.getAddress().toString(), floor, apartment);
    }

    private void startLoading() {
        findViewById(R.id.profile_progress_bar).setVisibility(View.VISIBLE);
        findViewById(R.id.profile_content).setVisibility(View.GONE);
    }

    private void stopLoading() {
        findViewById(R.id.profile_progress_bar).setVisibility(View.GONE);
        findViewById(R.id.profile_content).setVisibility(View.VISIBLE);
    }
}
