package moe.yukisora.solidot.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import moe.yukisora.solidot.R;

public class AboutFragment extends PreferenceFragment {
    public static AboutFragment newInstance() {
        Bundle args = new Bundle();
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_about);
    }
}
