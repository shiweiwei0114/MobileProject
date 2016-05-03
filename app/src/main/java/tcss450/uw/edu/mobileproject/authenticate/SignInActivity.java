package tcss450.uw.edu.mobileproject.authenticate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tcss450.uw.edu.mobileproject.HomeActivity;
import tcss450.uw.edu.mobileproject.R;

public class SignInActivity extends AppCompatActivity implements LoginFragment.LoginInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new LoginFragment()).commit();
    }

    @Override
    public void login(String userId, String pwd) {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }
}
