package jolan.example.com.navigationdrawer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;

import com.facebook.login.LoginResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;

import static java.security.AccessController.getContext;

//import static jolan.example.com.navigationdrawer.launch.categories;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    SupportMapFragment sMapFragment;

    private static GoogleMap mMap;
    private static DatabaseReference databaseReference;

    private Marker marker;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    final static List<String> categories = new ArrayList<>();
    static List<infoToStore> info = new ArrayList<>();

    public static final String NAME = "NAME";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String CATEGORY = "CATEGORY";
    public static final String IMAGE_URL = "IMAGE_URL";
    public static final String PUSH_ID = "PUSH_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sMapFragment = SupportMapFragment.newInstance();

        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        FacebookSdk.sdkInitialize(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);

        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        View headerLayout = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        ImageView profileView = (ImageView) headerLayout.findViewById(R.id.profile_image);
        TextView profileName = (TextView) headerLayout.findViewById(R.id.profile_name);

        profileName.setText(Profile.getCurrentProfile().getName());
        Picasso.with(MainActivity.this)
                .load(Profile.getCurrentProfile().getProfilePictureUri(200, 200))
                .into(profileView);

        sMapFragment.getMapAsync(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                }, 10);
                return;
            }
        }

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            LoginManager.getInstance().logOut();
            Intent logoutIntent = new Intent(MainActivity.this, launch.class);
            startActivity(logoutIntent);
            finish();

            return true;
        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //android.app.FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();

        if (sMapFragment.isAdded())
            sFm.beginTransaction().hide(sMapFragment).commit();

        if (id == R.id.nav_first_layout) {
            sFm.beginTransaction().replace(R.id.content_frame, new FirstFragment()).commit();
            if (!sMapFragment.isAdded())
                sFm.beginTransaction().add(R.id.map, sMapFragment).commit();
            else
                sFm.beginTransaction().show(sMapFragment).commit();
        } else if (id == R.id.nav_second_layout) {
            sFm.beginTransaction().replace(R.id.content_frame, new SecondFragment()).commit();

        } else if (id == R.id.about_us) {
            sFm.beginTransaction().replace(R.id.content_frame, new ThirdFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

/*
    public static void getLikedPageInfo(AccessToken accessToken) {

        GraphRequest data_request = GraphRequest.newMeRequest(
                accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json_object, GraphResponse response) {
                        try {
                            // convert Json object into Json array
                            JSONArray posts = json_object.getJSONObject("likes").optJSONArray("data");
                            //Log.e("data1",posts.toString());
                            for (int i = 0; i < posts.length(); i++) {
                                JSONObject post = posts.optJSONObject(i);
                                //String id = post.optString("id");
                                String category = post.optString("category");
                                String name = post.optString("name");
                                //int count = post.optInt("likes");

                                //if (!categories.contains(category)){
                                //  categories.add(category);
                                databaseReference.child("Advertiser").orderByChild("cat").equalTo(category).
                                        addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot advertiser : dataSnapshot.getChildren()) {
                                                    final String catString = advertiser.child("cat").getValue(String.class);
                                                    final String URLString = advertiser.child("downloadUrl").getValue(String.class);
                                                    final String keyString = advertiser.child("key").getValue(String.class);
                                                    final String lat = advertiser.child("latitude").getValue(String.class);
                                                    final String lng = advertiser.child("longitude").getValue(String.class);
                                                    final String descString = advertiser.child("serviceDesc").getValue(String.class);
                                                    final String nameString = advertiser.child("serviceName").getValue(String.class);
                                                    final String idString = advertiser.getKey();
                                                    info.add(new infoToStore(catString, URLString, keyString, lat, lng, descString, nameString, idString));
                                                    //Log.e("Sniper", URLString);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                //}
                                Log.e("name: ", name + " category: " + category);
                            }
                            //Log.e("category", categories.toString());
                        } catch (Exception e) {
                            Log.e("exception", e.toString());
                        }
                    }
                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "likes{id,category,name,location,likes}");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
    }
*/

    @Override
    protected void onResume(){
        super.onResume();
        final String[] afterString = {""};
        final Boolean[] noData = {false};

        Log.e("facebook id", Profile.getCurrentProfile().getId());
        do {
            Bundle params = new Bundle();
            params.putString("after", afterString[0]);
            params.putString("fields", "category,name");
            new GraphRequest(AccessToken.getCurrentAccessToken(), Profile.getCurrentProfile().getId() + "/likes", params,
                    HttpMethod.GET, new GraphRequest.Callback() {

                @Override
                public void onCompleted(GraphResponse response) {
                    JSONObject jsonObject = response.getJSONObject();

                    try {
                        Log.e("facebook data", response.toString());
                        JSONArray posts = jsonObject.getJSONArray("data");
                        for (int i = 0; i < posts.length(); i++) {
                            JSONObject post = posts.optJSONObject(i);
                            String category = post.optString("category");
                            String name = post.optString("name");

                            databaseReference.child("Advertiser").orderByChild("cat").equalTo(category).
                                    addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot advertiser : dataSnapshot.getChildren()) {
                                                final String catString = advertiser.child("cat").getValue(String.class);
                                                final String URLString = advertiser.child("downloadUrl").getValue(String.class);
                                                final String keyString = advertiser.child("key").getValue(String.class);
                                                final String lat = advertiser.child("latitude").getValue(String.class);
                                                final String lng = advertiser.child("longitude").getValue(String.class);
                                                final String descString = advertiser.child("serviceDesc").getValue(String.class);
                                                final String nameString = advertiser.child("serviceName").getValue(String.class);
                                                final String idString = advertiser.getKey();
                                                info.add(new infoToStore(catString, URLString, keyString, lat, lng, descString, nameString, idString));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                        }
                        if (!jsonObject.isNull("paging")){
                            JSONObject paging = jsonObject.getJSONObject("paging");
                            JSONObject cursors = paging.getJSONObject("cursors");
                            if (!cursors.isNull("after"))
                                afterString[0] = cursors.getString("after");
                            else
                                noData[0] = true;
                        }else
                            noData[0] = true;
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }).executeAndWait();
        }while (!noData[0] == true);
    }

    public static void getLikedPageInfo(final AccessToken accessToken) {
        final String[] afterString = {""};
        final Boolean[] noData = {false};

        Log.e("facebook id", Profile.getCurrentProfile().getId());
        do {
            Bundle params = new Bundle();
            params.putString("after", afterString[0]);
            params.putString("fields", "category,name");
            new GraphRequest(accessToken, Profile.getCurrentProfile().getId() + "/likes", params,
                    HttpMethod.GET, new GraphRequest.Callback() {

                @Override
                public void onCompleted(GraphResponse response) {
                    JSONObject jsonObject = response.getJSONObject();

                    try {
                        Log.e("facebook data", response.toString());
                        JSONArray posts = jsonObject.getJSONArray("data");
                        for (int i = 0; i < posts.length(); i++) {
                            JSONObject post = posts.optJSONObject(i);
                            String category = post.optString("category");
                            String name = post.optString("name");

                            databaseReference.child("Advertiser").orderByChild("cat").equalTo(category).
                                    addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot advertiser : dataSnapshot.getChildren()) {
                                                final String catString = advertiser.child("cat").getValue(String.class);
                                                final String URLString = advertiser.child("downloadUrl").getValue(String.class);
                                                final String keyString = advertiser.child("key").getValue(String.class);
                                                final String lat = advertiser.child("latitude").getValue(String.class);
                                                final String lng = advertiser.child("longitude").getValue(String.class);
                                                final String descString = advertiser.child("serviceDesc").getValue(String.class);
                                                final String nameString = advertiser.child("serviceName").getValue(String.class);
                                                final String idString = advertiser.getKey();
                                                info.add(new infoToStore(catString, URLString, keyString, lat, lng, descString, nameString, idString));
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });
                        }
                        if (!jsonObject.isNull("paging")){
                            JSONObject paging = jsonObject.getJSONObject("paging");
                            JSONObject cursors = paging.getJSONObject("cursors");
                            if (!cursors.isNull("after"))
                                afterString[0] = cursors.getString("after");
                            else
                                noData[0] = true;
                        }else
                            noData[0] = true;
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }).executeAndWait();
        }while (!noData[0] == true);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }




        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                                // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 40 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }



        for (final infoToStore details : info) {
            marker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(details.getLat()),
                            Double.parseDouble(details.getLng())))
            .title(details.getName())
            .snippet(details.getDesc()));

            marker.setTag(details);
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    infoToStore infoAttached = ((infoToStore) marker.getTag());
                    Intent intent = new Intent(MainActivity.this, InfoWindow.class);
                    intent.putExtra(NAME, infoAttached.getName());
                    intent.putExtra(DESCRIPTION, infoAttached.getDesc());
                    intent.putExtra(CATEGORY, infoAttached.getCat());
                    intent.putExtra(IMAGE_URL, infoAttached.getUrl());
                    intent.putExtra(PUSH_ID, infoAttached.getPushID());
                    startActivity(intent);
                }
            });
        }
    }


    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }
    }
}
