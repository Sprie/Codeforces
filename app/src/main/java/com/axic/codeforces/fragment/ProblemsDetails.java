package com.axic.codeforces.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.axic.codeforces.R;
import com.axic.codeforces.data.ProblemsIndex;
import com.axic.codeforces.database.ProblemsDBManager;
import com.axic.codeforces.method.CheckNet;
import com.axic.codeforces.method.GetProblemInfoFromHtml;
import com.axic.codeforces.method.GsonRequest;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

/**
 * Created by 59786 on 2016/3/25.
 */
public class ProblemsDetails extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private TextView tv;
    private String problemUrl = "http://codeforces.com/problemset/problem/";
    private String contestId;
    private String index;
    private String Url;
    private SwipeRefreshLayout mSwipe;
    private CheckNet checkNet;
    private String problemsDetails;
    private ProblemsDBManager db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new ProblemsDBManager(getActivity(), 1);
        view = inflater.inflate(R.layout.problemsdetails, container, false);
        tv = (TextView) view.findViewById(R.id.details);
        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.details_swipe);
        mSwipe.setOnRefreshListener(this);
        checkNet = new CheckNet(getActivity());

        Bundle bundle = getArguments();
        if (bundle != null) {
            contestId = bundle.getString("contestId");
            index = bundle.getString("index");
            Url = problemUrl + contestId + "/" + index;
            Log.d("url", Url);
//            GetProblemInfoFromHtml mTread = new GetProblemInfoFromHtml(Url);
//            mTread.start();
//            while (true) {
//                if (mTread.getStatus()) {
//                    tv.setText(Html.fromHtml(mTread.getHtml()));
//                    break;
//                }
        } else {
            tv.setText("error");
        }

        getDataFromNet();


        return view;
    }

    public void onRefresh() {
        tv.setText("正在刷新。。。");
//        getDataFromNet();
//        mSwipe.setRefreshing(false);

        if (checkNet.haveNet()) {
//            list.clear();

            getDataFromNet();
            mSwipe.setRefreshing(false);
        } else {
            Toast.makeText(getActivity(), "当前无网络", Toast.LENGTH_LONG).show();

            getDataFromDB();
            mSwipe.setRefreshing(false);
        }
    }

    private void getDataFromDB() {

        tv.setText(Html.fromHtml(db.getDetails(contestId, index)));
        Log.d("details", "getFromDB");
    }

    private void getDataFromNet() {
        if (db.getDetails(contestId, index) == null) {

            Log.d("details", "getFromNet");
            GetProblemInfoFromHtml mTread = new GetProblemInfoFromHtml(Url);
            mTread.start();
            while (true) {
                if (mTread.getStatus()) {
                    problemsDetails = mTread.getHtml();
                    tv.setText(Html.fromHtml(problemsDetails));
                    break;
                }
            }
            //向数据库添加数据
            db.addDetail(problemsDetails, contestId, index);
        } else {
            getDataFromDB();
        }

    }

    public void onDestroy() {

        super.onDestroy();
        db.closeDB();
    }
}
