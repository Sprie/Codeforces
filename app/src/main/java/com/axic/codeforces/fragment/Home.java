package com.axic.codeforces.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axic.codeforces.R;

/**
 * Created by 59786 on 2016/3/27.
 */
public class Home extends Fragment{
    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homepage, container, false);
//        view = getPersistentView(inflater, container, savedInstanceState);


        return view;
    }


//    private View getPersistentView(LayoutInflater inflater, ViewGroup container,
//                                   Bundle savedInstanceState) {
//        if (view == null) {
//            view = inflater.inflate(R.layout.homepage, container, false);
//        } else {
//            ((ViewGroup) view.getParent()).removeView(view);
//        }
//
//        return view;
//    }
}
