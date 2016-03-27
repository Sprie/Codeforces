package com.axic.codeforces.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.axic.codeforces.R;

/**
 * Created by 59786 on 2016/3/25.
 */
public class ProblemListTitle extends Fragment {

    private ImageButton mBack;
    private TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.problemslisttitle, container, false);
        mBack = (ImageButton) view.findViewById(R.id.title_back);
        title = (TextView) view.findViewById(R.id.problemslisttitle);
        mBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //获取返回栈的值；若无fragment，则结束Activity
                //否则返回上一个fragment
                if (getFragmentManager().getBackStackEntryCount() == 0) {
                    getActivity().finish();
                } else {
                    getFragmentManager().popBackStack();
                }
            }
        });
        return view;
    }
//    public void  setTitle(String str){
//        title.setText(str);
//    }
}
