package com.androidgroup.godelivery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;



import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;


public class WokerMapActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener, OnMapReadyCallback {

    String jobID = null;
    String jobFileName = null;

    String[] jobDetails = new String[22];



    AlertDialog alertDw;
    AlertDialog.Builder builder;




    private GoogleMap mMap;

    protected static final String TAG = "location-updates-sample";

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

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

    // UI Widgets.
    protected Button mStartUpdatesButton;
    protected Button mStopUpdatesButton;



    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    protected String mLastUpdateTime;



    LatLng myLatLng;

    ArrayList<LatLng> polylines = new ArrayList<LatLng>();


    boolean oneTime = true;


    int counter = -1;

    double[] latitudeArray = new double[5];
    double[] longitudeArray = new double[5];

    double averageLatitude = 0.0000000000;
    double averageLongitude = 0.00000000000;
    double averageDivider = 5.0000000000;



    List<LatLng> pontos = null;


    String origin = "34.1951962,72.0414057";
    String destination = "34.1978215,72.0472596";



    String pLatitude = null;
    String pLongitude = null;
    String dLatitude = null;
    String dLongitude = null;


    double pLa;
    double pLo;

    double dLa;
    double dLo;





    String LatitudeString = null;
    String LongitudeString = null;


    String LatitudeLongitudeString = "***";



    TextView jobStatusTitle;
    TextView jobStatus;

    Button jobDetailsButton;
    Button cancelButton;
    Button jobCompleteButton;

    Button LogOutButton;
    Button RefreshButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.job_accepter);


        Typeface font = Typeface.createFromAsset(getAssets(), "FancyFont_1.ttf");

        jobStatusTitle = (TextView) findViewById(R.id.jobAccepterActivityStatusTitleID);
        jobStatus = (TextView) findViewById(R.id.jobAccepterActivityStatusID);

        jobDetailsButton = (Button) findViewById(R.id.jobSeekerActivityJobDetailsButtonID);
        cancelButton = (Button) findViewById(R.id.jobSeekerActivityCancelButtonID);
        jobCompleteButton = (Button) findViewById(R.id.jobSeekerActivityCompleteJobButtonID);

        LogOutButton = (Button) findViewById(R.id.LogoutButtonID);

        LogOutButton.setTypeface(font);
        LogOutButton.setTextColor(Color.WHITE);

        RefreshButton = (Button) findViewById(R.id.RefreshButtonID);

        RefreshButton.setTypeface(font);
        RefreshButton.setTextColor(Color.WHITE);





        jobStatusTitle.setTypeface(font);
        jobStatusTitle.setTextColor(Color.WHITE);

        jobStatus.setTypeface(font);
        jobStatus.setTextColor(Color.WHITE);


        jobDetailsButton.setTypeface(font);
        jobDetailsButton.setTextColor(Color.WHITE);

        cancelButton.setTypeface(font);
        cancelButton.setTextColor(Color.WHITE);

        jobCompleteButton.setTypeface(font);
        jobCompleteButton.setTextColor(Color.WHITE);






        mStartUpdatesButton = (Button) findViewById(R.id.JobSeekerStartTrackingButtonID);
        mStopUpdatesButton = (Button) findViewById(R.id.JobSeekerStopTrackingButtonID);

        mStartUpdatesButton.setTypeface(font);
        mStartUpdatesButton.setTextColor(Color.WHITE);

        mStopUpdatesButton.setTypeface(font);
        mStopUpdatesButton.setTextColor(Color.WHITE);

        for (int i=0 ; i < 5 ; ++i)
        {
            latitudeArray[i] = 0.0000000000;
            longitudeArray[i] = 0.0000000000;

        }

        setUpMapIfNeeded();


        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        updateValuesFromBundle(savedInstanceState);

        buildGoogleApiClient();




        for (int i = 0; i < jobDetails.length; ++i) {
            jobDetails[i] = "";

        }





        Intent intent = getIntent();
        jobID = intent.getStringExtra("AcceptedJobIDNumber");

        jobFileName = (jobID + ".txt");


        new FetchAcceptedJobDetails().execute("http://www.lushapps.com/AndroidApps/GoDelivery/AcceptedJobs/" + jobFileName);









    }







    private class FetchAcceptedJobDetails extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return FetchJobDetails(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //progressBar.setVisibility(View.VISIBLE);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {



            jobDetailsButton.setVisibility(View.VISIBLE);


            pLatitude = jobDetails[15];
            pLongitude = jobDetails[16];

            dLatitude = jobDetails[20];
            dLongitude = jobDetails[21];


            try {
                pLa = Double.valueOf(pLatitude.trim()).doubleValue();

            } catch (NumberFormatException nfe) {

            }

            try {
                pLo = Double.valueOf(pLongitude.trim()).doubleValue();

            } catch (NumberFormatException nfe) {

            }

            try {
                dLa = Double.valueOf(dLatitude.trim()).doubleValue();

            } catch (NumberFormatException nfe) {

            }

            try {
                dLo = Double.valueOf(dLongitude.trim()).doubleValue();

            } catch (NumberFormatException nfe) {

            }


            new GetDirection().execute();


            //progressBar.setVisibility(View.GONE);

            //jobLongTitle.setText("Your have accepted the Job " + jobDetails[9] +", created By");
            // jobCreaterUsername.setText("Name\n" + jobDetails[1]);
            //jobCreaterContactNumber.setText("Contact Number\n" + jobDetails[5]);



        }
    }

    private String FetchJobDetails(String myurl) throws IOException, UnsupportedEncodingException {
        InputStream is = null;

        // Only display the first 500 characters of the retrieved
        // web page content.


        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setDefaultUseCaches(false);
            conn.addRequestProperty("Cache-Control", "no-cache");
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();



            is = conn.getInputStream();

            BufferedReader textReader = new BufferedReader(new InputStreamReader(is));


            String readlineTextListing;
            String complexJobListingString= null;




            while ((readlineTextListing = textReader.readLine()) != null) {

                complexJobListingString = readlineTextListing;


                if(complexJobListingString.length() > 25) {



                    int counter = 0;


                    for (int i = 0; i < complexJobListingString.length(); ++i) {

                        if (complexJobListingString.charAt(i) == '|') {
                            ++counter;
                            continue;
                        }


                        jobDetails[counter] = jobDetails[counter] + complexJobListingString.charAt(i);


                    }


                    break;

                }





            }








            //conn.disconnect();
            return conn.getResponseMessage();

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {


            if (is != null)
            {
                is.close();


            }

        }
    }








    public void ViewJobDetailsButtonClicked(View v)
    {


        Typeface font = Typeface.createFromAsset(getAssets(), "FancyFont_1.ttf");


        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.view_job_details, null);



        TextView jobTitleTextView = (TextView) dialoglayout.findViewById(R.id.GeneralJobTitleID);
        TextView distanceTextView = (TextView) dialoglayout.findViewById(R.id.GeneralDistanceID);
        TextView rateTextView = (TextView) dialoglayout.findViewById(R.id.GeneralRateID);
        TextView weightTextView = (TextView) dialoglayout.findViewById(R.id.GeneralWeightID);
        TextView dimensionsTextView = (TextView) dialoglayout.findViewById(R.id.GeneralDimensionID);
        TextView jobCreaterNameTextView = (TextView) dialoglayout.findViewById(R.id.GeneralJobOperaterNameID);
        TextView jobCreaterContactNoTextView = (TextView) dialoglayout.findViewById(R.id.GeneralJobOperatorNumberID);

        TextView pickUpAddressTextView = (TextView) dialoglayout.findViewById(R.id.GeneralPickUpPersonAddressID);
        TextView deliveryAddressTextView = (TextView) dialoglayout.findViewById(R.id.GeneralDeliveryPersonAddressID);

        TextView pickUpPersonNameTextView = (TextView) dialoglayout.findViewById(R.id.GeneralPickUpPersonNameID);
        TextView pickUpPersonContactNumberTextView = (TextView) dialoglayout.findViewById(R.id.GeneralPickUpPersonContactNumberID);

        TextView deliveryPersonNameTextView = (TextView) dialoglayout.findViewById(R.id.GeneralDeliveryPersonNameID);
        TextView deliveryPersonContactNumberTextView = (TextView) dialoglayout.findViewById(R.id.GeneralDeliveryPersonContactNumberID);


        jobTitleTextView.setTypeface(font);
        jobTitleTextView.setTextColor(Color.BLACK);

        distanceTextView.setTypeface(font);
        distanceTextView.setTextColor(Color.BLACK);

        rateTextView.setTypeface(font);
        rateTextView.setTextColor(Color.BLACK);

        weightTextView.setTypeface(font);
        weightTextView.setTextColor(Color.BLACK);

        dimensionsTextView.setTypeface(font);
        dimensionsTextView.setTextColor(Color.BLACK);

        jobCreaterNameTextView.setTypeface(font);
        jobCreaterNameTextView.setTextColor(Color.BLACK);

        jobCreaterContactNoTextView.setTypeface(font);
        jobCreaterContactNoTextView.setTextColor(Color.BLACK);


        pickUpAddressTextView.setTypeface(font);
        pickUpAddressTextView.setTextColor(Color.BLACK);

        deliveryAddressTextView.setTypeface(font);
        deliveryAddressTextView.setTextColor(Color.BLACK);



        pickUpPersonNameTextView.setTypeface(font);
        pickUpPersonNameTextView.setTextColor(Color.BLACK);


        pickUpPersonContactNumberTextView.setTypeface(font);
        pickUpPersonContactNumberTextView.setTextColor(Color.BLACK);


        deliveryPersonNameTextView.setTypeface(font);
        deliveryPersonNameTextView.setTextColor(Color.BLACK);


        deliveryPersonContactNumberTextView.setTypeface(font);
        deliveryPersonContactNumberTextView.setTextColor(Color.BLACK);


        Double distanceDoubleFormat = Double.valueOf(jobDetails[7]);
        double roundDistance = (double) Math.round(distanceDoubleFormat * 100.0) / 100.0;
        String roundedDistance = String.valueOf(roundDistance);

        Double amountDoubleFormat = Double.valueOf(jobDetails[8]);
        double roundAmount = (double) Math.round(amountDoubleFormat * 100.0) / 100.0;
        String roundedAmount = String.valueOf(roundAmount);





        jobTitleTextView.setText("Job Title\n" + jobDetails[9]);

        distanceTextView.setText("Distance\n" + roundedDistance + " KM");
        rateTextView.setText("Cost\n€" + roundedAmount);
        weightTextView.setText("Weight\n" + jobDetails[10] + " KG");
        dimensionsTextView.setText("Size/Dimension (LxWxH)\n" + jobDetails[11] + " inches");


        jobCreaterNameTextView.setText("Employer Name\n" + jobDetails[1]);
        jobCreaterContactNoTextView.setText("Employer Contact Number\n" + jobDetails[5]);


        pickUpAddressTextView.setText("Pick Up Address\n" + jobDetails[14]);
        deliveryAddressTextView.setText("Delivery Address\n" + jobDetails[19]);

        pickUpPersonNameTextView.setText("Pick Up Person Name\n" + jobDetails[12]);
        pickUpPersonContactNumberTextView.setText("Pick Up Person Contact Number\n" + jobDetails[13]);

        deliveryPersonNameTextView.setText("Delivery Person Name\n" + jobDetails[17]);
        deliveryPersonContactNumberTextView.setText("Delivery Person Contact Number\n" + jobDetails[18]);


        builder = new AlertDialog.Builder(this);

        builder.setView(dialoglayout);
        builder.setTitle(jobID);

        builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                alertDw.dismiss();
            }
        });


        alertDw = builder.create();


        alertDw.show();




    }






    private void updateValuesFromBundle(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
                setButtonsEnabledState();
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
    public void startUpdatesButtonHandler(View view) {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            setButtonsEnabledState();
            startLocationUpdates();
            // TimedLocationUpdates();
        }
    }

    /**
     * Handles the Stop Updates button, and requests removal of location updates. Does nothing if
     * updates were not previously requested.
     */
    public void stopUpdatesButtonHandler(View view) {
        if (mRequestingLocationUpdates) {
            mRequestingLocationUpdates = false;
            setButtonsEnabledState();
            stopLocationUpdates();
        }
    }

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
    private void setButtonsEnabledState() {
        if (mRequestingLocationUpdates) {
            mStartUpdatesButton.setEnabled(false);
            mStopUpdatesButton.setEnabled(true);
        } else {
            mStartUpdatesButton.setEnabled(true);
            mStopUpdatesButton.setEnabled(false);
        }
    }

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


        myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        setUpMap();

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
                    .findFragmentById(R.id.mapJobAccepter));
            mapFragment.getMapAsync(this);


            // mMap.setMyLocationEnabled(true);
        }


        // Check if we were successful in obtaining the map.
        if (mMap != null) {
            // setUpMap();
        }
    }




    private void setUpMap() {


        ++counter;

        latitudeArray[counter] = mCurrentLocation.getLatitude();
        longitudeArray[counter] = mCurrentLocation.getLongitude();


        if (counter == 4)
        {

            counter = -1;


            averageLatitude = ((latitudeArray[0] + latitudeArray[1] + latitudeArray[2] + latitudeArray[3] + latitudeArray[4])/averageDivider) ;
            averageLongitude = ((longitudeArray[0] + longitudeArray[1] + longitudeArray[2] + longitudeArray[3] + longitudeArray[4])/averageDivider) ;


            LatitudeString = String.valueOf(averageLatitude);
            LongitudeString = String.valueOf(averageLongitude);


            LatitudeLongitudeString =  (LatitudeString + "|" + LongitudeString);


            LatLng  polyline = new LatLng(averageLatitude,
                    averageLongitude);

            polylines.add(polyline);

            mMap.addPolyline(new PolylineOptions().addAll(polylines).width(16).color(Color.BLUE));

            new SendLocationUpdatesToServer().execute("http://www.lushapps.com/AndroidApps/GoDelivery/JobsLocationUpdates/LocationUpdates.php");







        }


    }



    public void TimedLocationUpdates()
    {
        new CountDownTimer(5000, 2500) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {


                new SendLocationUpdatesToServer().execute("http://www.lushapps.com/AndroidApps/GoDelivery/JobsLocationUpdates/LocationUpdates.php");
            }
        }.start();

    }





    private class SendLocationUpdatesToServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {


                return SendLocationUpdates(urls[0]);
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


            // TimedLocationUpdates();


            //Toast.makeText(getApplicationContext(),  "Location Sent", Toast.LENGTH_SHORT).show();
        }
    }

    private String SendLocationUpdates(String myurl) throws IOException, UnsupportedEncodingException {

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

            Uri.Builder builder = new Uri.Builder();




            builder.appendQueryParameter("Latitude", LatitudeString);
            builder.appendQueryParameter("Longitude", LongitudeString);
            builder.appendQueryParameter("JobFileName", jobFileName);




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




    public void SetUpMapPath()
    {

        if (pLatitude != null && pLongitude != null && dLatitude != null && dLongitude != null) {

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

                SetUpMapPath();
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




    public void CompleteJobButton(View v)
    {
        new SignUpFormSubmission().execute("http://www.lushapps.com/AndroidApps/GoDelivery/JobsStatus/AcceptedJobsStatus.php");
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


                // Toast.makeText(getApplicationContext(), "Complteted", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(WokerMapActivity.this, DeciderActivity.class);
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
                    .appendQueryParameter("JobStatus", "COMPLETED")
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



    public void LogOutClicked(View v)
    {
        LogoutUser();

        Intent intent = new Intent(WokerMapActivity.this, AlreadyLoggedInActivity.class);

        startActivity(intent);

        finish();

    }

    public void LogoutUser()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("GoDeliveryLoginEmail", null);
        editor.apply();
    }

    public void RefreshClicked(View v)
    {
        Intent intent = new Intent(WokerMapActivity.this, AlreadyLoggedInActivity.class);

        startActivity(intent);

        finish();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

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
            //  mMap.animateCamera(cam);
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


        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        //  PolylineOptions rectOptions = new PolylineOptions();//.width(5).color(Color.RED);
        //  Polyline polyline = mMap.addPolyline(rectOptions);


    }
}
