/*
package com.scroll.owner.sparkle.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import R;

*/
/**
 * Created by Jewel on 1/26/2016.
 *//*

public class FragNewSongList extends Fragment {

    private View view;
    private LinearLayout mainLay;
    LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lay_test, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        prepareList();
    }
    private void init(){
        mainLay= (LinearLayout) view.findViewById(R.id.lay_main_content);
        inflater=LayoutInflater.from(getActivity());
        mainLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.d("Jewel",v.getId()+" id");
                    if(mainLay.findViewById(2).getVisibility()==View.GONE)
                        mainLay.findViewById(2).setVisibility(View.VISIBLE);
                else
                        mainLay.findViewById(2).setVisibility(View.GONE);




            }
        });
    }
    private void prepareList(){
        mainLay.addView(inflater.inflate(R.layout.row_song_list_music_player,null));
        mainLay.addView(makeTextView("Test 1",1));

        mainLay.addView(inflater.inflate(R.layout.row_song_list_music_player,null));
        mainLay.addView(makeTextView("Test 2",2));

        mainLay.addView(inflater.inflate(R.layout.row_song_list_music_player,null));
        mainLay.addView(makeTextView("Test 3",3));
    }
    private TextView makeTextView(String title,int id){
        TextView tv=new TextView(getActivity());
        tv.setText(title);
        tv.setVisibility(View.GONE);
        tv.setTag("a");
        tv.setId(id);
        return tv;
    }

}
*/
