package com.lmiceli.habittracker;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.lmiceli.habittracker.model.DatabaseHelper;
import com.lmiceli.habittracker.model.Habit;
import com.lmiceli.habittracker.view.HabitViewAdapter;

import java.text.BreakIterator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * TODO check what stuff that remains here is called oncreate, onstart, etc
 */
public class MainActivity extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener {

    private static final String TAG = "MainActivity";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 13;

    @Bind(R.id.recycler_view)
    public RecyclerView recyclerView;

    private HabitViewAdapter viewAdapter;
    public static final int PLACE_PICKER_REQUEST = 1;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private BreakIterator mLatitudeText;
    private BreakIterator mLongitudeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
//        activateToolbar();

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewAdapter = new HabitViewAdapter(MainActivity.this);
        recyclerView.setAdapter(viewAdapter);
//        callOtherActivityTemp();


        // TODO this works, need to set it up back again later,
        // and then get location the user selected
//        startPlacePicker();

        // FIXME this not here please
//        createHabitWithLocation(dbHelper);
        createGoogleApiClient();

//        Log.d(TAG, "onCreate: ");
        
//        checkPermission();
    }

    private void createGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void askPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            // FIXME esto que significa?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Toast.makeText(MainActivity.this, "VENGA HOMBRE", Toast.LENGTH_SHORT).show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        }

    }

    /**
     * FIXME: delete this?
     *
     */
    private void showNotification() {

        Intent intent = new Intent(this, MapsActivity.class);
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // build notification
        // the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(this)
                .setContentTitle("ESTAS AQUI")
                .setContentText("Location Matched")
                .setSmallIcon(R.drawable.habit_tracker_icon)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.habit_tracker_icon, "Go to map", pIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);

    }

    @Override
    public void onConnected(Bundle connectionHint) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            askPermission();
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            /*mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));

            if (mLatitudeText.getText().toString() == "CUALCA"){
                // aca chequear que sea cerca
                showNotification();
            }*/

            Log.d(TAG, "onConnected: " + mLastLocation.toString());

        }

    }

//    private void checkPermission() {
//
//        int permissionCheck = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//        Log.d(TAG, "checkPermission: permission check " + permissionCheck);
//
//        if (permissionCheck != PackageManager.PERMISSION_GRANTED ) {
//            Log.d(TAG, "checkPermission: permission denied");
//        }
//        else {
//            Log.d(TAG, "checkPermission: permission granted");
//        }
//    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult() called with: " + "requestCode = [" + requestCode + "], permissions = [" + permissions + "], grantResults = [" + grantResults + "]");

        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // TODO: save to DB? creo que puede haver problemas si el usuario nos quita el permiso.
            // TODO puedo enterarme si me quitaron el permiso? o tengo que todo el tiempo chequear y listo?
            // TODO: ese chequeo es caro?
            Log.d(TAG, "onRequestPermissionsResult: permiso concedido");
        }
        else {
            Toast.makeText(MainActivity.this, "MALISIMO!", Toast.LENGTH_SHORT).show();
        }

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void createHabitWithLocation(DatabaseHelper dbHelper) {
        Habit habit = new Habit();
        habit.setDesc("Test habit with location");
        habit.setLat(37.3786526);
        habit.setLng(-5.9834105);

        dbHelper.addHabit(habit);
    }

    private void callOtherActivityTemp() {
        //        Intent mapsIntent = new Intent(this, MapsActivity.class);
////        mapsIntent.putExtra("key", value); //Optional parameters
//        this.startActivity(mapsIntent);
    }

    private void startPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
