package com.axic.codeforces.activity;


import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import com.axic.codeforces.fragment.ContestList;
import com.axic.codeforces.fragment.Home;
import com.axic.codeforces.fragment.Set;
import com.axic.codeforces.method.GetProblemInfoFromHtml;
import com.axic.codeforces.R;
import com.google.gson.Gson;


public class MainActivity extends Activity implements View.OnClickListener {

    String contestFalseUrl = "http://codeforces.com/api/contest.list?gym=false";

    private ContestList mContestList;
    private Home mHome;
    private Set mSet;
    private LinearLayout mTabSet;
    private LinearLayout mTabContestList;
    private LinearLayout mTabHome;

private TextView mTextHome;
    private TextView mTextSet;
    private TextView mTextContest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //设置fragment
        mTabContestList = (LinearLayout) findViewById(R.id.tab_contestList);
        mTabHome = (LinearLayout) findViewById(R.id.tab_home);
        mTabSet = (LinearLayout) findViewById(R.id.tab_set);
        mTabContestList.setOnClickListener(this);
        mTabHome.setOnClickListener(this);
        mTabSet.setOnClickListener(this);

        mTextHome = (TextView) findViewById(R.id.tv_home);
        mTextContest = (TextView) findViewById(R.id.tv_contest);
        mTextSet = (TextView) findViewById(R.id.tv_set);
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
//        mTabHome.setBackgroundColor(0xff111654);
        mTextSet.setTextColor(0xff111111);
        mTextContest.setTextColor(0xff111111);
        mTextHome.setTextColor(0xffa9c734);
        transaction.replace(R.id.id_content, mHome);
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
//                if (mContestList == null) {
//                    mContestList = new ContestList();
//                }
                //使用当前Fragment的布局替代id_content的控件
//                transaction.replace(R.id.id_content, mContestList,"contest");
                showFragment("contest");
                break;
            case R.id.tab_home:
//                if (mHome == null) {
//                    mHome = new Home();
//                }
//                transaction.replace(R.id.id_content, mHome,"");
                showFragment("home");
                break;
            case R.id.tab_set:
//                if (mSet == null) {
//                    mSet = new Set();
//                }
//                transaction.replace(R.id.id_content, mSet, "set");
                showFragment("set");
                break;
            default:
                break;
        }
//        transaction.commit();
    }


    public void showFragment(String str) {

        FragmentManager fm = getFragmentManager();
        //开启Fragment事物
        FragmentTransaction transaction = fm.beginTransaction();
        //逻辑
        if (str.equals("home")) {
            //控制颜色的显示
//            mTabHome.setBackgroundColor(0xff111654);
//            mTabSet.setBackgroundColor(0xffffffff);
//            mTabContestList.setBackgroundColor(0xffffffff);
            mTextSet.setTextColor(0xff111111);
            mTextContest.setTextColor(0xff111111);
            mTextHome.setTextColor(0xffa9c734);
            //控制fragment的显示
            if (mHome == null) {
                mHome = new Home();
                transaction.add(R.id.id_content,mHome, "home");
                Log.d("new", "home");
            } else {
                transaction.show(mHome);
                Log.d("show", "home");
            }
//            transaction.show(mHome);
            if (mSet != null) {
                transaction.hide(mSet);
                Log.d("hide", "set");
            }
            if (mContestList != null) {
                transaction.hide(mContestList);
                Log.d("hide", "contest");
            }
        } else if (str.equals("set")) {
//            mTabHome.setBackgroundColor(0xffffffff);
//            mTabSet.setBackgroundColor(0xff111654);
//            mTabContestList.setBackgroundColor(0xffffffff);
            mTextSet.setTextColor(0xffa9c734);
            mTextContest.setTextColor(0xff111111);
            mTextHome.setTextColor(0xff111111);
            //控制fragment的显示
            if (mSet == null) {
                mSet = new Set();
                transaction.add(R.id.id_content,mSet, "set");
                Log.d("new", "set");
            } else {
                transaction.show(mSet);
                Log.d("show", "set");
            }
//            Log.d("", "");
            if (mHome != null) {
                transaction.hide(mHome);
                Log.d("hide", "home");
            }
            if (mContestList != null) {
                transaction.hide(mContestList);
                Log.d("hide", "contest");
            }
        } else if (str.equals("contest")) {
//            mTabHome.setBackgroundColor(0xffffffff);
//            mTabSet.setBackgroundColor(0xffffffff);
//            mTabContestList.setBackgroundColor(0xff111654);
            mTextSet.setTextColor(0xff111111);
            mTextContest.setTextColor(0xffa9c734);
            mTextHome.setTextColor(0xff111111);
            //控制fragment的显示
            if (mContestList == null) {
                mContestList = new ContestList();
                transaction.add(R.id.id_content,mContestList, "contest");
                Log.d("new", "contest");
            } else {
                transaction.show(mContestList);
                Log.d("show", "contest");
            }
            if (mSet != null) {
                transaction.hide(mSet);
                Log.d("hide", "set");
            }
            if (mHome != null) {
                transaction.hide(mHome);
                Log.d("hide", "home");
            }
        }

        transaction.commit();
    }


}
