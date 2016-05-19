/*
 * TCSS 450 - Mobile App Programming
 * @author Weiwei Shi, Kyle Doan
 * @version 1.0
 */

package tcss450.uw.edu.mobileproject.authenticate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import java.net.URLEncoder;

import tcss450.uw.edu.mobileproject.HomeActivity;
import tcss450.uw.edu.mobileproject.R;

/**
 * The user can sign in.
 *
 * @author Weiwei Shi
 * @version May 5, 2016
 */
public class SignInActivity extends AppCompatActivity {

    //variables
    private EditText mEmailText;
    private EditText mPwdText;

    private Button mSignInButton;
    private Button mRegisterButton;

    private final static String LOGIN_URL = "http://cssgate.insttech.washington.edu/~_450btm7/login.php";

    //Progress Dialog
    private ProgressDialog mDialog;
    private SharedPreferences mSharedPreferences;

    //Constant
    //JSON element ids from response of php script
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "SignInActivity";

    public static final String USER_EMAIL = "tcss450.uw.edu.mobile.EMAIL";

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);
        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            // change color for navigation bar and status bar
            Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, null));
                window.setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark, null));
            }

            //setup input fields
            mEmailText = (EditText) findViewById(R.id.login_email);
            mPwdText = (EditText) findViewById(R.id.login_psw);

            //setup buttons
            mSignInButton = (Button) findViewById(R.id.login_button);
            mRegisterButton = (Button) findViewById(R.id.linkTo_register_button);

            mDialog = new ProgressDialog(SignInActivity.this);

            //setup listeners
            mSignInButton.setOnClickListener(new View.OnClickListener() {

                /**
                 * mSignInButton onClickListener,send the data to the web server.
                 * @param v view
                 */
                @Override
                public void onClick(View v) {

                    final String userId = mEmailText.getText().toString().trim().toLowerCase();
                    String pwd = mPwdText.getText().toString();
                    if (TextUtils.isEmpty(userId)) {
                        Toast.makeText(v.getContext(), "Enter userid"
                                , Toast.LENGTH_SHORT)
                                .show();
                        mEmailText.requestFocus();
                        return;
                    }
                    if (TextUtils.isEmpty(pwd)) {
                        Toast.makeText(v.getContext(), "Enter password"
                                , Toast.LENGTH_SHORT)
                                .show();
                        mPwdText.requestFocus();
                        return;
                    }

                    if (userId.length() != 0 && pwd.length() != 0) {
                        String url = buildSignInURL(userId, pwd.hashCode());
                        LogInTask login = new LogInTask();
                        login.setUserId(userId);
                        login.execute(url);
                        return;
                    }
                }
            });

            mRegisterButton.setOnClickListener(new View.OnClickListener() {

                /**
                 * mRegisterButton onClickListener, go to the RegistrationActivity.
                 * @param v view
                 */
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(SignInActivity.this, RegistrationActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        } else {
            Intent i = new Intent(this, HomeActivity.class);
            String user;
            user = mSharedPreferences.getString(getString(R.string.USER), null);
            i.putExtra(USER_EMAIL, user);
            startActivity(i);
            finish();
        }

    }

    private String buildSignInURL(String email, int passEncrypted) {
        StringBuilder sb = new StringBuilder(LOGIN_URL);
        try {
            sb.append("?email=");
            sb.append(URLEncoder.encode(email, "UTF-8"));

            sb.append("&pwd=");
            sb.append(URLEncoder.encode(String.valueOf(passEncrypted), "UTF-8"));
        } catch (Exception e) {
            Toast.makeText(this, "Something wrong with the url " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        return sb.toString();
    }

    /**
     * Retrieve data by using web service.
     * Network connection credits: http://developer.android.com/training/basics/network-ops/connecting.html
     */
    private class LogInTask extends AsyncTask<String, Void, String> {

        private static final String TAG = "LogInTask";

        private String mUser;

        void setUserId(String email) {
            mUser = email;
        }

        /**
         * Override this method to perform a computation on a background thread.
         *
         * @param urls receiving the web URLs.
         * @return go to downloadUrl method.
         */
        @Override
        protected String doInBackground(String... urls) {

            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL is invalid.";
            }
        }

        /**
         * Given a URL, establishes an HttpUrlConnection and retrieves the web page content as a InputStream.
         *
         * @param myUrl the URL link
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
            } catch (Exception e) {
                Log.d(TAG, "Something happened" + e.getMessage());
            } finally {
                if (is != null) {
                    is.close();
                }
            }
            return null;
        }

        /**
         * Read an inputStream and convert it to a String.
         *
         * @param stream input stream
         * @param len    the length of the InputStream
         * @return a string so that the activity can display it in the UI
         * @throws IOException
         */
        public String convert(InputStream stream, int len) throws IOException {
            Reader reader = new InputStreamReader(stream, "UTF-8");
            char[] buffer = new char[len];
            reader.read(buffer);
            return new String(buffer);
        }

        /**
         * Runs on the UI thread.
         *
         * @param s the string passed in.
         */
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
                    mSharedPreferences.edit()
                            .putBoolean(getString(R.string.LOGGEDIN), true)
                            .apply();
                    mSharedPreferences.edit()
                            .putString(getString(R.string.USER), mUser)
                            .apply();
                    Intent myIntent = new Intent(SignInActivity.this, HomeActivity.class);
                    myIntent.putExtra(USER_EMAIL, mUser);
                    finish();
                    startActivity(myIntent);
                    hideDialog();
                } else {
                    String reason = jsonObject.getString("error");
                    Toast.makeText(SignInActivity.this, "Failed :" + reason,
                            Toast.LENGTH_SHORT)
                            .show();
                }

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
     * Method for hiding progressDialog
     */
    private void hideDialog() {
        if (mDialog.isShowing())
            mDialog.dismiss();
    }
}




