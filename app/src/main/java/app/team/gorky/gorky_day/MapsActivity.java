package app.team.gorky.gorky_day;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;

    private static final String ANONYMOUS = "anonimous";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    String mUsername;
    String mPhotoUrl;
    GoogleApiClient mGoogleApiClient;
     String TAG = "myLog";

    ArrayList<String> namesPoly = new ArrayList<String>();
    ArrayList<Polygon> polyList = new ArrayList<Polygon>();
    ArrayList<Bitmap> images = new ArrayList<Bitmap>();
    ImageLoad imgload;
    int prevSize = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }

            imgload = new ImageLoad();
            getNdrawPoly();
            imgload.getImageInfo();
            drawAttraction();

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void getNdrawPoly(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("polygones");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot polygoneSnapshot: dataSnapshot.getChildren()) {
                    //Log.i(TAG, polygoneSnapshot.child("name").getValue(String.class));
                    namesPoly.add(polygoneSnapshot.child("name").getValue(String.class));

                    PolygonOptions pOptions = new PolygonOptions();
                    String inp_color = polygoneSnapshot.child("color").getValue(String.class);
                    pOptions.fillColor(Color.parseColor(inp_color));
                    pOptions.strokeWidth(1);


                    DataSnapshot pointsSnapshot = polygoneSnapshot.child("points");

                    for(DataSnapshot coordinateSnapshot: pointsSnapshot.getChildren()){
                        DataSnapshot lat = coordinateSnapshot.child("lat");
                        DataSnapshot log = coordinateSnapshot.child("log");

                        double latt = lat.getValue(Double.class);
                        double logt = log.getValue(Double.class);
                        pOptions.add(new LatLng(latt, logt));
                        //Log.i(TAG, Double.toString(latt) + " " + Double.toString(logt));

                    }
                    // pOptions.fillColor(Color.BLUE);
                    Polygon polygon = mMap.addPolygon(pOptions);
                    polyList.add(polygon);
                }

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void drawAttraction(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        if(prevSize < imgload.cacheNames.size()) {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (int l = 0; l < imgload.cacheNames.size(); l++) {
                                            String key = imgload.cacheNames.get(l);
                                            Bitmap im = Bitmap.createScaledBitmap(imgload.getBitmapFromMemCache(key), 150, 150, false);
                                            mMap.addMarker(new MarkerOptions()
                                                    .position(imgload.pointImages.get(l))
                                                    .flat(true)
                                                    .icon(BitmapDescriptorFactory.fromBitmap(im)));
                                        }
                                        prevSize = imgload.cacheNames.size();
                                    }
                                });
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
    }
}
