package com.axic.codeforces.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.axic.codeforces.R;

/**
 * Created by 59786 on 2016/3/28.
 */
public class Set extends Fragment {

    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.set, container, false);
//        view = getPersistentView(inflater, container, savedInstanceState);


        return view;
    }
}