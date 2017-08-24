package moe.yukisora.solidot.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.Html;
import android.widget.TextView;

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

        findPreference("license").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.license));
                builder.setMessage(Html.fromHtml(getResources().getString(R.string.licenses)));
                AlertDialog alert = builder.create();
                alert.show();
                ((TextView)alert.findViewById(android.R.id.message)).setTextSize(10);

                return true;
            }
        });
    }
}
