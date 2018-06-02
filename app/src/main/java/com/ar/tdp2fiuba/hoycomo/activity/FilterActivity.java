package com.ar.tdp2fiuba.hoycomo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.model.DistanceFilter;
import com.ar.tdp2fiuba.hoycomo.model.Filter;
import com.ar.tdp2fiuba.hoycomo.model.FoodType;
import com.ar.tdp2fiuba.hoycomo.utils.view.MultiselectSpinner;
import com.ar.tdp2fiuba.hoycomo.service.FoodTypeService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity implements MultiselectSpinner.OnMultipleItemsSelectedListener{

    private Filter filter;
    private DistanceFilter distanceFilter;
    private FusedLocationProviderClient mFuseLocationProviderClient;
    private MultiselectSpinner multiselectSpinner;
    private FilterActivity self = this;
    private final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        setInitialState();
        setLocationListener();
        setToolbarBackButton();

        multiselectSpinner = findViewById(R.id.filters_food_type_spinner);
        getFoodTypes();

        setListeners();
    }

    private void getFoodTypes() {
        Response.Listener<JSONArray> successListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() > 0) {
                    List<String> foodTypes = new ArrayList<>();
                    final Gson gson = new GsonBuilder()
                            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                            .create();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            FoodType foodType = gson.fromJson(response.getJSONObject(i).toString(), FoodType.class);
                            foodTypes.add(foodType.getDescription());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    multiselectSpinner.setItems(foodTypes);
                    if (filter.getFoodTypes() != null) {
                        multiselectSpinner.setSelection(filter.getFoodTypes());
                    }
                    multiselectSpinner.setListener(self);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(self, R.string.error_load_foodtypes, Toast.LENGTH_SHORT).show();
            }
        };

        FoodTypeService.getAllFoodTypes(successListener, errorListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.apply_filter:
                setFilter();
                Intent intent = new Intent();
                intent.putExtra("filter", new Gson().toJson(filter));
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_LOCATION:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setLocationListener();
                } else {
                    Toast.makeText(this, R.string.error_required_permissions_distance_filter, Toast.LENGTH_SHORT).show();
                    errorReturnToStoreList();
                }
                break;
            default:
                break;
        }
    }

    private void setListeners() {
        setSwitchListeners();
        setDistanceSliderListener();
    }

    private void setSwitchListeners() {
        setSwitchListener(R.id.filters_distance_switch, R.id.filters_distance_input_container);
        setSwitchListener(R.id.filters_price_switch, R.id.filters_price_input_container);
        setSwitchListener(R.id.filters_wait_time_switch, R.id.filters_wait_time_input_container);
        setSwitchListener(R.id.filters_food_type_switch, R.id.filters_food_type_spinner);
        setSwitchListener(R.id.filters_rating_switch, R.id.filters_rating_bar);
    }

    private void setSwitchListener(int switchResId, final int inputResId) {
        ((SwitchCompat) findViewById(switchResId)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    findViewById(inputResId).setVisibility(View.VISIBLE);
                    findViewById(inputResId).requestFocus();
                } else {
                    findViewById(inputResId).setVisibility(View.GONE);
                }
            }
        });
    }

    private void setDistanceSliderListener() {
        SeekBar distanceSlider = findViewById(R.id.filters_distance_slider);
        distanceSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                updateDistanceLabel(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void updateDistanceLabel(Integer kms) {
        TextView distanceLabel = findViewById(R.id.filters_distance_label);
        distanceLabel.setText(
                getResources().getString(R.string.filter_distance_label).replace(":kms", String.valueOf(kms))
        );
    }

    private void errorReturnToStoreList() {
        finish();
    }

    private void setInitialState() {
        updateDistanceLabel(2);
        setFiltersInitialState();
    }

    private void setFiltersInitialState() {
        String filterJSON = getIntent().getExtras().getString("filter");
        filter = Filter.parseJSONFilter(filterJSON);
        if (filter != null) {
            setDistanceFilterInitialState();
        } else {
            filter = new Filter();
            distanceFilter = new DistanceFilter();
        }
    }

    private void setDistanceFilterInitialState() {
        distanceFilter = filter.getDistanceFilter();
        SwitchCompat distanceSwitch = findViewById(R.id.filters_distance_switch);
        if (distanceFilter != null) {
            distanceSwitch.setChecked(true);
            findViewById(R.id.filters_distance_input_container).setVisibility(View.VISIBLE);

            SeekBar distanceSlider = findViewById(R.id.filters_distance_slider);
            Integer kms = distanceFilter.getDistance().intValue();
            updateDistanceLabel(kms);
            distanceSlider.setProgress(kms);
        } else {
            distanceSwitch.setChecked(false);
            findViewById(R.id.filters_distance_input_container).setVisibility(View.GONE);
        }
    }

    private void setToolbarBackButton() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });
    }

    private void setLocationListener() {
        mFuseLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
            return;
        }
        mFuseLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    distanceFilter.setLat(location.getLatitude());
                    distanceFilter.setLon(location.getLongitude());
                }
            }
        });
        mFuseLocationProviderClient.getLastLocation().addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                errorReturnToStoreList();
            }
        });
    }

    private void setFilter() {
        setDistanceFilter();
        setFoodTypeFilter();
    }

    private void setDistanceFilter() {
        if (((SwitchCompat) findViewById(R.id.filters_distance_switch)).isChecked()) {
            Integer distance = ((SeekBar) findViewById(R.id.filters_distance_slider)).getProgress();
            distanceFilter.setDistance(distance.doubleValue());
            filter.setDistanceFilter(distanceFilter);
        }
    }

    private void setFoodTypeFilter() {
        if (((SwitchCompat) findViewById(R.id.filters_food_type_switch)).isChecked()) {
            List<String> foodTypes = multiselectSpinner.getSelectedStrings();
            if (foodTypes.size() > 0) {
                filter.setFoodTypes(foodTypes.toArray(new String[0]));
            }
        }
    }

    //TODO: Métodos que devuelven lo que está en el pop up de multiselect
    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {
        //Toast.makeText(this, strings.toString(), Toast.LENGTH_LONG).show();
    }
}
