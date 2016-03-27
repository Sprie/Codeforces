package com.axic.codeforces.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.axic.codeforces.R;
import com.axic.codeforces.fragment.Problems;
import com.axic.codeforces.fragment.ProblemsDetails;

/**
 * Created by 59786 on 2016/3/25.
 */
public class ProblemsListActivity extends Activity
    implements Problems.Callbacks{

    private Fragment mFragment;//Problemslist的fragment
    private Fragment title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.problemsactivity);
        //获取上个活动传来的数据
        Intent intent = getIntent();
        String contestId = intent.getStringExtra("contestId");
        //向Fragment传递数据
        //并设置默认的fragment
        setDefaultFragment(contestId);


    }
    private void setDefaultFragment(String contestId) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mFragment = new Problems();
        Bundle bundle = new Bundle();
        bundle.putString("contestId", contestId);
        mFragment.setArguments(bundle);
        transaction.replace(R.id.problems_content, mFragment, "problems");
//        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void getData(String contestId,String index){
        Bundle data = new Bundle();
        data.putString("contestId",contestId);
        data.putString("index", index);
        ProblemsDetails detail = new ProblemsDetails();
        detail.setArguments(data);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.add(R.id.problems_content, detail);
        transaction.addToBackStack(null);
        transaction.commit();

//        title = new ProblemListTitle().setTitle(index);
//        Log.d("index",index);
//        title.setTitle(index);


    }


}
