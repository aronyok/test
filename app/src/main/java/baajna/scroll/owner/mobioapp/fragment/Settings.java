package baajna.scroll.owner.mobioapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobioapp.baajna.R;

public class Settings extends Fragment {
    public static int SectionNumber = 0;
    private View view;
    TextView settings_versionInfoTextView;
    TextView settings_terms_and_serviceTextView;
    TextView settings_policyTextView;
    TextView settings_licenseTextView;
    TextView settings_mobileTermsTextView;
    TextView settings_changePwdTextView;
    TextView setings_logoutTextView;


    public static Settings newInstance(int sectionNumber) {
        Settings fragment = new Settings();
        SectionNumber = sectionNumber;
        return fragment;
    }

    public Settings() {
        //required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_settings, container, false);

        initViews();
        setListener();
        loadData();


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(getTag(), "keyCode: " + keyCode);
                return true;
            }

        });
        // Inflate the layout for this fragment
        return view;
    }

    private void initViews() {
        settings_versionInfoTextView = (TextView) view.findViewById(R.id.settings_versionInfo);
        settings_terms_and_serviceTextView = (TextView) view.findViewById(R.id.terms_and_service);
        settings_policyTextView = (TextView) view.findViewById(R.id.settings_policy);
        settings_licenseTextView = (TextView) view.findViewById(R.id.settings_policy);
        settings_mobileTermsTextView = (TextView) view.findViewById(R.id.settings_mobileTerms);
        /*settings_changePwdTextView = (TextView) view.findViewById(R.id.settings_changePwd);
        setings_logoutTextView = (TextView) view.findViewById(R.id.setings_logout);*/
    }

    private void setListener() {


        settings_versionInfoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, Version.newInstance(7))
                        .commit();
            }
        });


    }

    private void loadData() {

    }


}