package com.shopiholik.app.com.shopiholik.app;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.util.CollectionUtils;
import com.shopiholik.app.GetPlacesApi;
import com.shopiholik.app.R;
import com.shopiholik.app.com.shopiholik.app.search.OfferBrandImageApi;
import com.shopiholik.app.model.OfferData;
import com.shopiholik.app.model.OfferLogo;
import com.shopiholik.app.model.PlacesResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author agrawroh
 * @version v1.0
 */
public class DetailPageActivity extends AppCompatActivity {
    private static String location = "";
    private LocationManager locationManager;
    private Retrofit retrofit = null;
    private ImageView logoImage;
    private TextView textView;

    /* Base Map URI */
    private static final String BASE_URL = "https://maps.googleapis.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        /* Get References */
        textView = findViewById(R.id.textview);
        findViewById(R.id.imageView).setBackgroundColor(Color.rgb(27, 87, 95));

        /* Load Image */
        logoImage = findViewById(R.id.imageView2);
        if (null != getIntent().getExtras()) {
            Glide.with(getApplicationContext())
                    .load(getIntent().getExtras().get("logo"))
                    .apply(RequestOptions.circleCropTransform())
                    .into(logoImage);
        }

        /* Set Location Manager */
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        /* Set Retrofit Instance */
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        /* Check Whether Location Enabled */
        if (!isLocationEnabled()) {
            showAlert();
        }

        /* Request Permissions */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    9988);
        }

        /* Location Listener */
        LocationListener locationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }

    /**
     * Show Alert.
     */
    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    /**
     * Check Whether Location Enabled.
     *
     * @return Whether Location Enabled
     */
    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * Get Places.
     *
     * @param coordinates Coordinates Data.
     */
    private void getPlaces(final String coordinates) {
        GetPlacesApi placesApi = retrofit.create(GetPlacesApi.class);
        Call<PlacesResults> call = placesApi.getPlaces("textquery",
                "nike",
                "circle:2000@" + coordinates,
                "photos,formatted_address,name,rating,opening_hours,geometry",
                "AIzaSyBROP6DzeuiobMzC7X1LlsG50Qzi_WTyqs");
        call.enqueue(new Callback<PlacesResults>() {
            @Override
            public void onResponse(Call<PlacesResults> call, retrofit2.Response<PlacesResults> response) {
                String output = "";
                if (null != response.body()) {
                    Log.i("***", response.body().toString());
                    Log.i("***", String.valueOf(response.body().getCandidates().size()));
                    for (final PlacesResults.Candidate candidate : response.body().getCandidates()) {
                        output += candidate.getName() + "\n" + candidate.getFormattedAddress();
                        output += "\n\n";
                    }
                    textView.setText(output);
                }
            }

            @Override
            public void onFailure(Call<PlacesResults> call, Throwable throwable) {
                Log.e("PLACES_RETRIEVAL", throwable.toString());
            }
        });
    }

    /**
     * My Listener.
     */
    public class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            location = String.valueOf(loc.getLatitude()) + "," + String.valueOf(loc.getLongitude());
            getPlaces(location);
            Toast.makeText(getApplicationContext(), String.valueOf(loc.getLatitude()) + "," + String.valueOf(loc.getLongitude()), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
}
