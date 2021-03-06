package com.example.antnio.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.content.*;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Address;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, SearchView.OnQueryTextListener,
        GoogleMap.OnCircleClickListener {

    private GoogleMap mMap;
    private Boolean inicio = true;
    private static final int MY_PERMISSION_LOCATION = 0;
    GoogleApiClient mGoogleApiClient = null;
    Location inicialLocation = null;
    Intent it;
    boolean marcar = false;
    LatLng latLngMarcar;
    SearchView barraProcurar;
    Circle circulo;
    Calendar hora;
    NumberFormat formato;
    Alerta alerta;
    static String tipo;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSION_LOCATION);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        barraProcurar = (SearchView) findViewById(R.id.barraProcurar);
        barraProcurar.setOnQueryTextListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        formato = new DecimalFormat("00");
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //está aqui porque implementou SearchView.OnQueryTextListener
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        List<Address> locaisList = null;
        if (query != null && !query.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                locaisList = geocoder.getFromLocationName(query, 1);
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

                return false;
            }

            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        }

        return false;
    }

    public void dados(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> address = null;
        try {
            address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cidade = "";
        String bairro = "";
        String estado = "";
        try{
            cidade = address.get(0).getLocality();
            bairro = address.get(0).getSubLocality();
            estado = address.get(0).getAdminArea();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        Log.e("ESTADO", " " + estado);
        Log.e("CIDADE", " " + cidade);
        Log.e("BAIRRO", " " + bairro);
        String lat = "" + latLng.latitude;
        Log.e("LATITUDE", lat);
        String slong = "" + latLng.longitude;
        Log.e("LONGITUDE", slong);

        hora = Calendar.getInstance();
        String horario = "" + hora.get(Calendar.YEAR) + "-" + formato.format((hora.get(Calendar.MONTH)+1))
                + "-" + formato.format(hora.get(Calendar.DAY_OF_MONTH)) + " "
                + formato.format(hora.get(Calendar.HOUR_OF_DAY)) + ":" + formato.format(hora.get(Calendar.MINUTE)) +
                ":" +formato.format(hora.get(Calendar.SECOND));
        Log.e("HORA: ", horario);

        alerta = new Alerta(estado, cidade, bairro, lat, slong, horario);

        Log.e("TIPO ALERTA: ", alerta.toString());
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 1) {
            dados(latLngMarcar);
            mMap.addMarker(new MarkerOptions().position(latLngMarcar).title("LUGAR BOM ").snippet("aw"+"\n"+"aa")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

                    LinearLayout info = new LinearLayout(context);
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(context);
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(context);
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }
            });

            circulo = mMap.addCircle(new CircleOptions()
                    .center(latLngMarcar)
                    .radius(300)
                    .strokeWidth(2)
                    .strokeColor(0xff4682B4)
                    .fillColor(0x504682B4)
                    .clickable(true));
        } else if (resultCode == 2) {
            dados(latLngMarcar);
            mMap.addMarker(new MarkerOptions().position(latLngMarcar).title("LUGAR RUIM").snippet(tipo));
        }
    }

    public void onCircleClick(Circle circle) {
        circulo.setFillColor(0x50008000);
        Log.e("XWXW", " " + "CLICK CIRCLE");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        it = new Intent(this, ActTelaParaMarcar.class);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                LatLng tt = mMap.getCameraPosition().target;
                Log.e("LATITUDE E LONG", tt.latitude + ", " + tt.longitude);
                Float zz = mMap.getCameraPosition().zoom;
                Log.e("NIVEL DE ZOOM", "" + zz);
            }});

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                marcar = true;
                latLngMarcar = point;
                it.putExtra("TESTE", "teste aqui"); // trecho apenas para um teste de passagem dos parametros
                startActivityForResult(it, 0);
            }
        }); //chama a outra janela

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            if (inicio) {
                onConnected(Bundle.EMPTY);
            }
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_LOCATION);
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // We are not connected anymore!
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // We tried to connect but failed!
    }

    public void onConnected(Bundle connectionHint) { // passa a posicao inicial
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && inicio) {
            inicialLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (inicialLocation != null) {
                LatLng lugar = new LatLng(inicialLocation.getLatitude(), inicialLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lugar, 14));

                Geocoder geocoder = new Geocoder(this);
                List<Address> address = null;
                try {
                    address = geocoder.getFromLocation(inicialLocation.getLatitude(), inicialLocation.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String cidade = "";
                String bairro = "";
                String estado = "";
                try{
                    cidade = address.get(0).getLocality();
                    bairro = address.get(0).getSubLocality();
                    estado = address.get(0).getAdminArea();
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

                //enviar para o banco os dados a seguir mais a lat e a long
                Log.e("CIDADE", " " + cidade);
                Log.e("BAIRRO", " " + bairro);
                Log.e("ESTADO", " " + estado);
                inicio = false;
            }
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
}
