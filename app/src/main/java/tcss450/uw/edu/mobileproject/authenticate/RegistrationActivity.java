package tcss450.uw.edu.mobileproject.authenticate;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import tcss450.uw.edu.mobileproject.HomeActivity;
import tcss450.uw.edu.mobileproject.R;

public class RegistrationActivity extends AppCompatActivity {

    //variables
    private EditText mEmailText;
    private EditText mPwdText;

    private Button mRegisterButton;
    private Button mBackToLogInButton;



    private String url = "http://cssgate.insttech.washington.edu/~_450btm7/addUser.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mEmailText = (EditText)findViewById(R.id.reg_email);
        mPwdText = (EditText)findViewById(R.id.reg_psw);

        mRegisterButton = (Button)findViewById(R.id.register_button);
        mBackToLogInButton = (Button)findViewById(R.id.linkTo_login_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mEmailText.getText().length() != 0 && mPwdText.getText().length() != 0){
                    url += "?email=" + mEmailText.getText().toString() + "&pwd=" + mPwdText.getText().toString();
                    new RegistrationTask().execute(url);
                }
            }
        });

        mBackToLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegistrationActivity.this, SignInActivity.class);
                startActivity(i);
            }
        });
    }


    private class RegistrationTask extends AsyncTask<String, Void, String> {

        private static final String TAG = "RegistrationTask";

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL is invalid.";
            }
        }

        /**
         * JSON Parser:
         * Given a URL, establishes an HttpUrlConnection and retrieves the webpage conten as a InputStream
         * @param myUrl
         * @return a string
         * @throws IOException
         */
        private String downloadUrl(String myUrl) throws IOException {
            InputStream is = null;

            // Only display the first 2000 characters of the retrieved
            // web page content.
            int len = 2000;

            try {
                URL url = new URL(myUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(TAG, "The response is: " + response);
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = convert(is, len);
                Log.d(TAG, "The string is: " + contentAsString);
                return contentAsString;

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } catch(Exception e ) {
                Log.d(TAG, "Something happened" + e.getMessage());
            }
            finally {
                if (is != null) {
                    is.close();
                }
            }
            return null;
        }

        /**
         * Read an inputStream and convert it to a String.
         * @param stream
         * @param len
         * @return
         * @throws IOException
         * @throws UnsupportedEncodingException
         */
        public String convert(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            Reader reader = null;
            reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // Parse JSON
            try {
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("result");
                if (status.equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), "Successfully created a new account!",
                            Toast.LENGTH_SHORT)
                            .show();
                    Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    String reason = jsonObject.getString("error");
                    Toast.makeText(getApplicationContext(), "Failed :" + reason,
                            Toast.LENGTH_SHORT)
                            .show();
                }

                //getFragmentManager().popBackStackImmediate();
            } catch (Exception e) {
                Log.d(TAG, "Parsing JSON Exception " + e.getMessage());
            }
        }

    }
}


