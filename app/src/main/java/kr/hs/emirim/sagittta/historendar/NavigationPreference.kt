package kr.hs.emirim.sagittta.historendar

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat

class NavigationPreference : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.navigation_pref)
    }

}
