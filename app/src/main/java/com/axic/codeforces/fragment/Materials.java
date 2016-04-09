package com.axic.codeforces.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axic.codeforces.R;

/**
 * Created by 59786 on 2016/4/9.
 * 获取contest Materials数据
 */
public class Materials extends Fragment {
    private View view;
    private String contestId;
    private String Url;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.materialsfragment, container, false);
        Log.d("materials","comingMaterialsFragment");
        Bundle bundle = getArguments();
        if (bundle != null) {
            contestId = bundle.getString("contestId");
//            index = bundle.getString("index");
            Url = "codeforces.com/" + contestId;
            Log.d("url", Url);


        } else {
            Log.d("url","null");
//            tv.setText("error");
        }
        return view;
    }
}
