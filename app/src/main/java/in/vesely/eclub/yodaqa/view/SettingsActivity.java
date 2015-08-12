package in.vesely.eclub.yodaqa.view;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import in.vesely.eclub.yodaqa.R;

/**
 * Created by ERMRK on 12.8.2015.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}