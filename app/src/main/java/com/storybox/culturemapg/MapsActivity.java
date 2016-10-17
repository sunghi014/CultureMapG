package com.storybox.culturemapg;

import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        hideInfoBox();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng acc = new LatLng(35.147, 126.920);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(acc, 16));
        //mMap.addMarker(new MarkerOptions().position(acc).title("국립아시아문화전당"));
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.flag))
                .anchor(0.0f, 1.0f)
                .position(acc).title("국립아시아문화전당"));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //infoBox의 TextView를 id를 통해 찾아서, 텍스트값을 새로 셋팅함
                TextView textView = (TextView) findViewById(R.id.infoBoxText);
                textView.setText(marker.getTitle());
                //infoBox 나타냄
                showInfoBox();
                return false;
            }
        });

        /* 마커를 클릭하면 나타나는 infoWindow를 클릭하면 발생하는 이벤트
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String text = "[Info Window Click Event]" + " info TITLE : " + marker.getTitle().toString();
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG)
                        .show();
                showInfoBox();
            }
        });
        */

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //맵의 아무곳이나 클릭을하면 infoBox를 숨김
                hideInfoBox();
            }
        });

        final int REQUEST_CODE_PERMISSION = 2;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION);
            mMap.setMyLocationEnabled(true);
        }


        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String text = null;

        switch (item.getItemId()){
            /*case R.id.action_search:
                text = "click the search button";
                break;*/
            case R.id.action_settings:
                text = "click the setting button";
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        return true;
    }

    private void hideInfoBox()
    {
        View infoBox = findViewById(R.id.infoBox);
        if(infoBox.getVisibility() == View.VISIBLE)
        {
            infoBox.setVisibility(View.GONE);
        }
    }

    private void showInfoBox()
    {
        View infoBox = findViewById(R.id.infoBox);

        if(infoBox.getVisibility() == View.GONE)
        {
            infoBox.setVisibility(View.VISIBLE);
        }
    }

    public void callCultureSpaceInfo(View view)
    {
        Toast.makeText(this, "fragment clicked", Toast.LENGTH_LONG).show();
    }
}
