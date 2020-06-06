package kr.hs.emirim.sagittta.historendar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
//import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.annotations.NonNls
import android.app.AlarmManager
import android.widget.Toast
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.app.ComponentActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.util.*
import android.content.pm.PackageManager
import android.os.Build
import android.content.ComponentName
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.content.SharedPreferences;
import android.preference.PreferenceManager
import android.view.View
import androidx.core.widget.NestedScrollView
import com.sothree.slidinguppanel.ScrollableViewHelper
import java.util.Calendar;
import java.util.GregorianCalendar;
import kotlin.reflect.typeOf


class MainActivity1 : AppCompatActivity() {

    private var isNavigationView = false

    var alarm_manager: AlarmManager? = null
    var context: Context? = null
    var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolbar()

///////////////////////////////////////////////////////////////////////////////////////////

        var prefs = PreferenceManager.getDefaultSharedPreferences(this)
        var key_use_push = prefs.getBoolean("key_use_push", true)

        if (key_use_push) {
            setPush()

            Toast.makeText(this, "setPush() 사용", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "setPush() 사용하지 않음", Toast.LENGTH_SHORT).show()
        }

    }


    private fun setPush() {
        val sharedPreferences = getSharedPreferences("daily alarm", MODE_PRIVATE)
        val millis =
            sharedPreferences.getLong("nextNotifyTime", Calendar.getInstance().getTimeInMillis())

        val nextNotifyTime = GregorianCalendar()
        nextNotifyTime.setTimeInMillis(millis)


        val hour: Int
        val hour_24 = 23
        val minute = 37

        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis(System.currentTimeMillis())
        calendar.set(Calendar.HOUR_OF_DAY, hour_24)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
        }

        //  Preference에 설정한 값 저장
        val editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit()
        editor.putLong("nextNotifyTime", calendar.getTimeInMillis() as Long)
        editor.apply()


        diaryNotification(calendar)
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_hbg)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, NavigationActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun diaryNotification(calendar: Calendar) {

        val dailyNotify = true

        val pm = this.packageManager
        val receiver = ComponentName(this, DeviceBootReceiver::class.java)
        val alarmIntent = Intent(this, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (dailyNotify) {


            if (alarmManager != null) {

                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY, pendingIntent
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            }

            pm.setComponentEnabledSetting(
                receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )

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
}
