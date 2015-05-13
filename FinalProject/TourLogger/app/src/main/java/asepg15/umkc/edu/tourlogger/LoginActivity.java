package asepg15.umkc.edu.tourlogger;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import asepg15.umkc.edu.tourlogger.validate.UserValidate;


public class LoginActivity extends ActionBarActivity {
    private EditText Username;
    private EditText Password;
    String validationMessage;

    private class UserLoginCheck extends AsyncTask<String,Void,String> {

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

                if(result.equalsIgnoreCase("true")) {
                    Toast.makeText(getBaseContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MytripsActivity.class);
                    startActivity(intent);
                }else {
                    Toast toast = Toast.makeText(getBaseContext(), "Invalid Username/Password", Toast.LENGTH_SHORT);
                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                    v.setTextColor(android.R.color.holo_red_dark);
                    toast.show();
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
        setContentView(R.layout.activity_login);

        Button Signup = (Button) findViewById(R.id.button2);
         Username = (EditText) findViewById(R.id.username);
         Password = (EditText) findViewById(R.id.password);

        Signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String un = Username.getText().toString();
                String pw = Password.getText().toString();
                UserValidate validate = new UserValidate();
                validationMessage = validate.validateLoginUser(un, pw);


                if(!validationMessage.equalsIgnoreCase("ok")){
                    Toast.makeText(asepg15.umkc.edu.tourlogger.LoginActivity.this, validationMessage, Toast.LENGTH_LONG).show();
                }
                else {
                    UserLoginCheck logincheck = new UserLoginCheck();
                    logincheck.execute(new String[]{"http://kc-sce-cs551-2.kc.umkc.edu/aspnet_client/MPG15/TourLoggerService/Service1.svc/login/user/"+ un + "/" + pw});

                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
