package com.ssafy.finalpjt.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.ssafy.finalpjt.R

class SettingPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preference)
    }
}
