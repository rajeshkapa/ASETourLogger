package asepg15.umkc.edu.tourlogger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import asepg15.umkc.edu.tourlogger.dto.User;
import asepg15.umkc.edu.tourlogger.validate.UserValidate;


public class RegisterActivity extends ActionBarActivity {

    private Button Register;
    private Button Previous;
    private EditText firstName;
    private EditText lastName;
    private EditText mobileNumber;
    private EditText emailid;
    private EditText password;
    private EditText reenterPassword;
    String validationMessage;


    private class UserRegistration extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder response = new StringBuilder();
            for (String url : urls) {

                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response.append(s);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println(response.toString());
            return response.toString();
        }

        @Override
        protected void onPreExecute() {
            //super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            try {

                if(result.equalsIgnoreCase("\"success\"")) {
                    Toast.makeText(getBaseContext(), "Registration Done Successfully..!!!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            } catch (Exception e) {
                //Log.d("ReadPlacesFeedTask", e.getLocalizedMessage());
                System.out.println(e.getStackTrace());
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //super.onProgressUpdate(values);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Register = (Button) findViewById(R.id.register);
        Previous = (Button) findViewById(R.id.previous);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        mobileNumber = (EditText) findViewById(R.id.phnumber);

        emailid=(EditText) findViewById(R.id.email);

        password = (EditText) findViewById(R.id.password);
        reenterPassword = (EditText) findViewById(R.id.reenterPassword);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fn = firstName.getText().toString();
                String ln = lastName.getText().toString();
                String num= mobileNumber.getText().toString();
                String email= emailid.getText().toString();
                String pwd = password.getText().toString();
                String repwd = reenterPassword.getText().toString();
                User user= new User(fn,ln,num,email,pwd,repwd);
                UserValidate validate=new UserValidate();
                validationMessage=validate.validateRegisterUser(user);
                if(!validationMessage.equalsIgnoreCase("ok"))
                {
                    Toast.makeText(asepg15.umkc.edu.tourlogger.RegisterActivity.this,validationMessage, Toast.LENGTH_LONG).show();
                }
                else
                {
                    UserRegistration regcheck = new UserRegistration();
                    regcheck.execute(new String[]{"http://kc-sce-cs551-2.kc.umkc.edu/aspnet_client/MPG15/TourLoggerService/Service1.svc/register/user/"+ fn + "/" + ln+ "/" + num+ "/" + email+ "/" + pwd});

                }


            }
        });


        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(intent);
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
