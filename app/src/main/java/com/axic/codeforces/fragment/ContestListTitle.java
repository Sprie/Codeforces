package com.axic.codeforces.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.axic.codeforces.R;
import com.axic.codeforces.activity.SearchDataFromDB;

/**
 * Created by 59786 on 2016/3/23.
 */
public class ContestListTitle extends Fragment {

    private ImageButton mSearch;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.titlefragment,container,false);
        mSearch = (ImageButton) view.findViewById(R.id.title_first_btn);
        mSearch.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                //查询按钮逻辑
                Intent intent = new Intent(getActivity(), SearchDataFromDB.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
