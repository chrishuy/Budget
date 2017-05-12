package edu.csulb.android.budget;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static edu.csulb.android.budget.R.id.map;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private EditText eAddress;
    private Intent resume;
    private GoogleMap mMap;
    private SharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        share = getSharedPreferences("AppPref", MODE_PRIVATE);
        String address;
        eAddress = (EditText) findViewById(R.id.etAddress);
        address = eAddress.toString();

        ((MapFragment) getFragmentManager().findFragmentById(map)).getMapAsync(this);
//        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
    }

    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.bAddress:
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                String address;
                address = eAddress.getText().toString();

                Geocoder gc = new Geocoder(this, Locale.US);
                List<Address> lstFoundAddresses = null;
                try {
                    lstFoundAddresses = gc.getFromLocationName(address, 5);
                }catch(IOException e)
                {
                    Toast.makeText(this, "No addresses found.", Toast.LENGTH_LONG).show();
                }

                //Extract coordinates from list
                double latitude = lstFoundAddresses.get(0).getLatitude();
                double longitude = lstFoundAddresses.get(0).getLongitude();

                //Sets coordinates of given address
                LatLng address_loc = new LatLng(latitude,longitude);

                //Moves camera to address on map and marks it
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(address_loc, 16);
                mMap.animateCamera(update);
                drawMarker(address_loc);

                //Saves longitudea and latitude into shared preferences
                SharedPreferences.Editor editor = share.edit();
                editor.putLong("latitude_pref", (long) latitude);
                editor.putLong("longitude_pref", (long) longitude);
                editor.commit();
        }
    }
    private void drawMarker(LatLng latLng){
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();
        // Setting latitude and longitude for the marker
        markerOptions.position(latLng);
        // Adding marker on the Google Map
        mMap.addMarker(markerOptions);
    }

}
