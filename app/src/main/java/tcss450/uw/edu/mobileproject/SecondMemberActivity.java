package tcss450.uw.edu.mobileproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class SecondMemberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2nd_main);
    }

    public void displayInfo(View view) {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    public void displayImg(View view) {
        Intent intent = new Intent(this, ImgActivity.class);
        startActivity(intent);
    }

    public void displayWeb(View view) {
        Uri webpage = Uri.parse("http://developer.android.com/index.html");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(webIntent);
    }

    public void displayToast(View view) {
        Context context = getApplicationContext();
        CharSequence text = getString(R.string.toast_mes);
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(context, text, duration).show();
    }

    public void displayDialog(View view) {
        DialogFragment newFragment = new TriggerDialog();
        newFragment.show(getSupportFragmentManager(), getString(R.string.dialog));
    }

}
