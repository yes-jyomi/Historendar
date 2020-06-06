package kr.hs.emirim.sagittta.historendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class PopupActivity extends AppCompatActivity {

    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

//        txt = (TextView) findViewById(R.id.txt);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        txt.setText(data);
    }

    public void mOnClose(View v) {
        Intent intent = new Intent();
        intent.putExtra("result", "Close PopUp");
        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        } else {
            return true;
        }
    }

    // 백 버튼 막음.
    @Override
    public void onBackPressed() {
        return;
    }
}
