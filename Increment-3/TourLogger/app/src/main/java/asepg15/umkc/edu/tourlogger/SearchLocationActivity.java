package asepg15.umkc.edu.tourlogger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class SearchLocationActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {


    String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.editText);
        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
        autoCompView.setOnItemClickListener(this);

        Button list = (Button) findViewById(R.id.venues);

        list.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(), NewTripActivity.class);
                AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.editText);
                String location = autoCompView.getText().toString();                //Create the bundle
//                Bundle bundle = new Bundle();

//Add your data to bundle
//                bundle.putString("stuff", location);

//Add the bundle to the intent
//                i.putExtras(bundle);

//Fire that second activity
//                startActivity(i);
                new getLocationInfo().execute(location);

            }
        });
    }


    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_location, menu);
        MenuItem searchItem = menu.findItem(R.id.location_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Configure the search info and add any event listeners

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private static final String LOG_TAG = "ExampleApp";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyA23jRoKzRI16Al7c88DpHxHDuW_OUtLjU";

    private ArrayList<String> autocomplete(String input) throws IOException, JSONException {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&types=(cities)");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

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
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }
        Log.i("List", resultList.toString());

        return resultList;
    }

    class getLocationInfo extends AsyncTask<String, Void, Void> {


        protected Void doInBackground(String... input) {
            //Execurte the network related option here
            JSONObject result = null;
            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();
            try {
                StringBuilder sb = new StringBuilder(PLACES_API_BASE + "/textsearch" + OUT_JSON);
                sb.append("?key=" + API_KEY);
                sb.append("&query=" + URLEncoder.encode(input[0], "utf8"));

                URL url = new URL(sb.toString());
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());

                // Load the results into a StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }

                try {
                    // Create a JSON object hierarchy from the results
                    if(jsonResults.toString() == null)
                    {
                        System.out.println("JSON object is null");
                    }
                    else {
                        JSONObject jsonObj = new JSONObject(jsonResults.toString());
                        //JSONObject test = jsonObj.getJSONObject("error_message");

                        JSONArray predsJsonArray = jsonObj.getJSONArray("results");


                        // Extract the Place descriptions from the results

                        result = new JSONObject();
                        result.put("formatted_address", predsJsonArray.getJSONObject(0).getString("formatted_address"));
                        result.put("id", predsJsonArray.getJSONObject(0).getString("id"));
                        result.put("name", predsJsonArray.getJSONObject(0).getString("name"));
                        result.put("place_id", predsJsonArray.getJSONObject(0).getString("place_id"));
                        result.put("reference", predsJsonArray.getJSONObject(0).getString("reference"));
System.out.println("Name : "+predsJsonArray.getJSONObject(0).getString("name") );

                        jsonObj = new JSONObject(predsJsonArray.getJSONObject(0).getString("geometry"));
                        jsonObj = new JSONObject(jsonObj.getString("location"));


                        result.put("lat", jsonObj.getString("lat"));
                        result.put("lng", jsonObj.getString("lng"));
                    }

                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Cannot process JSON results", e);
                }


            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error processing Places API URL", e);
                return null;

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error connecting to Places API", e);
                return null;

            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            Intent i = new Intent(getApplicationContext(), NewTripActivity.class);
            i.putExtra("PlaceDetails", result.toString());
            startActivity(i);
            return null;
        }

        protected void onPostExecute(ArrayList result) {
            Log.i("List ********",result.toString());
        }

        }



    public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        try {
                            resultList = autocomplete(constraint.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            ;
                        }

                         System.out.println("List size"+resultList.size());
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                    }
            };
            return filter;
        }
    }

}