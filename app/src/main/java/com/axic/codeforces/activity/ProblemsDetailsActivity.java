package com.axic.codeforces.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.axic.codeforces.R;
import com.axic.codeforces.fragment.Problems;
import com.axic.codeforces.fragment.ProblemsDetails;

/**
 * Created by 59786 on 2016/3/30.
 */
public class ProblemsDetailsActivity extends Activity{


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.problems_details_activity);

        //获取上个活动传来的数据
        Intent intent = getIntent();
        String problemsId = intent.getStringExtra("problemsId");
        String problemsIndex = intent.getStringExtra("problemsIndex");

        //向fragment传递数据

        Log.d("Start","ProblemsDetailsActivity");

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment pro = new ProblemsDetails();
        Bundle bundle1 = new Bundle();
        bundle1.putString("contestId", problemsId);
        bundle1.putString("index", problemsIndex);
        pro.setArguments(bundle1);
        transaction.replace(R.id.content, pro, "problems");
//        transaction.addToBackStack(null);
        transaction.commit();

    }
}
