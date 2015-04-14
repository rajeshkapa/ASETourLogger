package asepg15.umkc.edu.tourlogger;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;


public class NewTripActivity extends ActionBarActivity {
    //UI References
    private TextView fromDateEtxt;
    private TextView toDateEtxt;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;

    public static final String KEY_PUBDATE = "news_pub_date";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_trip);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

//Get the bundle
        Bundle bundle = getIntent().getExtras();

//Extract the data?


        final String placeDetails = getIntent().getStringExtra("PlaceDetails");
        JSONObject final_list = null;

        try{
            final_list = new JSONObject(placeDetails);
            // 3. show message on textView

            ((TextView)findViewById(R.id.nameofcity)).setText(final_list.getString("name"));

        }
        catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        findViewsById();

        setDateTimeField();

        Button start = (Button) findViewById(R.id.sDate);
        Button end = (Button) findViewById(R.id.eDate);
        Button savetrip = (Button) findViewById(R.id.save);
        start.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                fromDatePickerDialog.show();
            }
        });

        end.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                toDatePickerDialog.show();
            }
        });

        savetrip.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                /*
                On Click save Place Details to Database along with the dates entered
                 */

                Intent i = new Intent(getApplicationContext(), MytripsActivity.class);
                i.putExtra("StartDate",fromDateEtxt.getText().toString());
                i.putExtra("EndDate",toDateEtxt.getText().toString());
                i.putExtra("PlaceDetails",placeDetails);
                startActivity(i);

            }
        });


    }

    private void findViewsById() {
        fromDateEtxt = (TextView) findViewById(R.id.displaySDate);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();

        toDateEtxt = (TextView) findViewById(R.id.diaplayEDate);
        toDateEtxt.setInputType(InputType.TYPE_NULL);
    }

    private void setDateTimeField() {



        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_trip, menu);
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
}
