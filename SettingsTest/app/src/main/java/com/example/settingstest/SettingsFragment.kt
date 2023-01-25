package com.example.settingstest

import android.os.Bundle
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        Toast.makeText(context, "These are your settings", Toast.LENGTH_SHORT).show()
    }
}