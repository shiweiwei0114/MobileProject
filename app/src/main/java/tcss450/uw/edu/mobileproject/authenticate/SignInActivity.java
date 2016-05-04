package tcss450.uw.edu.mobileproject.authenticate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

public class SignInActivity extends AppCompatActivity {

    //variables
    private EditText mEmailText;
    private EditText mPwdText;

    private Button mSignInButton;
    private  Button mRegisterButton;

    private String url = "http://cssgate.insttech.washington.edu/~_450btm7/login.php";

    //Progress Dialog
    private ProgressDialog mDialog;

    //Constant
    //JSON element ids from response of php script
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public static final String USER_EMAIL = "tcss450.uw.edu.mobile.EMAIL";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new LoginFragment()).commit();

        //setup input fields
        mEmailText = (EditText)findViewById(R.id.login_email);
        mPwdText = (EditText)findViewById(R.id.login_psw);

        //setup buttons
        mSignInButton = (Button)findViewById(R.id.login_button);
        mRegisterButton = (Button)findViewById(R.id.linkTo_register_button);

        mDialog = new ProgressDialog(SignInActivity.this);

        //setup listeners
        mSignInButton.setOnClickListener(new View.OnClickListener() {

    @Override
    public void login(String userId, String pwd) {
        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra(USER_EMAIL, userId);
        startActivity(i);
        finish();
            @Override
            public void onClick(View v) {

                    String userId = mEmailText.getText().toString();
                    String pwd = mPwdText.getText().toString();
                    if (TextUtils.isEmpty(userId))  {
                        Toast.makeText(v.getContext(), "Enter userid"
                                , Toast.LENGTH_SHORT)
                                .show();
                        mEmailText.requestFocus();
                        return;
                    }
                    if (!userId.contains("@")) {
                        Toast.makeText(v.getContext(), "Enter a valid email address"
                                , Toast.LENGTH_SHORT)
                                .show();
                        mEmailText.requestFocus();
                        return;
                    }

                    if (TextUtils.isEmpty(pwd))  {
                        Toast.makeText(v.getContext(), "Enter password"
                                , Toast.LENGTH_SHORT)
                                .show();
                        mPwdText.requestFocus();
                        return;
                    }
                    if (pwd.length() < 6) {
                        Toast.makeText(v.getContext(), "Enter password of at least 6 characters"
                                , Toast.LENGTH_SHORT)
                                .show();
                        mPwdText.requestFocus();
                        return;
                    }

                    if (userId.length() != 0 && pwd.length() != 0){
                        url += "?email=" + userId + "&pwd=" +pwd;
                        new LogInTask().execute(url);
                        return;
                    }
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });

    }


//    @Override
//    public void login(String userId, String pwd) {
//        Intent i = new Intent(this, HomeActivity.class);
//        startActivity(i);
//        finish();
//    }

    private class LogInTask extends AsyncTask<String, Void, String> {

        private static final String TAG = "LogInTask";

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
            try {
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("result");

                if (status.equalsIgnoreCase("success")) {

                    showDialog();
                    mDialog.setMessage("Logging in ...");

                    Toast.makeText(SignInActivity.this, "Successfully log in...",
                            Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(SignInActivity.this, HomeActivity.class);
                    finish();
                    startActivity(myIntent);
                    hideDialog();
                } else {

                    String reason = jsonObject.getString("error");
                    Toast.makeText(SignInActivity.this, "Failed :" + reason,
                            Toast.LENGTH_SHORT)
                            .show();

                }

                //getFragmentManager().popBackStackImmediate();

            } catch (Exception e) {
                Log.d(TAG, "Parsing JSON Exception " + e.getMessage());
            }
        }
    }

    /**
     * Method for showing progressDialog
     */
    private void showDialog() {
        if (!mDialog.isShowing())
            mDialog.show();
    }

    /**
     * Method for hiding progressdialog
     */
    private void hideDialog() {
        if (mDialog.isShowing())
            mDialog.dismiss();
    }
}




