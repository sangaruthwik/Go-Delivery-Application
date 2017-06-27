package com.androidgroup.godelivery;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

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
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class EmployerActivity extends Activity{

    int PLACE_PICKER_REQUEST = 1;

    AlertDialog alertDw;
    AlertDialog.Builder builder;

    TextView productName;

    TextView distanceBetweenLocations;
    TextView CostForDistance;

    TextView pickUpPersonName;
    TextView pickUpPersonAddress;
    TextView pickUpPersonContactNumber;
    TextView pickUpPersonLatitude;
    TextView pickUpPersonLongitude;

    TextView recipientName;
    TextView recipientAddress;
    TextView recipientContactNumber;
    TextView recipientLatitude;
    TextView recipientLongitude;


    TextView pickUpProfile;
    TextView recipientProfile;



    EditText productNameField;

    EditText productWeightField;

    EditText productSizeLengthField;
    EditText productSizeWidthField;
    EditText productSizeHeightField;

    EditText pickUpPersonNameField;
    EditText pickUpPersonContactNumberField;

    EditText recipientNameField;
    EditText recipientContactNumberField;


    Boolean PickUpLocationButtonClicked = false;

    Button createJobButton;
    Button enterJobDetailsButton;
    Button pickUpLocationButton;
    Button recipientLocationButton;

    Button RefreshButton;


    String JobIDString = null;

    String distanceString = null;



    String productNameString = null;

    String productWeightString = null;

    String productSizeString = null;


    String pickUpPersonNameString = null;
    String pickUpPersonAddressString = null;
    String pickUpPersonContactNumberString = null;
    String pickUpPersonLatitudeString = null;
    String pickUpPersonLongitudeString = null;

    String recipientNameString = null;
    String recipientAddressString = null;
    String recipientContactNumberString = null;
    String recipientLatitudeString = null;
    String recipientLongitudeString = null;

    String jobCreaterContactNumberString = null;
    String jobCreaterName = null;


    float RatePerKilometer = 0.0F;
    float DistanceInKilometer;

    String ComplexJobListingString = null;

    String loginEmailString = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.employer);

        Typeface font = Typeface.createFromAsset(getAssets(), "FancyFont_1.ttf");

        LoadUserAllDetails();


        pickUpProfile = (TextView) findViewById(R.id.ClientpickUpProfileLabelID);
        recipientProfile = (TextView) findViewById(R.id.ClientrecipientProfileLabelID);

        productName = (TextView) findViewById(R.id.productNameTextID);



        distanceBetweenLocations = (TextView) findViewById(R.id.distanceBetweenLocationID);
        CostForDistance = (TextView) findViewById(R.id.CostForDistanceID);

        pickUpPersonName = (TextView) findViewById(R.id.pickUpPersonNameTextID);
        pickUpPersonAddress = (TextView) findViewById(R.id.pickUpPersonAddressTextID);
        pickUpPersonContactNumber = (TextView) findViewById(R.id.pickUpPersonContactNumberTextID);
        pickUpPersonLatitude = (TextView) findViewById(R.id.pickUpPersonLatitudeTextID);
        pickUpPersonLongitude = (TextView) findViewById(R.id.pickUpPersonLongitudeTextID);

        recipientName = (TextView) findViewById(R.id.recipientPersonNameTextID);
        recipientAddress = (TextView) findViewById(R.id.recipientPersonAddressTextID);
        recipientContactNumber = (TextView) findViewById(R.id.recipientPersonContactNumberTextID);
        recipientLatitude = (TextView) findViewById(R.id.recipientPersonLatitudeTextID);
        recipientLongitude = (TextView) findViewById(R.id.recipientPersonLongitudeTextID);


        createJobButton = (Button) findViewById(R.id.CreateJobButtonID);
        enterJobDetailsButton = (Button) findViewById(R.id.EnterJobDetailsButtonID);
        pickUpLocationButton = (Button) findViewById(R.id.pickUpPersonLocationButtonID);
        recipientLocationButton = (Button) findViewById(R.id.recipientPersonLocationButtonID);

        RefreshButton = (Button) findViewById(R.id.RefreshButtonID);

        RefreshButton.setTypeface(font);
        RefreshButton.setTextColor(Color.WHITE);



        pickUpProfile.setTypeface(font);
        pickUpProfile.setTextColor(Color.YELLOW);

        recipientProfile.setTypeface(font);
        recipientProfile.setTextColor(Color.YELLOW);


        productName.setTypeface(font);
        productName.setTextColor(Color.WHITE);

        distanceBetweenLocations.setTypeface(font);
        distanceBetweenLocations.setTextColor(Color.WHITE);

        CostForDistance.setTypeface(font);
        CostForDistance.setTextColor(Color.WHITE);

        pickUpPersonName.setTypeface(font);
        pickUpPersonName.setTextColor(Color.GREEN);

        pickUpPersonAddress.setTypeface(font);
        pickUpPersonAddress.setTextColor(Color.GREEN);

        pickUpPersonContactNumber.setTypeface(font);
        pickUpPersonContactNumber.setTextColor(Color.GREEN);

        pickUpPersonLatitude.setTypeface(font);
        pickUpPersonLatitude.setTextColor(Color.GREEN);

        pickUpPersonLongitude.setTypeface(font);
        pickUpPersonLongitude.setTextColor(Color.GREEN);



        recipientName.setTypeface(font);
        recipientName.setTextColor(Color.CYAN);

        recipientContactNumber.setTypeface(font);
        recipientContactNumber.setTextColor(Color.CYAN);


        recipientAddress.setTypeface(font);
        recipientAddress.setTextColor(Color.CYAN);


        recipientLatitude.setTypeface(font);
        recipientLatitude.setTextColor(Color.CYAN);


        recipientLongitude.setTypeface(font);
        recipientLongitude.setTextColor(Color.CYAN);







        createJobButton.setTypeface(font);
        createJobButton.setTextColor(Color.WHITE);

        enterJobDetailsButton.setTypeface(font);
        enterJobDetailsButton.setTextColor(Color.WHITE);

        pickUpLocationButton.setTypeface(font);
        pickUpLocationButton.setTextColor(Color.WHITE);

        recipientLocationButton.setTypeface(font);
        recipientLocationButton.setTextColor(Color.WHITE);

        new CheckIFUsernameAlreadyExists().execute("http://www.lushapps.com/AndroidApps/GoDelivery/RatePerKilometer.txt");

    }

    public void EnterJobDetailsButtonClicked (View v)
    {


        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.job_details, null);

        productNameField = (EditText) dialoglayout.findViewById(R.id.productNameID);

        productWeightField = (EditText) dialoglayout.findViewById(R.id.PackageWeightID);

        productSizeLengthField = (EditText) dialoglayout.findViewById(R.id.PackageLengthID);
        productSizeWidthField = (EditText) dialoglayout.findViewById(R.id.PackageWidthID);
        productSizeHeightField = (EditText) dialoglayout.findViewById(R.id.PackageHeightID);

        pickUpPersonNameField = (EditText) dialoglayout.findViewById(R.id.pickUpPersonNameID);
        pickUpPersonContactNumberField = (EditText) dialoglayout.findViewById(R.id.pickUpPersonContactNumberID);

        recipientNameField = (EditText) dialoglayout.findViewById(R.id.recipientNameID);
        recipientContactNumberField = (EditText) dialoglayout.findViewById(R.id.recipientContactNumberID);



        builder = new AlertDialog.Builder(this);

        builder.setView(dialoglayout);
        builder.setTitle("Job Details");
        builder.setPositiveButton("Submit Job Details", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {





                productName.setText(productNameField.getText().toString());

                pickUpPersonName.setText("NAME\n" + pickUpPersonNameField.getText().toString() + "\n");
                pickUpPersonContactNumber.setText("CONTACT\n" + pickUpPersonContactNumberField.getText().toString() + "\n");

                recipientName.setText("NAME\n" + recipientNameField.getText().toString() + "\n");
                recipientContactNumber.setText("CONTACT\n" + recipientContactNumberField.getText().toString() + "\n");


                productNameString = productNameField.getText().toString();

                productWeightString = productWeightField.getText().toString();

                productSizeString = (productSizeLengthField.getText().toString() +  "x" + productSizeWidthField.getText().toString() + "x" + productSizeHeightField.getText().toString());

                pickUpPersonNameString = pickUpPersonNameField.getText().toString();
                pickUpPersonContactNumberString = pickUpPersonContactNumberField.getText().toString();

                recipientNameString = recipientNameField.getText().toString();
                recipientContactNumberString = recipientContactNumberField.getText().toString();




                if (productNameString != null && pickUpPersonNameString != null && pickUpPersonContactNumberString != null  && pickUpPersonAddressString != null && pickUpPersonLatitudeString != null && pickUpPersonLongitudeString != null        && recipientNameString != null && recipientContactNumberString != null && recipientAddressString != null && recipientLatitudeString != null && recipientLongitudeString != null    && jobCreaterContactNumberString != null && loginEmailString != null)
                {


                    JobIDString = GenerateRandomJobID();
                    distanceString = DistanceBetweenLocations();

                    DistanceInKilometer = Float.valueOf(distanceString);

                    float TotalCost = (DistanceInKilometer * RatePerKilometer);


                    Double distanceDoubleFormat = Double.valueOf(distanceString);
                    double roundDistance = (double) Math.round(distanceDoubleFormat * 100.0) / 100.0;
                    String roundedDistance = String.valueOf(roundDistance);

                    Double amountDoubleFormat = Double.valueOf(TotalCost);
                    double roundAmount = (double) Math.round(amountDoubleFormat * 100.0) / 100.0;
                    String roundedAmount = String.valueOf(roundAmount);


                    distanceBetweenLocations.setText("Distance" + "\n"+ roundedDistance + " KM");
                    CostForDistance.setText("Cost" + "\n" + "€"+ roundedAmount);

                }



            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Toast.makeText(ClientActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        });


        alertDw = builder.create();


        alertDw.show();







    }

    public void CreateJobButtonClicked(View v) {


        if (JobIDString != null && productNameString != null && productWeightString != null && productSizeString != null && distanceString != null && pickUpPersonNameString != null && pickUpPersonContactNumberString != null  && pickUpPersonAddressString != null && pickUpPersonLatitudeString != null && pickUpPersonLongitudeString != null        && recipientNameString != null && recipientContactNumberString != null && recipientAddressString != null && recipientLatitudeString != null && recipientLongitudeString != null   && jobCreaterContactNumberString != null && loginEmailString != null && jobCreaterName != null)
        {


            String stringIdentifier = "|";


            ComplexJobListingString = JobIDString + stringIdentifier + jobCreaterName + stringIdentifier + loginEmailString + stringIdentifier + jobCreaterContactNumberString + stringIdentifier + productNameString + stringIdentifier + productWeightString + stringIdentifier + productSizeString + stringIdentifier + distanceString + stringIdentifier + pickUpPersonNameString + stringIdentifier + pickUpPersonContactNumberString + stringIdentifier + pickUpPersonAddressString + stringIdentifier + pickUpPersonLatitudeString + stringIdentifier + pickUpPersonLongitudeString + stringIdentifier + recipientNameString + stringIdentifier + recipientContactNumberString + stringIdentifier + recipientAddressString + stringIdentifier + recipientLatitudeString + stringIdentifier + recipientLongitudeString;


            new SignUpFormSubmission().execute("http://www.lushapps.com/AndroidApps/GoDelivery/JobsListings/CreateJobListing.php");

            //Toast.makeText(ClientActivity.this, "Job Created Successfully" ,Toast.LENGTH_SHORT).show();





        }
        else
        {
            Toast.makeText(EmployerActivity.this, "Please fill all fields" ,Toast.LENGTH_SHORT).show();
        }



    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                Geocoder geocoder;

                List<Address> addresses = null;
                geocoder = new Geocoder(this, Locale.getDefault());

                try {


                    addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                String address = addresses.get(0).getAddressLine(0); //
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();


                if (PickUpLocationButtonClicked) {
                    pickUpPersonAddress.setText("ADDRESS\n" + address + "\n" + "CITY" + "\n" + city + "\n" + "STATE/PROVINCE" + "\n " + state + "\n" + "COUNTRY" + "\n" + country + "\n");
                    pickUpPersonLatitude.setText("LATITUDE\n" + String.valueOf(place.getLatLng().latitude) + "\n");
                    pickUpPersonLongitude.setText("LONGITUDE\n" + String.valueOf(place.getLatLng().longitude) + "\n");

                    pickUpPersonAddressString = address +  " City: " + city + " State-Province: " + state + " Country: " + country;
                    pickUpPersonLatitudeString = String.valueOf(place.getLatLng().latitude);
                    pickUpPersonLongitudeString = String.valueOf(place.getLatLng().longitude);
                }
                else
                {
                    recipientAddress.setText("ADDRESS\n" + address + "\n" + "CITY" + "\n" + city + "\n" + "STATE/PROVINCE" + "\n " + state + "\n" + "COUNTRY" + "\n" + country + "\n");
                    recipientLatitude.setText("LATITUDE\n" + String.valueOf(place.getLatLng().latitude) + "\n");
                    recipientLongitude.setText("LONGITUDE\n" + String.valueOf(place.getLatLng().longitude) + "\n");


                    recipientAddressString = address +  " City: " + city + " State-Province: " + state + " Country: " + country;
                    recipientLatitudeString = String.valueOf(place.getLatLng().latitude);
                    recipientLongitudeString = String.valueOf(place.getLatLng().longitude);

                }





                if (productNameString != null && pickUpPersonNameString != null && pickUpPersonContactNumberString != null  && pickUpPersonAddressString != null && pickUpPersonLatitudeString != null && pickUpPersonLongitudeString != null        && recipientNameString != null && recipientContactNumberString != null && recipientAddressString != null && recipientLatitudeString != null && recipientLongitudeString != null)
                {


                    JobIDString = GenerateRandomJobID();
                    distanceString = DistanceBetweenLocations();

                    DistanceInKilometer = Float.valueOf(distanceString);

                    float TotalCost = (DistanceInKilometer * RatePerKilometer);


                    Double distanceDoubleFormat = Double.valueOf(distanceString);
                    double roundDistance = (double) Math.round(distanceDoubleFormat * 100.0) / 100.0;
                    String roundedDistance = String.valueOf(roundDistance);

                    Double amountDoubleFormat = Double.valueOf(TotalCost);
                    double roundAmount = (double) Math.round(amountDoubleFormat * 100.0) / 100.0;
                    String roundedAmount = String.valueOf(roundAmount);


                    distanceBetweenLocations.setText("Distance" + "\n"+ roundedDistance + " KM");
                    CostForDistance.setText("Cost" + "\n" + "€"+ roundedAmount);



                }


            }
        }
    }


    public void pickUpLocationButtonClicked(View v)
    {
        PickUpLocationButtonClicked = true;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();


        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void recipientLocationButtonClicked(View v)
    {
        PickUpLocationButtonClicked = false;

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();


        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }




    public String GenerateRandomJobID()
    {
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String device_id = tm.getDeviceId();

        Bundle bb = new Bundle();



        Random random = new Random();
        final  int randomNumber = random.nextInt(10000);

        NumberFormat numberFormat = new DecimalFormat("00000");

        String randomPart = numberFormat.format(randomNumber);

        String JobID = "JobID-" + device_id + randomPart;


        return JobID;
    }




    public String DistanceBetweenLocations()
    {
        Double startingLatitude = Double.valueOf(pickUpPersonLatitudeString);
        Double startingLongitude = Double.valueOf(pickUpPersonLongitudeString);

        Double endingLatitude = Double.valueOf(recipientLatitudeString);
        Double endingLongitude = Double.valueOf(recipientLongitudeString);

        float[] results = new float[1];

        Location.distanceBetween(startingLatitude, startingLongitude, endingLatitude , endingLongitude, results);

        results[0] = (results[0]/1000F);

        String distance = String.valueOf(results[0]);

        return distance;
    }









    private class CheckIFUsernameAlreadyExists extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return CheckUsername(urls[0]);
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




            //progressBar.setVisibility(View.GONE);



            String RatePerKilometerString = result;

            RatePerKilometerString =  RatePerKilometerString + "F";


            RatePerKilometer = Float.valueOf(RatePerKilometerString);







        }
    }

    private String CheckUsername(String myurl) throws IOException, UnsupportedEncodingException {
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


            String readlineText;
            String complexJobListingString= null;



            while ((readlineText = textReader.readLine()) != null) {


                complexJobListingString = readlineText;






            }




            conn.disconnect();
            return complexJobListingString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {


            if (is != null)
            {
                is.close();


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




            Toast.makeText(EmployerActivity.this, "Job Created Successfully!", Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(EmployerActivity.this , UserTypeActivity.class);

            startActivity(intent);
            finish();


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
                    .appendQueryParameter("username", ComplexJobListingString);


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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(EmployerActivity.this, UserTypeActivity.class);

            startActivity(intent);
            finish();

            return false;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }



    public void LoadUserAllDetails()
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        loginEmailString = prefs.getString("GoDeliveryLoginEmail", null);
        jobCreaterContactNumberString = prefs.getString("GoDeliveryPhone", null);
        jobCreaterName  = prefs.getString("GoDeliveryName", null);


    }



    public void RefreshClicked(View v)
    {
        Intent intent = new Intent(EmployerActivity.this, AlreadyLoggedInActivity.class);

        startActivity(intent);

        finish();

    }








}
