package com.mechatitans.earthhacks;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.UUID;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    Button connect ;
    boolean connected = false;
    String line;
    static String language= "English";
    FileOutputStream outputStream;

    private static final int ACTIVATION_REQUEST = 1;
    private static final int CONNECTION_REQUEST = 2;

    BluetoothAdapter BTAdapter = null;
    BluetoothDevice BTDevice = null;
    BluetoothSocket BTSocket = null;
    LocationManager locManager;

    private static String NAME = null;
    private static String MAC = null;

    static UUID MyUuid = null;
    TextView welcome;
    TextView des;
    boolean b;




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        welcome = findViewById(R.id.welcome);
        des = findViewById(R.id.des);
        //BTAdapter = BluetoothAdapter.getDefaultAdapter();

        connect = (Button)findViewById(R.id.connect);
        //Drawable d = getResources().getDrawable(R.drawable.applybtn);
        //connect.setBackground(d);
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (BTAdapter == null) {
            Toast.makeText(getApplicationContext(), "This Device Doesn't Have BlueTooth", Toast.LENGTH_LONG).show();
        }else {
            if (!BTAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, ACTIVATION_REQUEST);
            }
        }
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            buildAlertMessageNoGps();
        }
        connect.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
               /*if (connected){
                    try{
                        BTSocket.close();
                        connected = false;
                        if(language.equals("français")) {
                            Toast.makeText(getApplicationContext(), "Déconnecté", Toast.LENGTH_LONG).show();
                        }else if (language.equals("العربية")){
                            Toast.makeText(getApplicationContext(), "قطع الإتصال", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_LONG).show();
                        }
                    }catch (IOException error){
                        Toast.makeText(getApplicationContext(), "ERROR" + error, Toast.LENGTH_LONG).show();
                    }
                }else{


*/              requestLocationPermission();
                if(b) {
                    Intent myIntent = new Intent(v.getContext(), PaintActivity.class);
                    startActivity(myIntent);
                }else requestLocationPermission();
            }
    });
    }
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @Override
    protected void onActivityResult(int requestCode, int resultCode , Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ACTIVATION_REQUEST:
                if (resultCode == Activity.RESULT_OK) {

                    Toast.makeText(getApplicationContext(), "BlueTooth Has Been Activated", Toast.LENGTH_LONG).show();

                } else if (resultCode == Activity.RESULT_CANCELED) {

                    Toast.makeText(getApplicationContext(), "BlueTooth Hasn't Been Activated", Toast.LENGTH_LONG).show();

                }
                break;
            case CONNECTION_REQUEST:
                if (resultCode == Activity.RESULT_OK) {
                    MAC = deviceList.MacAddress;
                    BTDevice = BTAdapter.getRemoteDevice(MAC);
                    NAME = BTDevice.getName();
                    System.out.println("Name " + NAME);
                    MyUuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                    try {
                        BTSocket = BTDevice.createRfcommSocketToServiceRecord(MyUuid);
                    } catch (Exception e1) {
                        Toast.makeText(getApplicationContext(), "Failed To Create Socket", Toast.LENGTH_LONG).show();
                    }
                    try {
                        BTSocket.connect();
                        connect.setText("Disconnect From GreenBot");
                        Toast.makeText(getApplicationContext(), "Connected To : " + NAME, Toast.LENGTH_LONG).show();

                    } catch (IOException e2) {
                        try {
                            Toast.makeText(getApplicationContext(), "Trying Fallback...", Toast.LENGTH_LONG).show();
                            BTSocket = (BluetoothSocket) BTDevice.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class}).invoke(BTDevice, MyUuid);
                            connected = true;
                            BTSocket.connect();
                            connect.setText("Disconnect From GreenBot");

                            Toast.makeText(getApplicationContext(), "Connected To : " + NAME, Toast.LENGTH_LONG).show();

                        } catch (Exception e3) {
                            MyUuid = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
                            try {
                                BTSocket = BTDevice.createRfcommSocketToServiceRecord(MyUuid);
                            } catch (Exception e4) {
                                Toast.makeText(getApplicationContext(), "Failed To Create Socket", Toast.LENGTH_LONG).show();
                            }
                            try {
                                BTSocket.connect();
                                connected = true;
                                connect.setText("Disconnect From GreenBot");

                                Toast.makeText(getApplicationContext(), "Connected To : " + NAME, Toast.LENGTH_LONG).show();

                            } catch (Exception e5) {
                                Toast.makeText(getApplicationContext(), "Trying Fallback...", Toast.LENGTH_LONG).show();
                                try {
                                    BTSocket = (BluetoothSocket) BTDevice.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class}).invoke(BTDevice, MyUuid);
                                    connected = true;
                                    BTSocket.connect();
                                    connect.setText("Disconnect From GreenBot");
                                    Toast.makeText(getApplicationContext(), "Connected To : " + NAME, Toast.LENGTH_LONG).show();

                                } catch (Exception e6) {
                                    try {
                                        BTSocket = (BluetoothSocket) BTDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(BTDevice, 1);
                                        connected = true;
                                        BTSocket.connect();
                                        connect.setText("Disconnect From GreenBot");
                                        Toast.makeText(getApplicationContext(), "Connected To : " + NAME, Toast.LENGTH_LONG).show();

                                    } catch (Exception e7) {
                                        Toast.makeText(getApplicationContext(), "Failed to Connect To : " + NAME, Toast.LENGTH_LONG).show();

                                    }
                                }
                            }
                        }
                    }
                    /*}else{
                        text2.setText("Not Compatible");
                    }*/
                } else {
                    Toast.makeText(getApplicationContext(), "Failed To Get The MAC Address", Toast.LENGTH_LONG).show();
                }

        }
        if (connected) {
            Intent myIntent = new Intent(this, PaintActivity.class);
            startActivity(myIntent);
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
    private final int REQUEST_LOCATION_PERMISSION = 1;
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
            b = false;
        }else b = true;
    }

}