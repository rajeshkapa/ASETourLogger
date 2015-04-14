package asepg15.umkc.edu.tourlogger;

/**
 * Created by kh1441 on 4/13/15.
 */


    import android.app.Activity;

    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.AdapterView.OnItemClickListener;

    import android.widget.ListView;
    import android.widget.Toast;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.ArrayList;


public class HotelsView extends Activity{
    private static final String LOG_TAG = "ExampleApp";

        ListView list;
        String[] itemname;
        String[] vicinity;
        String[] imgid;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            final String jsonResults = getIntent().getStringExtra("HotelDetails");
            ArrayList<JSONObject> resultList = null;

            try {
                // Create a JSON object hierarchy from the results
                JSONObject jsonObj = new JSONObject(jsonResults);
                JSONArray predsJsonArray = jsonObj.getJSONArray("results");
                JSONObject result  = null;
                // Extract the Place descriptions from the results
                resultList = new ArrayList<JSONObject>(predsJsonArray.length());
                imgid = new String[predsJsonArray.length()];
                itemname = new String[predsJsonArray.length()];
                vicinity = new String[predsJsonArray.length()];
                int limit;
                if(predsJsonArray.length() > 10)
                {
                    limit = 10;
                }
                else
                {
                    limit = predsJsonArray.length();
                }
                for (int i = 0; i < limit; i++) {
                    result = new JSONObject();
                    // result.put("formatted_address", predsJsonArray.getJSONObject(0).getString("formatted_address"));
                    result.put("id", predsJsonArray.getJSONObject(i).getString("id"));
                    result.put("name", predsJsonArray.getJSONObject(i).getString("name"));
                    itemname[i] = predsJsonArray.getJSONObject(i).getString("name");
                    result.put("place_id", predsJsonArray.getJSONObject(i).getString("place_id"));
                    result.put("reference", predsJsonArray.getJSONObject(i).getString("reference"));
                    result.put("rating", predsJsonArray.getJSONObject(i).getString("rating"));
                    result.put("vicinity", predsJsonArray.getJSONObject(i).getString("vicinity"));
                    vicinity[i]=predsJsonArray.getJSONObject(i).getString("vicinity");
                    jsonObj = new JSONObject(predsJsonArray.getJSONObject(i).getString("geometry"));
                    jsonObj = new JSONObject(jsonObj.getString("location"));
                    result.put("lat", jsonObj.getString("lat"));
                    result.put("lng", jsonObj.getString("lng"));
                    JSONArray predsJsonArray1 = predsJsonArray.getJSONObject(i).getJSONArray("photos");
                    result.put("photo", predsJsonArray1.getJSONObject(0).getString("photo_reference"));
                    imgid[i] = predsJsonArray1.getJSONObject(0).getString("photo_reference");

                    resultList.add(result);


                }


            }



            catch (JSONException e) {
                Log.e(LOG_TAG, "Cannot process JSON results", e);
            }
            Log.i("List",resultList.toString());
            CustomHotelsAdapter adapter=new CustomHotelsAdapter(this, itemname, imgid,vicinity);
            list=(ListView)findViewById(R.id.list);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    String Slecteditem= itemname[+position];
                    Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

