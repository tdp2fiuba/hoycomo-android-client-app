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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.model.DistanceFilter;
import com.ar.tdp2fiuba.hoycomo.model.Filter;
import com.ar.tdp2fiuba.hoycomo.multiselect.Multiselect;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import java.util.List;

public class FilterActivity extends AppCompatActivity implements Multiselect.OnMultipleItemsSelectedListener{

    private Filter filter;
    private DistanceFilter distanceFilter;
    private FusedLocationProviderClient mFuseLocationProviderClient;

    private final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        setFilter();
        setLocationListener();
        setToolbarBackButton();

        // TODO: Reemplazar array por los food_types
        String[] array = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
        Multiselect multiselect = (Multiselect) findViewById(R.id.spinner);
        multiselect.setItems(array);
        multiselect.setSelection(new int[] {2,6});
        multiselect.setListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.apply_filter:
                if (validate()) {
                    fillFilter();
                    Intent intent = new Intent();
                    intent.putExtra("filter", new Gson().toJson(filter));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
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

    private void errorReturnToStoreList() {
        finish();
    }

    private void setFilter() {
        String filterJSON = getIntent().getExtras().getString("filter");
        filter = Filter.parseJSONFilter(filterJSON);

        if (filter != null) {
            distanceFilter = filter.getDistanceFilter();
            EditText distance = findViewById(R.id.distance);
            if (distance != null) {
                distance.setText(Double.toString(distanceFilter.getDistance()));
            }
        } else {
            filter = new Filter();
            distanceFilter = new DistanceFilter();
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

    private void fillFilter() {
        String distance = ((EditText)findViewById(R.id.distance)).getText().toString();
        distanceFilter.setDistance(Double.parseDouble(distance));
        filter.setDistanceFilter(distanceFilter);
    }

    private boolean validate() {
        String distance = ((EditText)findViewById(R.id.distance)).getText().toString();
        if (!distance.equals("")) {
            return true;
        }
        Toast.makeText(this, R.string.error_required_distance_filter, Toast.LENGTH_SHORT).show();
        return false;
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
