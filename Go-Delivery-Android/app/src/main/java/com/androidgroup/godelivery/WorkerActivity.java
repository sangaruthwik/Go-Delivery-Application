package com.androidgroup.godelivery;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class WorkerActivity extends Activity {

    ListView jobsListing;
    ArrayAdapter<String> stringArrayAdapter;
    JobsListAdapter myJobsListAdapter;

    List<String> jobIDsList = new ArrayList<String>();

    List<String> productNamesList = new ArrayList<String>();
    List<String> productWeightList = new ArrayList<String>();
    List<String> productSizeList = new ArrayList<String>();

    List<String> distanceList = new ArrayList<String>();
    List<String> rateList = new ArrayList<String>();

    List<String> pickUpPersonNamesList = new ArrayList<String>();
    List<String> pickUpPersonContactNumbersList = new ArrayList<String>();
    List<String> pickUpPersonAddressesList = new ArrayList<String>();
    List<String> pickUpPersonLatitudesList = new ArrayList<String>();
    List<String> pickUpPersonLongitudesList = new ArrayList<String>();



    List<String> recipientPersonNamesList = new ArrayList<String>();
    List<String> recipientPersonContactNumbersList = new ArrayList<String>();
    List<String> recipientPersonAddressesList = new ArrayList<String>();
    List<String> recipientPersonLatitudesList = new ArrayList<String>();
    List<String> recipientPersonLongitudesList = new ArrayList<String>();


    List<String> jobCreaterNameList = new ArrayList<String>();
    List<String> jobCreaterEmailList = new ArrayList<String>();
    List<String> jobCreaterContactNumberList = new ArrayList<String>();

    String RateString;
    String RatePerKilometerString = null;
    float RatePerKilometer;

    String loginEmailString = null;


    TextView title;
    Button refreshButton;
    Button HomeButton;

    String []dataValues = new String[18];
    int counter = 0;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.worker);

        LoadLoginEmail();

        Typeface font = Typeface.createFromAsset(getAssets(), "FancyFont_1.ttf");


        title = (TextView) findViewById(R.id.deliveryboyTitleID);
        refreshButton = (Button) findViewById(R.id.deliveryboyRefreshButtonID);
        HomeButton = (Button) findViewById(R.id.RefreshButtonID);

        HomeButton.setTypeface(font);
        HomeButton.setTextColor(Color.WHITE);

        title.setTypeface(font);
        title.setTextColor(Color.WHITE);

        refreshButton.setTypeface(font);
        refreshButton.setTextColor(Color.WHITE);



        dataValues[0] = "";
        dataValues[1] = "";
        dataValues[2] = "";
        dataValues[3] = "";
        dataValues[4] = "";
        dataValues[5] = "";
        dataValues[6] = "";
        dataValues[7] = "";
        dataValues[8] = "";
        dataValues[9] = "";
        dataValues[10] = "";
        dataValues[11] = "";
        dataValues[12] = "";
        dataValues[13] = "";
        dataValues[14] = "";
        dataValues[15] = "";
        dataValues[16] = "";
        dataValues[17] = "";


        new RetrievePerKilometerRate().execute("http://www.lushapps.com/AndroidApps/GoDelivery/RatePerKilometer.txt");


        jobsListing = (ListView) findViewById(R.id.jobsListingsID);
        jobsListing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(DeliveryBoyActivity.this, pickUpPersonLatitudesList.get(position), Toast.LENGTH_SHORT).show();


                // Log.d("popopo", pickUpPersonLatitudesList.get(0));

                Intent intent = new Intent(WorkerActivity.this, ProductActivity.class);


                intent.putExtra("jobID", jobIDsList.get(position));
                intent.putExtra("jobOwnerName" , jobCreaterNameList.get(position));
                intent.putExtra("jobOwnerEmail" , jobCreaterEmailList.get(position));
                intent.putExtra("jobCreaterContactNumberString", jobCreaterContactNumberList.get(position));

                intent.putExtra("ProductName", productNamesList.get(position));
                intent.putExtra("ProductWeight", productWeightList.get(position));
                intent.putExtra("ProductSize", productSizeList.get(position));

                intent.putExtra("distanceInKilometer",distanceList.get(position));
                intent.putExtra("rateInUSD",rateList.get(position));

                intent.putExtra("pickUpPersonName", pickUpPersonNamesList.get(position));
                intent.putExtra("pickUpContantNumber" , pickUpPersonContactNumbersList.get(position));
                intent.putExtra("pickUpAddress" , pickUpPersonAddressesList.get(position));
                intent.putExtra("pickUpLatitude",pickUpPersonLatitudesList.get(position));
                intent.putExtra("pickUpLongitude", pickUpPersonLongitudesList.get(position));

                intent.putExtra("deliveryPersonName", recipientPersonNamesList.get(position));
                intent.putExtra("deliveryPersonContantNumber" , recipientPersonContactNumbersList.get(position));
                intent.putExtra("deliveryAddress", recipientPersonAddressesList.get(position));
                intent.putExtra("deliveryLatitude",recipientPersonLatitudesList.get(position));
                intent.putExtra("deliveryLongitude",recipientPersonLongitudesList.get(position));


                startActivity(intent);
                finish();
            }
        });



        myJobsListAdapter = new JobsListAdapter(this, productNamesList, distanceList, rateList);

        jobsListing.setAdapter(myJobsListAdapter);



    }







    private class CheckIfUserAlreadyExists extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return CheckUser(urls[0]);
            } catch (IOException e) {
                return "NotFound";
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



            if (result.equals("OK")) {



                myJobsListAdapter.updateReceiptsList(productNamesList, distanceList, rateList);



            }

            else if (result.equals("NotFound"))
            {

                Toast.makeText(getApplicationContext(), "No Job Listing Found.", Toast.LENGTH_SHORT).show();
            }
            else
            {

                Toast.makeText(getApplicationContext(), "Network Connection Problem. Make sure that your internet is properly connected", Toast.LENGTH_SHORT).show();
            }



        }
    }

    private String CheckUser(String myurl) throws IOException, UnsupportedEncodingException {
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


                if(complexJobListingString.length() > 0) {

                    String[] details = new String[18];

                    for (int i = 0; i < details.length; ++i) {
                        details[i] = "";

                    }

                    int counter = 0;


                    for (int i = 0; i < complexJobListingString.length(); ++i) {

                        if (complexJobListingString.charAt(i) == '|') {
                            ++counter;
                            continue;
                        }


                        details[counter] = details[counter] + complexJobListingString.charAt(i);


                    }


                    float distanceFloat = 0.0F;

                    try {
                        distanceFloat = Float.valueOf(details[7].trim()).floatValue();

                    } catch (NumberFormatException nfe) {

                    }

                    float TotalCost = (distanceFloat * RatePerKilometer);


                    jobIDsList.add(details[0]);
                    jobCreaterNameList.add(details[1]);
                    jobCreaterEmailList.add(details[2]);
                    jobCreaterContactNumberList.add(details[3]);

                    productNamesList.add(details[4]);
                    productWeightList.add(details[5]);
                    productSizeList.add(details[6]);
                    distanceList.add(details[7]);
                    rateList.add(String.valueOf(TotalCost));

                    pickUpPersonNamesList.add(details[8]);
                    pickUpPersonContactNumbersList.add(details[9]);
                    pickUpPersonAddressesList.add(details[10]);
                    pickUpPersonLatitudesList.add(details[11]);
                    pickUpPersonLongitudesList.add(details[12]);



                    recipientPersonNamesList.add(details[13]);
                    recipientPersonContactNumbersList.add(details[14]);
                    recipientPersonAddressesList.add(details[15]);
                    recipientPersonLatitudesList.add(details[16]);
                    recipientPersonLongitudesList.add(details[17]);


                }


            }




            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                conn.disconnect();
                return "OK";

            }
            else
            {
                conn.disconnect();
                return "NetworkError";
            }

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {


            if (is != null)
            {
                is.close();


            }

        }
    }







    private class FetchAppointmentsListFromServer extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return FetchAppointmentsList(urls[0]);
            } catch (IOException e) {
                return "NotFound";
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            dataValues[0] = "";
            dataValues[1] = "";
            dataValues[2] = "";
            dataValues[3] = "";
            dataValues[4] = "";
            dataValues[5] = "";
            dataValues[6] = "";
            dataValues[7] = "";
            dataValues[8] = "";
            dataValues[9] = "";
            dataValues[10] = "";
            dataValues[11] = "";
            dataValues[12] = "";
            dataValues[13] = "";
            dataValues[14] = "";
            dataValues[15] = "";
            dataValues[16] = "";
            dataValues[17] = "";

            counter = 0;

        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {





            if (result.equals("OK")) {



                 //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();

            }

            else if (result.equals("NotFound"))
            {

                Toast.makeText(getApplicationContext(), "Not Found", Toast.LENGTH_SHORT).show();
            }
            else
            {

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }




        }
    }

    private String FetchAppointmentsList(String myurl) throws IOException, UnsupportedEncodingException {
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


            String comp = "naaa";

            while ((readlineText = textReader.readLine()) != null) {


                if (readlineText.length() > 0) {

                    comp = readlineText;

                    for (int i = 0; i < readlineText.length(); ++i) {
                        if (readlineText.charAt(i) == '|') {
                            ++counter;

                            continue;
                        }

                        dataValues[counter] = (dataValues[counter] + readlineText.charAt(i));
                    }


                    float distanceFloat = 0.0F;

                    try {
                        distanceFloat = Float.valueOf(dataValues[7].trim()).floatValue();

                    } catch (NumberFormatException nfe) {

                    }

                    float TotalCost = (distanceFloat * RatePerKilometer);


                    jobIDsList.add(dataValues[0]);
                    jobCreaterNameList.add(dataValues[1]);
                    jobCreaterEmailList.add(dataValues[2]);
                    jobCreaterContactNumberList.add(dataValues[3]);

                    productNamesList.add(dataValues[4]);
                    productWeightList.add(dataValues[5]);
                    productSizeList.add(dataValues[6]);
                    distanceList.add(dataValues[7]);
                    rateList.add(String.valueOf(TotalCost));

                    pickUpPersonNamesList.add(dataValues[8]);
                    pickUpPersonContactNumbersList.add(dataValues[9]);
                    pickUpPersonAddressesList.add(dataValues[10]);
                    pickUpPersonLatitudesList.add(dataValues[11]);
                    pickUpPersonLongitudesList.add(dataValues[12]);



                    recipientPersonNamesList.add(dataValues[13]);
                    recipientPersonContactNumbersList.add(dataValues[14]);
                    recipientPersonAddressesList.add(dataValues[15]);
                    recipientPersonLatitudesList.add(dataValues[16]);
                    recipientPersonLongitudesList.add(dataValues[17]);



                    counter = 0;
                    dataValues[0] = "";
                    dataValues[1] = "";
                    dataValues[2] = "";
                    dataValues[3] = "";
                    dataValues[4] = "";
                    dataValues[5] = "";
                    dataValues[6] = "";
                    dataValues[7] = "";
                    dataValues[8] = "";
                    dataValues[9] = "";
                    dataValues[10] = "";
                    dataValues[11] = "";
                    dataValues[12] = "";
                    dataValues[13] = "";
                    dataValues[14] = "";
                    dataValues[15] = "";
                    dataValues[16] = "";
                    dataValues[17] = "";


                }

            }




            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                conn.disconnect();
                return comp;

            }
            else
            {
                conn.disconnect();
                return "NetworkError";
            }



            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {


            if (is != null)
            {
                is.close();


            }

        }
    }




    private class RetrievePerKilometerRate extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return CheckRate(urls[0]);
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




            if (result.length() > 0)
            {


                RatePerKilometerString = result;


                try
                {
                    RatePerKilometer = Float.valueOf(RatePerKilometerString.trim()).floatValue();

                }
                catch (NumberFormatException nfe)
                {
                    //System.out.println("NumberFormatException: " + nfe.getMessage());
                }






                new CheckIfUserAlreadyExists().execute("http://www.lushapps.com/AndroidApps/GoDelivery/JobsListings/JobsListings.txt");

            }

            else
            {
                Toast.makeText(WorkerActivity.this , "Check Internet Connection" , Toast.LENGTH_SHORT).show();
            }




            //progressBar.setVisibility(View.GONE);











        }
    }

    private String CheckRate(String myurl) throws IOException, UnsupportedEncodingException {
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


            String readlineTextRate;



            while ((readlineTextRate = textReader.readLine()) != null) {


                RateString = readlineTextRate;






            }



            conn.disconnect();
            return RateString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {


            if (is != null)
            {
                is.close();



            }

        }
    }




    public void RefreshListingsClicked(View v)
    {



        jobIDsList.clear();

        jobCreaterContactNumberList.clear();
        jobCreaterEmailList.clear();
        jobCreaterNameList.clear();

        productSizeList.clear();
        productWeightList.clear();

        productNamesList.clear();
        distanceList.clear();
        rateList.clear();

        pickUpPersonNamesList.clear();
        pickUpPersonContactNumbersList.clear();
        pickUpPersonAddressesList.clear();
        pickUpPersonLatitudesList.clear();
        pickUpPersonLongitudesList.clear();



        recipientPersonNamesList.clear();
        recipientPersonContactNumbersList.clear();
        recipientPersonAddressesList.clear();
        recipientPersonLatitudesList.clear();
        recipientPersonLongitudesList.clear();


        //  new CheckIfUserAlreadyExists().execute("http://www.lushapps.com/androidApps/GoDelivery/JobsListings/JobsListings.txt");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(WorkerActivity.this, UserTypeActivity.class);
            startActivity(intent);
            finish();

            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }




    public void LoadLoginEmail()
    {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        loginEmailString = prefs.getString("GoDeliveryLoginEmail", null);

    }


    public void RefreshClicked(View v)
    {
        Intent intent = new Intent(WorkerActivity.this, AlreadyLoggedInActivity.class);

        startActivity(intent);

        finish();

    }


}
