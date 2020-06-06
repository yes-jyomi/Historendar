package kr.hs.emirim.sagittta.historendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class NavigationActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NavigationActivity.this, MainActivity.class);
        startActivity(intent);

        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
    }
}
