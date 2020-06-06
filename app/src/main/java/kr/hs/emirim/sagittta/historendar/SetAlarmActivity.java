package kr.hs.emirim.sagittta.historendar;

import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class SetAlarmActivity extends AppCompatActivity {

    private Fragment cameraFragment;
    private Fragment galleryFragment;

    TimePicker mTimepicker;
    Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);

        mTimepicker = (TimePicker) findViewById(R.id.TimePicker);
        mCalendar = Calendar.getInstance();

        int hour, min;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hour = mTimepicker.getHour();
            min = mTimepicker.getMinute();
        } else {
            hour = mTimepicker.getCurrentHour();
            min = mTimepicker.getCurrentMinute();
        }
    }
}
