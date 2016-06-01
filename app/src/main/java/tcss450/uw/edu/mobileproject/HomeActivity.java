/*
 * TCSS 450 - Mobile App Programming
 * @author Weiwei Shi, Kyle Doan
 * @version 1.0
 */

package tcss450.uw.edu.mobileproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tcss450.uw.edu.mobileproject.authenticate.SignInActivity;
import tcss450.uw.edu.mobileproject.model.Question;
import tcss450.uw.edu.mobileproject.offlineDatabase.ProjectDB;

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

    private final static String TAGS_URL =
            "http://cssgate.insttech.washington.edu/~_450btm7/test.php?cmd=tags_list";

    /**
     * user email to transfer to other fragment.
     */
    private String mUserEmail;
    private ArrayList<String> mTags;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private QuestionsListFragment mQuestListFragment;

    private ShareActionProvider mShareActionProvider;

    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTags = new ArrayList<>();
        mTags.add("All");
        // download Tags list
        DownloadTagsTask tagsTask = new DownloadTagsTask();
        tagsTask.execute(TAGS_URL);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (mDrawerLayout != null) {
            mDrawerLayout.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
        mDrawerList = (ListView) findViewById(R.id.tags_drawer);

        // get user from either SignInActivity or RegistrationActivity;
        Intent intent = getIntent();
        mUserEmail = intent.getStringExtra(SignInActivity.USER_EMAIL);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_question);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.share);
        fab2.hide();

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {

                /**
                 * FloatingActionButton onClickListener,send the data to the web server.
                 * @param v view
                 */
                @Override
                public void onClick(View v) {
                    QuestionAddFragment questionAddFragment = new QuestionAddFragment();
                    Bundle args = new Bundle();
                    args.putString(QuestionAddFragment.USER, mUserEmail);
                    args.putStringArrayList(QuestionAddFragment.TAGS, mTags);
                    questionAddFragment.setArguments(args);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, questionAddFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            mQuestListFragment = new QuestionsListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mQuestListFragment)
                    .commit();
        }
    }

    public List<String> getTags() {
        return mTags;
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
     *
     * @param menu The context menu that is being built.
     * @return return true if it has options
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        return super.onCreateOptionsMenu(menu);
    }


    /**
     * Called whenever an item in your options menu is selected.
     *
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
//        if (id == R.id.menu_item_share) {
//            //mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
//            //SharedPreferences questionPrefs = getPreferences(QuestionPostFragment.QUEST_SELECTED, 0);
//            Log.d("TAG","whatever");
//            Intent sendIntent = new Intent();
//            sendIntent.setAction(Intent.ACTION_SEND);
//            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, check out this interview question!" + "question");
//            sendIntent.setType("text/plain");
//            startActivity(Intent.createChooser(sendIntent, "Share via"));
//           // startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
//            return true;
//        }

        if (id == R.id.action_logout) {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                    Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false).apply();
            sharedPreferences.edit().putString(getString(R.string.USER), null).apply();
            Intent i = new Intent(this, SignInActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Called whenever a navigation item in the action bar is selected.
     *
     * @param item is selected.
     * @return true if the item is selected.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_add) {
            QuestionAddFragment questionAddFragment = new QuestionAddFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container,questionAddFragment).addToBackStack(null).commit();
        } else if (id == R.id.nav_aboutUs) {
            AboutUsFragment aboutUsFragment = new AboutUsFragment();
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.fragment_container, aboutUsFragment).addToBackStack(null).commit();
        } else if (id == R.id.nav_logOut) {
            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                    Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false).apply();
            sharedPreferences.edit().putString(getString(R.string.USER), null).apply();
            Intent i = new Intent(this, SignInActivity.class);
            startActivity(i);
            finish();
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    /**
     * The list of the items on the Fragments.
     *
     * @param item the list of the items.
     */
    @Override
    public void onListFragmentInteraction(Question item) {
        QuestionPostFragment questionPostFragment = new QuestionPostFragment();
        Bundle args = new Bundle();
        args.putSerializable(QuestionPostFragment.QUEST_SELECTED, item);
        args.putString(QuestionPostFragment.USER, mUserEmail);
        questionPostFragment.setArguments(args);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, questionPostFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Add queston task.
     *
     * @param url the url received.
     */
    @Override
    public void addQuestion(String url) {
        AddQuestionTask task = new AddQuestionTask();
        task.execute(url);
        // Takes you back to the previous fragment by popping the current fragment out.
        getSupportFragmentManager().popBackStackImmediate();
        mTags.clear();
        mTags.add("All");
        DownloadTagsTask tagsTask = new DownloadTagsTask();
        tagsTask.execute(TAGS_URL);
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

    private class DownloadTagsTask extends AsyncTask<String, Void, String> {

        private ProjectDB mDB;
        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
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
            if (result.startsWith("Unable to")) {
                Toast.makeText(HomeActivity.this,
                        result, Toast.LENGTH_LONG).show();
                return;
            }
            result = saveToDB(result);
            // Something wrong with the JSON returned.
            if (result != null) {
                Toast.makeText(HomeActivity.this,
                        result, Toast.LENGTH_LONG).show();
                return;
            }
            mTags.addAll(mDB.getTagsList());
            // set up the drawer's list view with items and click listener
            mDrawerList.setAdapter(new ArrayAdapter<>(HomeActivity.this,
                    R.layout.drawer_list_tags, mTags));
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
            Log.i(LOG, "Size of tag list " + mTags.size());
        }

        private String saveToDB(String tagJSON) {
            if (mDB == null) {
                mDB = new ProjectDB(getApplicationContext());
            }

            // Delete old data so that you can refresh the local
            // database with the network data.
            mDB.deleteTagsTable();

            String reason = null;
            if (tagJSON != null) {
                try {
                    JSONArray arr = new JSONArray(tagJSON);
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        String tag = obj.getString(Question.TAG_NAME);
                        String questId = obj.getString(Question.ID);
                        mDB.insertTag(tag, questId);
                    }
                } catch (JSONException e) {
                    reason = "Unable to parse tags data, Reason: " + e.getMessage();
                }
            }
//            mDB.closeDB();
            return reason;
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        /**
         * Callback method to be invoked when an item in this AdapterView has
         * been clicked.
         * <p/>
         * Implementers can call getItemAtPosition(position) if they need
         * to access the data associated with the selected item.
         *
         * @param parent   The AdapterView where the click happened.
         * @param view     The view within the AdapterView that was clicked (this
         *                 will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         * @param id       The row id of the item that was clicked.
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i(LOG, "position of tag " + position);
            selectTag(position);
        }
    }

    private void selectTag(int position) {
        String tagFilter = mTags.get(position);
        Log.i(LOG, "Tag Selected is " + tagFilter);
        mQuestListFragment.filterListBasedOnTag(tagFilter);

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mTags.get(position));
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}
