package com.mechatitans.earthhacks;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static android.view.View.VISIBLE;

public class PaintActivity extends FragmentActivity implements OnMapReadyCallback {
    Context context;
    Button setUp, submit, locate, next, settings; ;
    GoogleMap map;
    String fruitType;
    MainActivity activity;
    boolean isLocating = false;
    static public EditText widthText;
    static TextView text;
    static public int width = 3;
    ConnectivityManager cnxManager;
    LocationManager locManager;
    ImageView mapImage;
    SupportMapFragment mapFragment;
    final int MY_PERMISSIONS_REQUEST_LOCATION = 25;
    LatLng center = new LatLng(0, 0);
    double[] gps = new double[2];

//    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    //  ConstraintLayout ll = (ConstraintLayout) inflater.inflate(R.layout.paint_layout, null);


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.paint_layout);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "montserrat_bold.ttf");
        Window w = getWindow() ;
        w.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setVisibility(View.GONE);
        setUp = (Button) findViewById(R.id.SetUp);
        settings = (Button) findViewById(R.id.settings);
        submit = (Button) findViewById(R.id.Submit);
        next = (Button) findViewById(R.id.next);
        locate = (Button) findViewById(R.id.locbtn);
        final TextView notice = (TextView) findViewById(R.id.notice);
        final TextView status = (TextView) findViewById(R.id.status);
        final ConstraintLayout notif = (ConstraintLayout)findViewById(R.id.notif);
        cnxManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapImage = (ImageView) findViewById(R.id.mapImage);
        next.setVisibility(View.GONE);
        locate.setVisibility(View.GONE);
        notice.setVisibility(VISIBLE);
        notif.setVisibility(VISIBLE);
        mapImage.setImageResource(R.drawable.map_default);
        status.setTypeface(face);
        notice.setTypeface(face);
       /* widthVal = 100;
        lenghtVal = 100;
        editWidth.setText("100");
        editLenght.setText("100");*/
        /*apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPanel.setVisibility(View.GONE);
                apply.setVisibility(View.GONE);
                textWidth.setVisibility(View.GONE);
                textHeight.setVisibility(View.GONE);
                textFruit.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
                editLenght.setVisibility(View.GONE);
                editWidth.setVisibility(View.GONE);
                scroll.setVisibility(View.GONE);
                if (editWidth.getText().equals("") && editLenght.getText().equals("")) {
                    widthVal = 30;
                    lenghtVal = 30;
                } else {
                    widthVal = Integer.valueOf((editWidth.getText().toString()));
                    if (widthVal >= 500) {
                        widthVal = (Integer.valueOf((editWidth.getText().toString())) / 2);
                        lenghtVal = (Integer.valueOf((editLenght.getText().toString())) / 2);
                    }
                    lenghtVal = Integer.valueOf((editLenght.getText().toString()));
                    if (lenghtVal >= 900) {
                        widthVal = (Integer.valueOf((editWidth.getText().toString())) / 2);
                        lenghtVal = (Integer.valueOf((editLenght.getText().toString())) / 2);
                    }
                    if (language == "français") {
                        Toast.makeText(getApplicationContext(), "Veuiller remplir les champs", Toast.LENGTH_LONG).show();
                    } else if (language == "english") {
                        Toast.makeText(getApplicationContext(), "Please fill in the blanks ", Toast.LENGTH_LONG).show();
                    } else if (language == "العربية") {
                        Toast.makeText(getApplicationContext(), " الرجاء تعمير الفراغات", Toast.LENGTH_LONG).show();
                    }
                }
                if (setaLad.getVisibility() == VISIBLE) {
                    setaLad.getLayoutParams().height = lenghtVal;
                    setaLad.getLayoutParams().width = widthVal;
                }

            }
        });*/
        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(PaintActivity.this,"This is unavailable",Toast.LENGTH_LONG).show();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //paintView.ClearPath();

                if (isLocating) {
                    CaptureMapScreen();
                    mapFragment.getView().setVisibility(View.GONE);
                    locate.setVisibility(View.GONE);
                    setUp.setVisibility(VISIBLE);
                    settings.setVisibility(VISIBLE);
                    submit.setVisibility(VISIBLE);
                    setUp.setBackgroundResource(R.drawable.mapbtn);
                    setUp.setVisibility(VISIBLE);
                    next.setVisibility(View.GONE);
                    isLocating = false;
                } else {
                    //next button
                    System.out.println("yoo");
                }
            }
        });
        setUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (IsConnected()) {
                    notice.setVisibility(View.GONE);
                    notif.setVisibility(View.GONE);
                    map.clear();
                    map.moveCamera(CameraUpdateFactory.newLatLng(center));
                    mapFragment.getView().setVisibility(VISIBLE);
                    locate.setVisibility(VISIBLE);
                    submit.setVisibility(View.GONE);
                    settings.setVisibility(View.GONE);
                    setUp.setVisibility(View.GONE);
                    next.setVisibility(VISIBLE);
                    locate.setVisibility(VISIBLE);
                    isLocating = true;
                    Toast.makeText(getApplicationContext(), "You Are Connected To The Internet", Toast.LENGTH_LONG).show();



                } else {
                    /*

                    System.out.println(widthVal);

                    setaLad.getLayoutParams().height = lenghtVal;
                    setaLad.getLayoutParams().width = widthVal;
                    mapFragment.getView().setVisibility(View.GONE);
                    setaLad.setVisibility(VISIBLE);*/
                }
            }
        });
        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (ContextCompat.checkSelfPermission(PaintActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PaintActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                    } else {
                        double[] loc = getLocation();
                        LatLng myLocation = new LatLng(loc[0], loc[1]);
                        map.clear();
                        map.addMarker(new MarkerOptions().position(myLocation).title("Your Location"));

                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(myLocation)
                                .zoom(18)
                                .bearing(0)
                                .build();
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                } else {
                    buildAlertMessageNoGps();
                }
            }
        });
    }

    public boolean IsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

/*
    static void showText() {
        text.setText(paintView.txt);
    }*/

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        double[] loc = getLocation();
        LatLng Hammmamet = new LatLng(gps[0], gps[1]);
        map.addMarker(new MarkerOptions().position(Hammmamet).title("Hammamet"));
        //map.moveCamera(CameraUpdateFactory.newLatLng(Hammmamet));
        //map.setMinZoomPreference(1);
        map.setMapType(map.MAP_TYPE_SATELLITE);
        map.setMyLocationEnabled(true);
    }

    private double[] getLocation() {
        List<String> providers = locManager.getProviders(true);
        Location l = null;
        for (int i = providers.size() - 1; i >= 0; i--) {
            try {
                l = locManager.getLastKnownLocation(providers.get(i));
                if (l != null) break;
            } catch (SecurityException e) {
            }
        }
        if (l != null) {
            gps[0] = l.getLatitude();
            gps[1] = l.getLongitude();
        }
        return gps;
    }

    public void CaptureMapScreen() {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap;

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                bitmap = snapshot;
                mapImage.setImageBitmap(bitmap);
            }
        };
        map.snapshot(callback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                /*if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }else {

                }*/
                return;
            }
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setTitle("Location Services")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });

        builder.setCancelable(false);

        final AlertDialog alert = builder.create();
        alert.show();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

