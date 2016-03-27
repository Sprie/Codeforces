package com.axic.codeforces.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.axic.codeforces.R;

/**
 * Created by 59786 on 2016/3/23.
 */
public class ContestListTitle extends Fragment {

    private ImageButton mLeftMenu;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.titlefragment,container,false);
        mLeftMenu = (ImageButton) view.findViewById(R.id.title_first_btn);
        mLeftMenu.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                //标题图片点击事件
                Toast.makeText(getActivity(),
                        "i am an ImageButton in TitleFragment!",
                        Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
