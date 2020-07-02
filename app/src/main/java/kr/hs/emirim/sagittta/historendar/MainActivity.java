package kr.hs.emirim.sagittta.historendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

import butterknife.BindView;

import butterknife.ButterKnife;
import kr.hs.emirim.sagittta.historendar.DB.DataAdapter;
import kr.hs.emirim.sagittta.historendar.DB.User;
import kr.hs.emirim.sagittta.historendar.Search.SearchMainActivity;
import kr.hs.emirim.sagittta.historendar.decorators.EventDecorator;
import kr.hs.emirim.sagittta.historendar.decorators.SaturdayDecorator;
import kr.hs.emirim.sagittta.historendar.decorators.OneDayDecorator;
import kr.hs.emirim.sagittta.historendar.decorators.SundayDecorator;
import kr.hs.emirim.sagittta.historendar.mypage.MypageActivity;

public class MainActivity extends AppCompatActivity implements OnDateSelectedListener {

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    @BindView(R.id.calendarView)
            MaterialCalendarView widget;

    TextView txtResult;

    private static final String TAG = "DemoActivity";
//    private static MaterialCalendarView materialCalendarView;
//    private CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);

    static CalendarDay selectedDay = null;
    static boolean Selected;

    ArrayAdapter<String> adapter;
    ArrayList<String> arrayList;

    TimePicker timePicker;
    String DATE;

    private SlidingUpPanelLayout mLayout;
    private Boolean isNavigationView = false;

    AlarmManager alarm_manager = null;
    Context context = null;
    PendingIntent pendingIntent = null;
    public List<User> userList;

    private Context mContext;
    private FloatingActionButton fab_main;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

//        처음 앱 시작 시 데이터 불러오기
        initLoadDB();
        /////////////////////////

//        캘린더
//        TODO: 사건 있을 때 날짜 옆에 점으로 표시하기
        ButterKnife.bind(this);

        widget.setTileHeightDp(70);

        final int[] months = new int[1];
        final int[] days = new int[1];

        widget.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                months[0] = date.getMonth();
                days[0] = date.getDay();
                String data = date.getYear() + " / " + months[0] + " / " + days[0];
                Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
                Log.d("jyomi", "onSelectedDayChange: " + data);
                Log.d("jyomi", "months[0]: " + months[0]);
                initLoadDB(months[0], days[0]);
            }
        });
        widget.setShowOtherDates(MaterialCalendarView.SHOW_DEFAULTS);

        widget.addDecorators(
                new SaturdayDecorator(),
                new SundayDecorator(),
                new TodayDecorator(),
                oneDayDecorator
        );

        new ApiSimulator().executeOnExecutor(Executors.newSingleThreadExecutor());

        Button searchBtn=(Button)findViewById(R.id.searchBtn);
        Button mypageHeart=(Button)findViewById(R.id.mypage);
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

//        Floating Action Button 클릭 시 발생하는 이벤트 리스너
        fab_main = (FloatingActionButton) findViewById(R.id.fab_main);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SetAlarmActivity.class);
                startActivity(intent);
            }
        });


        mypageHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, MypageActivity.class);
                startActivity(intent);
            }
        });

//        검색 버튼 클릭 시 발생하는 이벤트 리스너
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, SearchMainActivity.class);
                startActivity(intent);
            }
        });

//        드래그하면 나오는 리스트 설정


///////////////////////////////////////

        SharedPreferences sharedPreferences = getSharedPreferences("daily alarm", MODE_PRIVATE);
        long millis = sharedPreferences.getLong("nextNotifyTime", Calendar.getInstance().getTimeInMillis());

        Calendar nextNotifyTime = new GregorianCalendar();
        nextNotifyTime.setTimeInMillis(millis);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean key_use_push = prefs.getBoolean("key_use_push", true);

        if (key_use_push) {
            setPush();
        } else {
            Toast.makeText(this, "푸시알림을 받지 않음.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDateSelected(
            @NonNull MaterialCalendarView widget,
            @NonNull CalendarDay date,
            boolean selected) {
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();
    }

    private class TodayDecorator implements DayViewDecorator {
        private final CalendarDay today;
        private final Drawable backgroundDrawable;

        private TodayDecorator() {
            today = CalendarDay.today();
            backgroundDrawable = getResources().getDrawable(R.drawable.today_circle_background);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return today.equals(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.rgb(240, 171, 164)));
//            view.setBackgroundDrawable(backgroundDrawable);
        }
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LocalDate temp = LocalDate.now().minusMonths(2);
            final ArrayList<CalendarDay> dates = new ArrayList<>();
            for (int i = 0; i < 30; i++) {
                final CalendarDay day = CalendarDay.from(temp);
                dates.add(day);
                temp = temp.plusDays(5);
            }

            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            widget.addDecorator(new EventDecorator(Color.rgb(240, 171, 164), calendarDays));
        }
    }


//    public void mOnPopUpClick(View v, int year, int month, int dayOfMonth) {
//        Intent intent = new Intent(this, PopupActivity.class);
//        // Test Pop 자리에 불러온 데이터 넣어야 함.
//        String data = year + "/" + month + "/" + dayOfMonth;
//        intent.putExtra("data", data);
//        startActivityForResult(intent, 1);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                assert data != null;
                String result = data.getStringExtra("result");
                txtResult.setText(result);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_open) {
            Intent intent = new Intent(this, NavigationActivity.class);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    void setPush() {
        // 이전 설정값으로 TimePicker 초기화

        int hour_24 = 03, minute = 05;
        // 현재 지정된 시간으로 알람 시간 설정
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour_24);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        //  Preference에 설정한 값 저장
        SharedPreferences.Editor editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
        editor.putLong("nextNotifyTime", (long) calendar.getTimeInMillis());
        editor.apply();


        diaryNotification(calendar);
    }

    void diaryNotification(Calendar calendar) {

        Boolean dailyNotify = true;

        PackageManager pm = this.getPackageManager();
        ComponentName receiver = new ComponentName(this, DeviceBootReceiver.class);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (dailyNotify) {


            if (alarmManager != null) {

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }

            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

        }
//        else { //Disable Daily Notifications
//            if (PendingIntent.getBroadcast(this, 0, alarmIntent, 0) != null && alarmManager != null) {
//                alarmManager.cancel(pendingIntent);
//                //Toast.makeText(this,"Notifications were disabled",Toast.LENGTH_SHORT).show();
//            }
//            pm.setComponentEnabledSetting(receiver,
//                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                    PackageManager.DONT_KILL_APP);
//        }
    }


//    Database
//    initLoadDB(): DB 에 있는 값을 리스트에 불러오는 함수
//    앱 초기 실행 시 초기화 위해 ()와 (int months, int days) 로 나눔
//    TODO: 리스트 값에 이미지 존재 시 이미지가 존재하다는 표시하기
//    TODO: 리스트 클릭 시 이미지 보여주기
//    TODO: 겹치는 부분 분리하기
    private void initLoadDB() {
        DataAdapter mDbHelper = new DataAdapter(getApplicationContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        // db에 있는 값들을 model을 적용해서 넣는다.
        userList = mDbHelper.getNotiData();
        // db 닫기
//        Toast.makeText(this, userList.get(0).getEVENT() + "+Test", Toast.LENGTH_SHORT).show();

        ListView lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "리스트 하나 클릭함", Toast.LENGTH_SHORT).show();
            }
        });

//       List<String> history_heart_list=Arrays.asList(userList.get(0).getEVENT());

        List<String> history_heart_list=new ArrayList<>();
        Log.d("sowon", String.valueOf(userList.size()));

        for(int i=0;i<userList.size();i++){
            history_heart_list.add(userList.get(i).getEVENT());
//            Log.d("sowon", history_heart_list.toString()+"sowon"+i);
        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                history_heart_list);

        lv.setAdapter(arrayAdapter);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

//        main 화면에 보이는 부분에 나오는 텍스트 설정.
        TextView t = (TextView) findViewById(R.id.name);
        t.setText(Html.fromHtml(getString(R.string.hello)));

        mDbHelper.close();

    }

    private void initLoadDB(int months, int days) {
        DataAdapter mDbHelper = new DataAdapter(getApplicationContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        Log.d("jyomi", "월" + months);

        // db에 있는 값들을 model을 적용해서 넣는다.
        userList = mDbHelper.getNotiData(months, days);
        // db 닫기
//        Toast.makeText(this, userList.get(0).getEVENT() + "+Test", Toast.LENGTH_SHORT).show();

        ListView lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "리스트 하나 클릭함", Toast.LENGTH_SHORT).show();
            }
        });

//       List<String> history_heart_list=Arrays.asList(userList.get(0).getEVENT());

        List<String> history_heart_list=new ArrayList<>();
        Log.d("sowon", String.valueOf(userList.size()));

        for(int i=0;i<userList.size();i++){
            history_heart_list.add(userList.get(i).getEVENT());
//            Log.d("sowon", history_heart_list.toString()+"sowon"+i);
        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                history_heart_list);

        lv.setAdapter(arrayAdapter);

        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

//        main 화면에 보이는 부분에 나오는 텍스트 설정.
        TextView t = (TextView) findViewById(R.id.name);
        t.setText(Html.fromHtml(getString(R.string.hello)));

        mDbHelper.close();

    }

}
