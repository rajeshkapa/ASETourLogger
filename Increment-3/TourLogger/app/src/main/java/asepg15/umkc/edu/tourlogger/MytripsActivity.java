package asepg15.umkc.edu.tourlogger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ListView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MytripsActivity extends ActionBarActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mytrips);
        final String fromDate = getIntent().getStringExtra("StartDate");

        final String placeDetails = getIntent().getStringExtra("PlaceDetails");
        final String toDate = getIntent().getStringExtra("EndDate");
        ListView list;
        JSONObject final_list = null;
        String[] itemname = new String[1];
        String[] date = new String[1];
        Integer[] imgid = new Integer[1];
        if(placeDetails != null) {
                try {
                final_list = new JSONObject(placeDetails);
                itemname[0] = final_list.getString("name");
                date[0] = fromDate + " - " + toDate;
                imgid[0] = R.drawable.welcome;
            } catch (JSONException e) {
                e.printStackTrace();

                    }
//        String[] itemname = {
//                "Safari",
//                "Camera",
//                "Global",
//                "FireFox",
//                "UC Browser",
//                "Android Folder",
//                "VLC Player",
//                "Cold War"
//        };
//        Integer[] imgid={
//                R.drawable.car_hire,
//                R.drawable.car_hire,
//                R.drawable.car_hire,
//                R.drawable.car_hire,
//                R.drawable.car_hire,
//                R.drawable.car_hire,
//                R.drawable.car_hire,
//                R.drawable.car_hire,
//        };


            CustomListAdapter adapter = new CustomListAdapter(this, itemname, imgid, date);
            list = (ListView) findViewById(R.id.list);
            list.setAdapter(adapter);

            list.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub
                    // String Slecteditem= itemname[+position];
                    //Toast.makeText(getApplicationContext(), Slecteditem, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), Trip.class);
                    i.putExtra("StartDate",fromDate);
                    i.putExtra("EndDate",toDate);
                    i.putExtra("PlaceDetails",placeDetails);
                    startActivity(i);
                }
            });
            }
    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mytrips, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


    switch (id) {
        case R.id.trip_add:
            Intent i = new Intent(getApplicationContext(), SearchLocationActivity.class);
            startActivity(i);
            return true;
        case R.id.action_settings:

            return true;
        default:
        return super.onOptionsItemSelected(item);
    }
    }
}
