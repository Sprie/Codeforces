package com.axic.codeforces.activity;


import android.app.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import com.axic.codeforces.fragment.ContestList;
import com.axic.codeforces.fragment.Home;
import com.axic.codeforces.fragment.Problems;
import com.axic.codeforces.method.GetProblemInfoFromHtml;
import com.axic.codeforces.R;
import com.google.gson.Gson;


public class MainActivity extends Activity implements View.OnClickListener {

    RequestQueue mQueue;
    String url = "http://codeforces.com/problemset/problem/630/Q";
    String contestFalseUrl = "http://codeforces.com/api/contest.list?gym=false";
    GetProblemInfoFromHtml mTread;
    Gson gson;
    TextView mTextView;

    private ContestList mContextList;
    private Home mHome;

    private LinearLayout mTabContestList;
    private LinearLayout mTabHome;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //设置fragment
        mTabContestList = (LinearLayout) findViewById(R.id.tab_contestList);
        mTabHome = (LinearLayout) findViewById(R.id.tab_home);
        mTabContestList.setOnClickListener(this);
        mTabHome.setOnClickListener(this);
        //设置默认的fragment
        setDefaultFragment();

//        mContextList.GetContestListFromAPI();
    }



    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
//        mContextList = new ContestList();
//        transaction.replace(R.id.id_content, mContextList);
        mHome = new Home();
        transaction.replace(R.id.id_content,mHome);
        transaction.commit();
    }

    //bottombar图标的点击事件
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        //开启Fragment事物
        FragmentTransaction transaction = fm.beginTransaction();

        switch (v.getId()) {
//            case R.id.show:
//                mTextView.setText(Html.fromHtml(mTread.getHtml()));
//                break;
            case R.id.tab_contestList:
                if (mContextList == null) {
                    mContextList = new ContestList();
                }
                //使用当前Fragment的布局替代id_content的控件
                transaction.replace(R.id.id_content, mContextList);
                break;
            case R.id.tab_home:
                if (mHome == null) {
                    mHome = new Home();
                }
                transaction.replace(R.id.id_content, mHome);
                break;

            default:
                break;
        }
        transaction.commit();
    }



}
