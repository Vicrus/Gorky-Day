package app.team.gorky.gorky_day;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.LruCache;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mordy on 06.07.17.
 */

public class ImageLoad{

  /*  private static final String TAG = "myLog";

    public ArrayList<String> cacheImageName = new ArrayList<String>();

    public ArrayList<String> nameWebImages = new ArrayList<String>();
    public ArrayList<LatLng> pointImages = new ArrayList<LatLng>();
    private ArrayList<String> urlImages = new ArrayList<String>();
    private ArrayList<String> namesForDownload = new ArrayList<String>();
    private ArrayList<Bitmap> Images = new ArrayList<Bitmap>();
    private boolean run_ = true;
    private int count = 0;
    private LruCache<String, Bitmap> mMemoryCache;

    //private String TAG = "myLog";

    ImageLoad(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
        getImageInfo();
        loadThread();
    }
    public void loadImg(final ArrayList<Bitmap> img){

        new Thread(new Runnable() {
            public void run() {
                while (run_){
                    //поток
                }
                img.addAll(Images);
            }
        }).start();


    }
    private void checkCacheNames(){
        for(int i = 0; i<nameWebImages.size(); i++){
            if(cacheImageName.indexOf(nameWebImages.get(i)) < 0){
                namesForDownload.add(nameWebImages.get(i));
            }
        }

    }
    private void getImageInfo(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("images");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot imageSnapshot: dataSnapshot.getChildren()) {
                    Log.i(TAG, imageSnapshot.child("name").getValue(String.class));
                    nameWebImages.add(imageSnapshot.child("name").getValue(String.class));
                    DataSnapshot pointsSnapshot = imageSnapshot.child("point");
                    double latt = pointsSnapshot.child("lat").getValue(Double.class);
                    double logt = pointsSnapshot.child("log").getValue(Double.class);
                    pointImages.add(new LatLng(latt, logt ));
                    Log.i(TAG, Double.toString(latt) + " " + Double.toString(logt));
                    /*getNdrawImages(imageSnapshot.child("name").getValue(String.class),
                            new LatLng(latt, logt));//
                    //getUrlImages(imageSnapshot.child("name").getValue(String.class));
                }
                checkCacheNames();
                for(int i = 0; i<namesForDownload.size(); i++){
                    getUrlImages(namesForDownload.get(i));
                }

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void getUrlImages(final String name){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child("imagesNizhny/" + name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d(TAG, "add url:" + uri.toString());
                urlImages.add(uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

    }

    private void loadThread(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(run_){
                    // Log.d(TAG, "size imgUrl:" + Integer.toString(Images.size())+
                    // "url_size: "+Integer.toString(urlImages.size()));
                    if(Images.size()<urlImages.size()){
                        Images.add(getImageBmp(urlImages.get(count)));
                        Log.d(TAG, "Bitmap added");
                        count++;
                    }else if(Images.size()==urlImages.size() && urlImages.size()>0){
                        Log.d(TAG, "exit thread");
                        run_ = false;
                    }
                }
            }
        }).start();
    }

    private static Bitmap getImageBmp(String iUrl) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream buf_stream = null;
        try {
            Log.v(TAG, "Starting loading image by URL: " + iUrl);
            conn = (HttpURLConnection) new URL(iUrl).openConnection();
            conn.setDoInput(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();
            buf_stream = new BufferedInputStream(conn.getInputStream(), 8192);
            bitmap = BitmapFactory.decodeStream(buf_stream);
            buf_stream.close();
            conn.disconnect();
            buf_stream = null;
            conn = null;
        } catch (MalformedURLException ex) {
            Log.e(TAG, "Url parsing was failed: " + iUrl);
        } catch (IOException ex) {
            Log.d(TAG, iUrl + " does not exists");
        } catch (OutOfMemoryError e) {
            Log.w(TAG, "Out of memory!!!");
            return null;
        } finally {
            if ( buf_stream != null )
                try { buf_stream.close(); } catch (IOException ex) {}
            if ( conn != null )
                conn.disconnect();
        }
        return bitmap;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }*/
  class mMarker{
      String name;
      LatLng latLng;
  }

  private static final String TAG = "myLog";
  public ArrayList<mMarker> markers = new ArrayList<mMarker>();
  private ArrayList<String> nameWebImages = new ArrayList<String>();
  public  ArrayList<String> cacheNames    = new ArrayList<String>();
  private ArrayList<String> nameForLoad   = new ArrayList<String>();
  private LruCache<String, Bitmap> mMemoryCache;


  private boolean run_ = true;

  ImageLoad(){

      final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

      // Use 1/8th of the available memory for this memory cache.
      final int cacheSize = maxMemory / 8;

      mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
          @Override
          protected int sizeOf(String key, Bitmap bitmap) {
              // The cache size will be measured in kilobytes rather than
              // number of items.
              return bitmap.getByteCount() / 1024;
          }
      };
  }
  public void checkImages(){
      for(int i = 0; i< nameWebImages.size(); i++){
        //Bitmap bmp = getBitmapFromMemCache(nameWebImages.get(i));
          if(getBitmapFromMemCache(nameWebImages.get(i)) == null){
              Log.d(TAG, "added for for download " + nameWebImages.get(i));
              nameForLoad.add(nameWebImages.get(i));
          }
      }

      if(nameForLoad.size()>0){
          for(int i = 0; i<nameForLoad.size(); i++){
              Log.d(TAG, "download " + nameForLoad.get(i));
              downloadImage(nameForLoad.get(i));
          }
      }

  }

   public void getImageInfo() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("images");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot imageSnapshot : dataSnapshot.getChildren()) {
                    //Log.i(TAG, imageSnapshot.child("name").getValue(String.class));
                    nameWebImages.add(imageSnapshot.child("name").getValue(String.class));
                    mMarker nmarker = new mMarker();
                    nmarker.name = imageSnapshot.child("name").getValue(String.class);
                    DataSnapshot pointsSnapshot = imageSnapshot.child("point");
                    double latt = pointsSnapshot.child("lat").getValue(Double.class);
                    double logt = pointsSnapshot.child("log").getValue(Double.class);
                    nmarker.latLng = new LatLng(latt, logt);
                    markers.add(nmarker);
                    //Log.i(TAG, Double.toString(latt) + " " + Double.toString(logt));
                }
                checkImages();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read namesImage", error.toException());
            }
        });
    }

    private void downloadImage(final String name){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        storageRef.child("imagesNizhny/" + name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                Log.d(TAG, "add url:" + uri.toString());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bmp = getImageBmp(uri.toString());
                        Log.d(TAG, "getting " + name);
                        addBitmapToMemoryCache(name, bmp);

                    }
                }).start();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "download error");
            }
        });

    }


    private static Bitmap getImageBmp(String iUrl) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        BufferedInputStream buf_stream = null;
        try {
            Log.v(TAG, "Starting loading image by URL: " + iUrl);
            conn = (HttpURLConnection) new URL(iUrl).openConnection();
            conn.setDoInput(true);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();
            buf_stream = new BufferedInputStream(conn.getInputStream(), 8192);
            bitmap = BitmapFactory.decodeStream(buf_stream);
            buf_stream.close();
            conn.disconnect();
            buf_stream = null;
            conn = null;
        } catch (MalformedURLException ex) {
            Log.e(TAG, "Url parsing was failed: " + iUrl);
        } catch (IOException ex) {
            Log.d(TAG, iUrl + " does not exists");
        } catch (OutOfMemoryError e) {
            Log.w(TAG, "Out of memory!!!");
            return null;
        } finally {
            if ( buf_stream != null )
                try { buf_stream.close(); } catch (IOException ex) {}
            if ( conn != null )
                conn.disconnect();
        }
        return bitmap;
    }
    public LatLng getPoint(String key){
        for(int i = 0; i<markers.size(); i++){
            if(markers.get(i).name == key){
                return markers.get(i).latLng;
            }
        }
        return null;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
            cacheNames.add(key);
            Log.d(TAG, "caching " + key);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }



}
