package jolan.example.com.navigationdrawer;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;

import static jolan.example.com.navigationdrawer.R.id.map;

public class advertiserMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Marker myMarker;
    private Button next;
    private double latitude;
    private double longitude;

    public static String lat;
    public static String lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertiser_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( myMarker != null) {
                    lat = String.valueOf(latitude);
                    lng = String.valueOf(longitude);
                    startActivity(new Intent(getApplicationContext(), advertiserFinish.class));
                }else{
                    Toast.makeText(advertiserMap.this, "Pin location first and then select next", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(-20.285889,57.584607) , 10.0f));
        //Setting map to Mauritius

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override

            public void onMapLongClick(LatLng latLng) {
                if (myMarker == null) {
                    myMarker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("")
                            .snippet(""));
                } else {
                    myMarker.setPosition(latLng);
                }
                LatLng position = myMarker.getPosition();
                latitude = position.latitude;
                longitude = position.longitude;
            }
        });
    }


}
