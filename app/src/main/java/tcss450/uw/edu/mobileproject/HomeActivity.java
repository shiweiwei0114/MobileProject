/*
 * TCSS 450 - Mobile App Programming
 * @author Weiwei Shi, Kyle Doan
 * @version 1.0
 */

package tcss450.uw.edu.mobileproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import tcss450.uw.edu.mobileproject.authenticate.SignInActivity;
import tcss450.uw.edu.mobileproject.model.Question;

/**
 * The home page of activity of the app.
 *
 * @author Kyle Doan, Weiwei Shi
 * @version May 5, 2016
 */
public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        QuestionsListFragment.OnListFragmentInteractionListener,
        QuestionAddFragment.QuestionAddListener {

    private final static String LOG = "HomeActivity";

    /** user email to transfer to other fragment. */
    private String mUserEmail;

    /**
     * Called when the activity is starting.
     * @param savedInstanceState contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mUserEmail = intent.getStringExtra(SignInActivity.USER_EMAIL);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_question);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {

                /**
                 * FloatingActionButton onClickListener,send the data to the web server.
                 * @param v view
                 */
                @Override
                public void onClick(View v) {
                    QuestionAddFragment questionAddFragment = new QuestionAddFragment();
                    questionAddFragment.setUserEmail(mUserEmail);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, questionAddFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            QuestionsListFragment questListFragment = new QuestionsListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, questListFragment)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Called when a context menu for the view is about to be shown.
     * @param menu The context menu that is being built.
     * @return return true if it has options
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    /**
     * Called whenever an item in your options menu is selected.
     * @param item the menu item that was selected.
     * @return return true if has item.
     */
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

        if (id == R.id.action_logout) {
            Intent i = new Intent(this, SignInActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called whenever a navigation item in the action bar is selected.
     * @param item is selected.
     * @return true if the item is selected.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    /**
     * The list of the items on the Fragments.
     * @param item the list of the items.
     */
    @Override
    public void onListFragmentInteraction(Question item) {
        QuestionPostFragment questionPostFragment = new QuestionPostFragment();
        Bundle args = new Bundle();
        args.putSerializable(QuestionPostFragment.QUEST_SELECTED, item);
        questionPostFragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, questionPostFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Add queston task.
     * @param url the url received.
     */
    @Override
    public void addQuestion(String url) {
        AddQuestionTask task = new AddQuestionTask();
        task.execute(url);
        // Takes you back to the previous fragment by popping the current fragment out.
        getSupportFragmentManager().popBackStackImmediate();
    }


    /**
     * Store data by using web service.
     */
    private class AddQuestionTask extends AsyncTask<String, Void, String> {

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute} by the caller of this task.
         * This method can call {@link #publishProgress} to publish updates on the UI thread.
         *
         * @param urls The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s;
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to add question, Reason: " + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result the string in echo from php file.
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Question successfully added!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Log.i(LOG, jsonObject.get("error").toString());
                    Toast.makeText(getApplicationContext(), "Failed to add: "
                            + jsonObject.get("error"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Log.i(LOG, e.getMessage());
                Toast.makeText(getApplicationContext(), "Something wrong with the data "
                        + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
