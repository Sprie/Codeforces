package com.axic.codeforces.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axic.codeforces.R;
import com.axic.codeforces.activity.AboutApp;

/**
 * Created by 59786 on 2016/3/28.
 */
public class Set extends Fragment implements View.OnClickListener {

    private TextView aboutApp;
    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.set, container, false);
//        view = getPersistentView(inflater, container, savedInstanceState);
        aboutApp = (TextView) view.findViewById(R.id.about_app);
        aboutApp.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.about_app:
                Intent intent = new Intent(getActivity(), AboutApp.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


}