package com.ssafy.finalpjt

import android.os.Bundle
import android.preference.PreferenceFragment

class settingPreferenceFragment constructor() : PreferenceFragment() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings_preference)
    }
}