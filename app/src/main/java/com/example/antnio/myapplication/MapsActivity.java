package com.example.antnio.myapplication;

import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import  android.view.*;
import android.content.*;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Address;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private static final int MY_PERMISSION_LOCATION = 0;
    GoogleApiClient mGoogleApiClient = null;
    Location mLastLocation = null;
    Intent it;
    boolean marcar = false;
    LatLng latLngMarcar;
    EditText edtProcurar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSION_LOCATION);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void procurarLocal(View view) {
        edtProcurar = (EditText)findViewById(R.id.edtProcurar);
        String location = edtProcurar.getText().toString();
        List<Address> locaisList = null;
        if(location != null && !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                locaisList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Address address;
            try {
                address = locaisList.get(0);
            } catch (IndexOutOfBoundsException e) {
                Context context = getApplicationContext();
                CharSequence text = "Local digitado não existe!";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                InputMethodManager imm = (InputMethodManager) getSystemService( // serve para esconder o teclado
                        INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                return;
            }

            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));

            InputMethodManager imm = (InputMethodManager) getSystemService( // serve para esconder o teclado
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
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
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == 1) {
                mMap.addMarker(new MarkerOptions().position(latLngMarcar).title("LUGAR BOM")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            } else if (resultCode == 2) {
                mMap.addMarker(new MarkerOptions().position(latLngMarcar).title("LUGAR RUIM"));
            }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        it = new Intent(this, ActTelaParaMarcar.class);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener()
        { @Override public void onMapLongClick(LatLng point)
        {   marcar = true;
            latLngMarcar = point;
            it.putExtra("TESTE", "teste aqui"); // trecho apenas para um teste de passagem dos parametros
            startActivityForResult(it, 0); } }); //chama a outra janela

        /*Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            if (bundle.containsKey("MARCAR")) {
                boolean confirma = bundle.getBoolean("MARCAR");
                if (bundle.containsKey("Classificacao") && marcar && confirma) {
                    String tipo = bundle.getString("Classificacao");
                    mMap.addMarker(new MarkerOptions().position(latLngMarcar).title(tipo));
                    marcar = false;
                }
            }
        }*/

        /*mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener()
        { @Override public void onMapLongClick(LatLng point)
        { mMap.addMarker(new MarkerOptions().position(point).title("DANGER!!").draggable(true));} });*/

        /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        { @Override public void onMapClick(LatLng point)
        { mMap.addMarker(new MarkerOptions() .position(point).title("DANGER!!").draggable(true)); } });*/
        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            onConnected(Bundle.EMPTY);
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_LOCATION);
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // We are not connected anymore!
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // We tried to connect but failed!
    }

    public void onConnected(Bundle connectionHint) { // adiciona um marcador na posicao inicial
        /*if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
            if (mLastLocation != null) {
                LatLng lugar = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(lugar).title("Estou aqui!!"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(lugar));
            }
        }*/
    }
}
