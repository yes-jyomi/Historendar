package kr.hs.emirim.sagittta.historendar

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class CheckAlarmPreference : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.alarm_pref)
    }

}
