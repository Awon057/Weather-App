package com.example.weatherapp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.weatherapp.R;
import com.example.weatherapp.model.Data;
import com.example.weatherapp.model.Temp;
import com.example.weatherapp.sync.WeatherSync;
import com.example.weatherapp.util.ReturnData;
import com.example.weatherapp.util.SwipeListener;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    private double longitude;
    private double latitude;
    private boolean isLocationPermissionGranted;
    private LocationManager locationManager;
    private TextView temp;
    private ArrayList<String> temparature;
    private ArrayList<String> time;
    int pos = 0;
    private long size;
    private long minn = 999999999;
    private TextView back;
    private TextView forward;
    private CharSequence s;
    private TextView date;
    private LinearLayout swipeLayout;
    private ProgressDialog progress;

    @Override
    protected void onResume() {
        super.onResume();
        checkInternet();
        getLocation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        temp = (TextView) findViewById(R.id.temp);
        date = (TextView) findViewById(R.id.time);
        back = (TextView) findViewById(R.id.back);
        forward = (TextView) findViewById(R.id.forward);
        swipeLayout = (LinearLayout) findViewById(R.id.swipe_layout);

        swipeLayout.setOnTouchListener(new SwipeListener(MainActivity.this) {
            public void onSwipeRight() {
                if (pos > 0) {
                    pos--;
                    temp.setText(temparature.get(pos) + "");
                    setTime(pos);
                    setBackNxt(pos);
                } else
                    Toast.makeText(MainActivity.this, "No Previous data", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeLeft() {
                if (pos < size - 1) {
                    pos++;
                    temp.setText(temparature.get(pos) + "");
                    setTime(pos);
                    setBackNxt(pos);
                } else Toast.makeText(MainActivity.this, "No next data", Toast.LENGTH_SHORT).show();
            }
        });

        temparature = new ArrayList<>();
        time = new ArrayList<>();

        Date d = new Date();
        s = DateFormat.format("yyyy-MM-dd H:m:s", d.getTime());
    }

    private void checkInternet() {
        new Thread(new Runnable() {
            public void run() {
                // a potentially time consuming task
                try {
                    URL myUrl = new URL("http://google.com");
                    URLConnection connection = myUrl.openConnection();
                    connection.setConnectTimeout(3000);
                    connection.connect();
                } catch (Exception e) {
                    mHandler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            Toast.makeText(MainActivity.this,"No internet connection",Toast.LENGTH_LONG).show();
        }
    };

    private void getLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            } else {
                isLocationPermissionGranted = true;
            }
        } else {
            isLocationPermissionGranted = true;
        }
        if (isLocationPermissionGranted) {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                System.out.print("Network");
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 10, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        showProgress();
                        WeatherSync.getData(latitude, longitude, returnData);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                System.out.print("GPS");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        showProgress();
                        WeatherSync.getData(latitude, longitude, returnData);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
            } else {
                Intent enableGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(enableGPS);
            }
        }
    }

    ReturnData returnData = new ReturnData() {
        @Override
        public void returnData(Data data) {
            int i;
            size = data.getDataList().size();
            for (i = 0; i < data.getDataList().size(); i++) {
                time.add(data.getDataList().get(i).getTime());
                temparature.add((int)(data.getDataList().get(i).getTemp().getDayTemp()-273) + "\u2103");

                /*if (data.getDataList().get(i).getTime().equals(s)) {
                    pos = i;
                }*/
            }
            if (i == data.getDataList().size()) {
                setTime(pos);
                setBackNxt(pos);
                temp.setText(temparature.get(pos) + "");
            }
            dismissProgress();
        }
    };

    private void setTime(int position) {
        date.setText(time.get(position));
    }

    private void setBackNxt(int position) {
        if (position != 0)
            back.setText(temparature.get(pos - 1));
        else back.setText("");
        if (position != size - 1)
            forward.setText(temparature.get(position + 1));
        else forward.setText("");
    }

    private void showProgress(){
        progress = new ProgressDialog(this);
        progress.setTitle("Fetching Data");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }

    private void dismissProgress(){
        progress.dismiss();
    }
}
