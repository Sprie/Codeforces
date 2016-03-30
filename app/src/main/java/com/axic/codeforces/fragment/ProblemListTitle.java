package com.axic.codeforces.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.PowerManager;
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

    //控制屏幕常亮
    private TextView screenBright;
    PowerManager powerManager = null;
    PowerManager.WakeLock wakeLock = null;
    int tag = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.problemslisttitle, container, false);
        mBack = (ImageButton) view.findViewById(R.id.title_back);
        title = (TextView) view.findViewById(R.id.problemslisttitle);

        powerManager = (PowerManager)getActivity().getSystemService(getActivity().POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Lock");
        screenBright = (TextView) view.findViewById(R.id.Screen_Bright);
        screenBright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击常亮并字体变亮
                //第一次点击，常亮
                if(tag%2==0){
                    wakeLock.acquire();
                    Toast.makeText(getActivity(),"Screen bright",Toast.LENGTH_SHORT).show();
//                    screenBright.setTextColor(0xffffffff);
                    screenBright.setBackgroundResource(R.drawable.screen_bright_light);
                }else{
                    //释放
                    screenBright.setBackgroundResource(R.drawable.screen_bright_release);
                    wakeLock.release();
                    Toast.makeText(getActivity(),"Release",Toast.LENGTH_SHORT).show();

//                    screenBright.setTextColor(0x88ffffff);
                }
                tag++;
            }
        });
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
