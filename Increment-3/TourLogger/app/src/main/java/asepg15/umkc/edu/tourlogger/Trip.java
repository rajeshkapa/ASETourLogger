package asepg15.umkc.edu.tourlogger;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class Trip extends ActionBarActivity {

    ImageButton imgButton;
    JSONObject final_list = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        final String fromDate = getIntent().getStringExtra("StartDate");

        final String placeDetails = getIntent().getStringExtra("PlaceDetails");
        final String toDate = getIntent().getStringExtra("EndDate");
        try{
            final_list = new JSONObject(placeDetails);
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        addListenerOnButton();
    }

    private void addListenerOnButton() {

        imgButton = (ImageButton) findViewById
                (R.id.Map);

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lat = "";
                try{
                    lat = final_list.getString("lat");                }
                catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Toast.makeText(Trip.this, "Maps " + lat,
                        Toast.LENGTH_SHORT).show();
            }
        });

        imgButton = (ImageButton) findViewById
                (R.id.Attractions);

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getLocationInfo().execute("attractions");

                Toast.makeText(Trip.this, "Attractions ",
                        Toast.LENGTH_SHORT).show();
            }
        });

        imgButton = (ImageButton) findViewById
                (R.id.Activities);

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Trip.this, "Activities",
                        Toast.LENGTH_SHORT).show();
            }
        });

        imgButton = (ImageButton) findViewById
                (R.id.Weather);

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Trip.this, "Weather",
                        Toast.LENGTH_SHORT).show();
            }
        });

        imgButton = (ImageButton) findViewById
                (R.id.Carhire);

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Trip.this, "Carhire",
                        Toast.LENGTH_SHORT).show();
            }
        });

        imgButton = (ImageButton) findViewById
                (R.id.Hotels);

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Trip.this, "Hotels" ,
                        Toast.LENGTH_SHORT).show();
                new getLocationInfo().execute("hotels");


            }
        });
    }


    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/nearbysearch";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyA23jRoKzRI16Al7c88DpHxHDuW_OUtLjU";
    class getLocationInfo extends AsyncTask<String, Void, Void> {
        protected Void doInBackground(String... input) {
               try {
                if(input[0] == "hotels"){
                   getHotels();
               }
                else if(input[0] == "attractions"){
                    getAttractions();
                }
            }
                catch (IOException e) {
                    Log.e(LOG_TAG, "Cannot process JSON results", e);
                }
               catch (JSONException e) {
                   Log.e(LOG_TAG, "Cannot process JSON results", e);
               }
        return  null;
        }
    }
    private ArrayList<JSONObject> getHotels() throws IOException, JSONException {
        ArrayList<JSONObject> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&types=lodging");
            sb.append("&location=" +final_list.getString("lat") + "," + final_list.getString("lng"));
            sb.append("&radius=5000");

            System.out.println("Hello here"+sb.toString());
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            if (conn != null) {
                conn.disconnect();
            }
    }


        Intent i = new Intent(getApplicationContext(), HotelsView.class);
        i.putExtra("HotelDetails", jsonResults.toString());
        startActivity(i);
        return resultList;
    }



    private ArrayList<JSONObject> getAttractions() throws IOException, JSONException {
        ArrayList<JSONObject> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&types=amusement_park|art_gallery|church|mosque|movie_theater|hindu_temple|museum|stadium|zoo");
            sb.append("&location=" +final_list.getString("lat") + "," + final_list.getString("lng"));
            sb.append("&radius=5000");

            System.out.println("Hello here attractions"+sb.toString());

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            if (conn != null) {
                conn.disconnect();
            }
        }


        Intent i = new Intent(getApplicationContext(), AttractionsView.class);
        i.putExtra("Attractions", jsonResults.toString());
        startActivity(i);
        return resultList;
    }
}
