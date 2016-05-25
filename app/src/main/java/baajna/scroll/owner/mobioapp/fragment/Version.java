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

public class Version extends Fragment {

    public static int SectionNumber = 0;
    private View view;
    private TextView infoTextView;

    public static Version newInstance(int sectionNumber) {
        Version fragment = new Version();
        SectionNumber = sectionNumber;
        return fragment;
    }

    public Version() {
        //required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ((MainActivity) getActivity()).setOnBackPressedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings_info, container, false);

        initViews();
        setListener();
        loadData();


        return view;
    }



    private void initViews() {
        infoTextView = (TextView) view.findViewById(R.id.txt_info);
    }

    private void setListener() {

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(getTag(), "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, Settings.newInstance(7))
                            .commit();
                    Log.i(getTag(), "onKey Back listener is working!!!");
                    return true;
                } else {
                    return false;
                }
            }

        });

    }

    private void loadData() {
        infoTextView.setText("Version 1.0.3");
    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        ((MainActivity) getActivity()).setOnBackPressedListener(this);
//    }

//    @Override
//    public void doBack() {
////
//        Intent intent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
//        startActivity(intent);
////
//    }
}