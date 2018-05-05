package com.ar.tdp2fiuba.hoycomo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ar.tdp2fiuba.hoycomo.R;
import com.ar.tdp2fiuba.hoycomo.model.Filter;
import com.google.gson.Gson;

public class FilterActivity extends AppCompatActivity {

    private Filter filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String filterJSON = getIntent().getExtras().getString("filter");
        filter = Filter.parseJSONFilter(filterJSON);

        setContentView(R.layout.activity_filter);

        if (filter != null) {
            EditText distance = findViewById(R.id.distance);
            if (distance != null) {
                distance.setText(Double.toString(filter.getDistance().getDistance()));
            }
        } else {
            filter = new Filter();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();
            }
        });
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
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.putExtra("filter", new Gson().toJson(filter));
                    startActivity(intent);
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

    private void fillFilter() {
        String distance = ((EditText)findViewById(R.id.distance)).getText().toString();
        filter.setDistanceFilter(10, 10, Double.parseDouble(distance));
    }

    private boolean validate() {
        String distance = ((EditText)findViewById(R.id.distance)).getText().toString();
        if (!distance.equals("")) {
            return true;
        }
        Toast.makeText(this, R.string.error_required_distance_filter, Toast.LENGTH_SHORT).show();
        return false;
    }
}
