package com.androidgroup.godelivery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProductActivity extends FragmentActivity implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener , OnMapReadyCallback {


    AlertDialog alertDw;
    AlertDialog.Builder builder;

    String pLat;
    String pLong;
    String dLat;
    String dLong;

    double pLa;
    double pLo;

    double dLa;
    double dLo;

    TextView pickUpAddressTextView;
    TextView deliveryAddressTextView;

    TextView distanceTextView;
    TextView rateTextView;

    String productName;
    String productWeight;
    String productSize;
    String rate;
    String distance;
    String pickUpAddress;
    String deliveryAddress;
    String jobID;
    String jobOwnerName;
    String jobOwnerEmail;
    String jobOwnerContantNumber;
    String pickUpPersonName;
    String pickUpPersonContactNumber;
    String pickUpLatitude;
    String pickUpLongitude;
    String deliveryPersonName;
    String deliveryPersonContactNumber;
    String deliveryLatitude;
    String deliveryLongitude;

    String JobSeekerEmailString = null;
    String JobSeekerNameString = null;
    String jobSeekerContactNumberString = null;


    String complexAcceptedJobString = null;
    String complexAcceptedJobDetailsString = null;

    String JobIDFileName = null;





    private GoogleMap mMap;


    protected static final String TAG = "location-updates-sample";

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;

    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;


    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;


    List<LatLng> pontos = null;


    String origin = "34.1951962,72.0414057";
    String destination = "34.1978215,72.0472596";


    Button acceptJobButton;
    Button RefreshButton;

    TextView pickUpAddressTitle;
    TextView deliveryAddressTitle;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.product);

        LoadLoginAllDetails();

        Typeface font = Typeface.createFromAsset(getAssets(), "FancyFont_1.ttf");

        distanceTextView = (TextView) findViewById(R.id.productDistanceID);
        rateTextView = (TextView) findViewById(R.id.productRateID);
        pickUpAddressTextView = (TextView) findViewById(R.id.productPickUpAddressID);
        deliveryAddressTextView = (TextView) findViewById(R.id.productDeliveryAddressID);

        pickUpAddressTitle = (TextView) findViewById(R.id.ProductPickUpAddressTitleID);
        deliveryAddressTitle = (TextView) findViewById(R.id.ProductDeliveryAddressTitleID);
        acceptJobButton = (Button) findViewById(R.id.ProductAcceptJobButtonID);

        RefreshButton = (Button) findViewById(R.id.RefreshButtonID);

        RefreshButton.setTypeface(font);
        RefreshButton.setTextColor(Color.WHITE);

        distanceTextView.setTypeface(font);
        distanceTextView.setTextColor(Color.WHITE);

        rateTextView.setTypeface(font);
        rateTextView.setTextColor(Color.WHITE);

        pickUpAddressTextView.setTypeface(font);
        pickUpAddressTextView.setTextColor(Color.CYAN);

        deliveryAddressTextView.setTypeface(font);
        deliveryAddressTextView.setTextColor(Color.CYAN);


        pickUpAddressTitle.setTypeface(font);
        pickUpAddressTitle.setTextColor(Color.YELLOW);


        deliveryAddressTitle.setTypeface(font);
        deliveryAddressTitle.setTextColor(Color.YELLOW);


        acceptJobButton.setTypeface(font);
        acceptJobButton.setTextColor(Color.WHITE);


        Intent intent = getIntent();



        jobID =  intent.getStringExtra("jobID");
        jobOwnerName = intent.getStringExtra("jobOwnerName");
        jobOwnerEmail = intent.getStringExtra("jobOwnerEmail");
        jobOwnerContantNumber = intent.getStringExtra("jobCreaterContactNumberString");
        productName = intent.getStringExtra("ProductName");

        productWeight = intent.getStringExtra("ProductWeight");
        productSize = intent.getStringExtra("ProductSize");
        distance =   intent.getStringExtra("distanceInKilometer");
        rate =  intent.getStringExtra("rateInUSD");

        pickUpPersonName = intent.getStringExtra("pickUpPersonName");
        pickUpPersonContactNumber = intent.getStringExtra("pickUpContantNumber");
        pickUpAddress =  intent.getStringExtra("pickUpAddress");
        pickUpLatitude =  intent.getStringExtra("pickUpLatitude");
        pickUpLongitude =  intent.getStringExtra("pickUpLongitude");

        deliveryPersonName = intent.getStringExtra("deliveryPersonName");
        deliveryPersonContactNumber = intent.getStringExtra("deliveryPersonContantNumber");
        deliveryAddress =  intent.getStringExtra("deliveryAddress");
        deliveryLatitude = intent.getStringExtra("deliveryLatitude");
        deliveryLongitude = intent.getStringExtra("deliveryLongitude");








        pLat = intent.getStringExtra("pickUpLatitude");
        pLong = intent.getStringExtra("pickUpLongitude");
        dLat = intent.getStringExtra("deliveryLatitude");
        dLong = intent.getStringExtra("deliveryLongitude");



        try {
            pLa = Double.valueOf(pLat.trim()).doubleValue();

        } catch (NumberFormatException nfe) {

        }

        try {
            pLo = Double.valueOf(pLong.trim()).doubleValue();

        } catch (NumberFormatException nfe) {

        }

        try {
            dLa = Double.valueOf(dLat.trim()).doubleValue();

        } catch (NumberFormatException nfe) {

        }

        try {
            dLo = Double.valueOf(dLong.trim()).doubleValue();

        } catch (NumberFormatException nfe) {

        }



        Double distanceDoubleFormat = Double.valueOf(distance);
        double roundDistance = (double) Math.round(distanceDoubleFormat * 100.0) / 100.0;
        String roundedDistance = String.valueOf(roundDistance);

        Double amountDoubleFormat = Double.valueOf(rate);
        double roundAmount = (double) Math.round(amountDoubleFormat * 100.0) / 100.0;
        String roundedAmount = String.valueOf(roundAmount);


        distanceTextView.setText("Distance: " + roundedDistance + " KM");
        rateTextView.setText("Earn: €" + roundedAmount);
        pickUpAddressTextView.setText(pickUpAddress);
        deliveryAddressTextView.setText(deliveryAddress);





        setUpMapIfNeeded();


        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        updateValuesFromBundle(savedInstanceState);

        buildGoogleApiClient();

        new GetDirection().execute();


    }


    private void updateValuesFromBundle(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);

            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }
            updateUI();
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Handles the Start Updates button and requests start of location updates. Does nothing if
     * updates have already been requested.
     */


    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    /**
     * Ensures that only one button is enabled at any time. The Start Updates button is enabled
     * if the user is not requesting location updates. The Stop Updates button is enabled if the
     * user is requesting location updates.
     */


    /**
     * Updates the latitude, the longitude, and the last location time in the UI.
     */
    private void updateUI() {
        if (mCurrentLocation != null) {

        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();


        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        setUpMapIfNeeded();

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();

        super.onStop();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        }

        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());



    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {

    }


    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.

            SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.mapProduct));
            mapFragment.getMapAsync(this);

            /*
            mMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);
            // create marker
            //  MarkerOptions startMarker = new MarkerOptions();
            // MarkerOptions marker = new MarkerOptions();

            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                LatLng mapInitialLocation = new LatLng(latitude, longitude);
                CameraPosition camPos = new CameraPosition.Builder()
                        .target(latLng).zoom(14).build();
                CameraUpdate cam = CameraUpdateFactory
                        .newCameraPosition(camPos);
                mMap.animateCamera(cam);
                //startMarker.position(latLng).title(
                // "This is where you started");

                // adding marker
                //mMap.addMarker(startMarker);

            }


            // check if map is created successfully or not
            if (mMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        // mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //  PolylineOptions rectOptions = new PolylineOptions();//.width(5).color(Color.RED);
        //  Polyline polyline = mMap.addPolyline(rectOptions);


        // Check if we were successful in obtaining the map.
        if (mMap != null) {
            // setUpMap();
        }


        */

        }
    }


    private void setUpMap() {



        if (pontos != null) {


            List<Marker> markers = new ArrayList<Marker>();

            Marker marker1 = null;
            Marker marker2 = null;


        /*
       Polyline line = googleMap.addPolyline(new PolylineOptions()
                .add(new LatLng(pLa, pLo), new LatLng(dLa, dLo))
                .width(10)
                .color(Color.BLUE)
                .geodesic(true));
                */



            marker1 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(pontos.get(0).latitude, pontos.get(0).longitude))
                    .title("Pick Up Point").icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup_icon)));


            marker2 = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(pontos.get(pontos.size() - 1).latitude, pontos.get(pontos.size() - 1).longitude))
                    .title("Delivery Point").icon(BitmapDescriptorFactory.fromResource(R.drawable.delivery_icon)));

            markers.add(marker1);
            markers.add(marker2);



            for (int i = 0; i < pontos.size() - 1; i++) {
                LatLng src = pontos.get(i);
                LatLng dest = pontos.get(i + 1);
                try {

                    //here is where it will draw the polyline in your map
                    Polyline line = mMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(src.latitude, src.longitude),
                                    new LatLng(dest.latitude, dest.longitude))
                            .width(6).color(Color.RED).geodesic(true));
                } catch (NullPointerException e) {
                    Log.e("Error", "NullPointerException onPostExecute: " + e.toString());
                } catch (Exception e2) {
                    Log.e("Error", "Exception onPostExecute: " + e2.toString());

                }

            }



// Set the camera to the greatest possible zoom level that includes the
// bounds




            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markers) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();



            // mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            // mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(asa, 0, 0, 0));
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));






        }


    }



    class GetDirection extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();



            origin = (String.valueOf(pLa) + "," + String.valueOf(pLo));
            destination = (String.valueOf(dLa) + "," + String.valueOf(dLo));

        }

        protected String doInBackground(String... args) {
            String stringUrl = "http://maps.googleapis.com/maps/api/directions/json?origin=" + origin+ "&destination=" + destination+ "&sensor=false";
            StringBuilder response = new StringBuilder();

            String status = "NetworkError";
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpconn = (HttpURLConnection) url
                        .openConnection();
                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(httpconn.getInputStream()),
                            8192);
                    String strLine = null;

                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }

                String jsonOutput = response.toString();


                JSONObject jsonObject = new JSONObject(jsonOutput);

                // routesArray contains ALL routes
                JSONArray routesArray = jsonObject.getJSONArray("routes");
                // Grab the first route
                JSONObject route = routesArray.getJSONObject(0);

                JSONObject poly = route.getJSONObject("overview_polyline");
                String polyline = poly.getString("points");
                pontos = decodePoly(polyline);

                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    status = "OK";

                }

                return status;


            } catch (Exception e) {

            }
            finally {
                return status;
            }



        }

        protected void onPostExecute(String file_url) {


            if (file_url.equals("OK")) {
                setUpMap();
            }


        }
    }

    private List<LatLng> decodePoly(String encoded) {


        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }


        return poly;
    }




    public void AcceptJobButtonClicked(View v)
    {
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.accept_job, null);


        builder = new AlertDialog.Builder(this);

        builder.setView(dialoglayout);
        builder.setTitle("Confirmation");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                if (jobSeekerContactNumberString.length() > 3) {

                    Log.e("Chec" , "1");

                    if (jobID != null && jobOwnerName != null && jobOwnerEmail != null && jobSeekerContactNumberString != null && jobOwnerContantNumber != null && jobSeekerContactNumberString != null && distance != null && rate != null && productName != null && productWeight != null && productSize != null && pickUpPersonName != null && pickUpPersonContactNumber != null && pickUpAddress != null && pickUpLatitude != null && pickUpLongitude != null      && deliveryPersonName != null && deliveryPersonContactNumber != null && deliveryAddress != null && deliveryLatitude != null && deliveryLongitude != null )
                    {

                        Log.e("Chec" , "1.1");

                        new RemoveJobListing().execute("http://www.lushapps.com/AndroidApps/GoDelivery/JobsListings/removeJobListing.php");
                    }


                }

                else

                {



                    Log.e("Chec" , "2");



                }

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                alertDw.dismiss();
            }
        });


        alertDw = builder.create();


        alertDw.show();





    }






    private class RemoveJobListing extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return RemoveJob(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            String separater = "|";

            complexAcceptedJobString = jobID + separater + jobOwnerEmail + separater + JobSeekerEmailString;
            complexAcceptedJobDetailsString = jobID + separater + jobOwnerName + separater + jobOwnerEmail + separater  + JobSeekerNameString + separater + JobSeekerEmailString + separater + jobOwnerContantNumber + separater + jobSeekerContactNumberString + separater + distance + separater + rate + separater + productName + separater + productWeight + separater + productSize + separater + pickUpPersonName + separater + pickUpPersonContactNumber + separater + pickUpAddress + separater + pickUpLatitude + separater + pickUpLongitude + separater + deliveryPersonName + separater + deliveryPersonContactNumber + separater + deliveryAddress + separater + deliveryLatitude + separater + deliveryLongitude;
            JobIDFileName = (jobID + ".txt");

            new RegisterAcceptedJob().execute("http://www.lushapps.com/AndroidApps/GoDelivery/AcceptedJobs/RegisterAcceptedJobs.php");


        }
    }

    private String RemoveJob(String myurl) throws IOException, UnsupportedEncodingException {

        OutputStream os = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // Starts the query
            conn.connect();


            os = conn.getOutputStream();

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("removeJobListing", jobID);


            String query = builder.build().getEncodedQuery();



            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();

            // Convert the InputStream into a string
            // String contentAsString = readIt(is, len);


            return conn.getResponseMessage();

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {

            if (os != null)
            {
                os.close();

            }

        }
    }





    private class RegisterAcceptedJob extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return RegisterAcceptedJob(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {


            if(result.equals("OK")) {
                Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_SHORT).show();

                new SignUpFormSubmission().execute("http://www.lushapps.com/AndroidApps/GoDelivery/JobsStatus/AcceptedJobsStatus.php");

            }

        }
    }

    private String RegisterAcceptedJob(String myurl) throws IOException, UnsupportedEncodingException {

        OutputStream os = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // Starts the query
            conn.connect();


            os = conn.getOutputStream();

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("AcceptedJob", complexAcceptedJobString)
                    .appendQueryParameter("AcceptedJobDetails", complexAcceptedJobDetailsString)
                    .appendQueryParameter("JobFileName", JobIDFileName);


            String query = builder.build().getEncodedQuery();



            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();

            // Convert the InputStream into a string
            // String contentAsString = readIt(is, len);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                return "OK";
            }
            else
            {
                return "NetworkError";
            }


            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {

            if (os != null)
            {
                os.close();

            }

        }
    }





    private class SignUpFormSubmission extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return SignUpForm(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();




        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {



            if (result.equals("OK")) {


                // Toast.makeText(getApplicationContext(), "Status Set", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ProductActivity.this, DeciderActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Network Problem", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String SignUpForm(String myurl) throws IOException, UnsupportedEncodingException {

        OutputStream os = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // Starts the query
            conn.connect();


            os = conn.getOutputStream();

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("JobStatus", "ACCEPTED")
                    .appendQueryParameter("JobFileName", jobID + "-Status.txt");



            String query = builder.build().getEncodedQuery();



            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();

            // Convert the InputStream into a string
            // String contentAsString = readIt(is, len);


            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                return "OK";
            }
            else
            {
                return "NetworkError";
            }

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {

            if (os != null)
            {
                os.close();

            }

        }
    }






    public void LoadLoginAllDetails()
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        JobSeekerEmailString = prefs.getString("GoDeliveryLoginEmail", null);
        jobSeekerContactNumberString= prefs.getString("GoDeliveryPhone", null);
        JobSeekerNameString = prefs.getString("GoDeliveryName", null);




    }




    public void RefreshClicked(View v)
    {
        Intent intent = new Intent(ProductActivity.this, AlreadyLoggedInActivity.class);

        startActivity(intent);

        finish();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
    }
}
