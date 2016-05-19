/*
 * TCSS 450 - Mobile App Programming
 * @author Weiwei Shi, Kyle Doan
 * @version 1.0
 */

package tcss450.uw.edu.mobileproject.authenticate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
 * The user can register a new account.
 *
 * @author Weiwei Shi
 * @version May 5, 2016
 */
public class RegistrationActivity extends AppCompatActivity {

    //variables
    private EditText mEmailText;
    private EditText mPwdText;
    private EditText mConfirmPwText;

    private Button mRegisterButton;
    private Button mBackToLogInButton;


    public static final String USER_EMAIL = "tcss450.uw.edu.mobileproject.EMAIL";

    private static final String REGISTER_URL = "http://cssgate.insttech.washington.edu/~_450btm7/addUser.php";


    /**
     * Called when the activity is starting.
     * @param savedInstanceState contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // change color for navigation bar and status bar
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, null));
            window.setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark, null));
        }

        mEmailText = (EditText) findViewById(R.id.reg_email);
        mPwdText = (EditText) findViewById(R.id.reg_psw);
        mConfirmPwText = (EditText) findViewById(R.id.reg_confirm_psw);

        mRegisterButton = (Button) findViewById(R.id.register_button);
        mBackToLogInButton = (Button) findViewById(R.id.linkTo_login_button);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {

            /**
             * mRegisterButton onClickListener,send the data to the web server.
             * @param v view.
             */
            @Override
            public void onClick(View v) {
                final String userId = mEmailText.getText().toString().trim().toLowerCase();
                String pwd = mPwdText.getText().toString();
                String confirmPw = mConfirmPwText.getText().toString();
                if (TextUtils.isEmpty(userId)) {
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

                if (TextUtils.isEmpty(pwd)) {
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
                if (TextUtils.isEmpty(confirmPw)) {
                    Toast.makeText(v.getContext(), "Enter confirm password"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mConfirmPwText.requestFocus();
                    return;
                }
                if (!confirmPw.equals(pwd)) {
                    Toast.makeText(v.getContext(), "Confirm password does not match"
                            , Toast.LENGTH_SHORT)
                            .show();
                    mConfirmPwText.requestFocus();
                    return;
                }
                if (mEmailText.getText().length() != 0 && mPwdText.getText().length() != 0) {
                    String url = buildUserURL(userId, pwd.hashCode());
                    url += "?email=" + mEmailText.getText().toString() +
                            "&pwd=" + mPwdText.getText().toString().hashCode();
                    RegistrationTask task = new RegistrationTask();
                    task.setUser(mEmailText.getText().toString());
                    task.execute(url);
                }
            }
        });

        mBackToLogInButton.setOnClickListener(new View.OnClickListener() {
            /**
             * mBackToLogInButton onClickListener, go back to SignInActivity.
             * @param v view
             */
            @Override
            public void onClick(View v) {
                // Prevent over stack from user when keep switching login and registration screen
                Intent i = new Intent(RegistrationActivity.this, SignInActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegistrationActivity.this, SignInActivity.class);
        startActivity(i);
        finish();
    }

    private String buildUserURL(String email, int passEncrypted) {
        StringBuilder sb = new StringBuilder(REGISTER_URL);
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
     * Store data by using web service.
     * Network connection credits: http://developer.android.com/training/basics/network-ops/connecting.html
     */
    private class RegistrationTask extends AsyncTask<String, Void, String> {

        private String mUser;

        private static final String TAG = "RegistrationTask";

        void setUser(String user) {
            mUser = user;
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
         * @param stream the inputstream
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

            // Parse JSON
            try {
                JSONObject jsonObject = new JSONObject(s);
                String status = jsonObject.getString("result");
                if (status.equalsIgnoreCase("success")) {
                    Toast.makeText(getApplicationContext(), "Successfully created a new account!",
                            Toast.LENGTH_SHORT)
                            .show();
                    SharedPreferences sharedPreferences =
                            getSharedPreferences(getString(R.string.LOGIN_PREFS),
                            Context.MODE_PRIVATE);
                    sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), true).apply();
                    sharedPreferences.edit().putString(getString(R.string.USER), mUser).apply();
                    Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                    intent.putExtra(USER_EMAIL, mUser);
                    startActivity(intent);
                    finish();
                } else {
                    String reason = jsonObject.getString("error");
                    Toast.makeText(getApplicationContext(), "Failed :" + reason,
                            Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (Exception e) {
                Log.d(TAG, "Parsing JSON Exception " + e.getMessage());
            }
        }

    }
}


