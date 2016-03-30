package com.axic.codeforces.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.axic.codeforces.R;
import com.axic.codeforces.database.ProblemsDBManager;
import com.axic.codeforces.method.CheckNet;
import com.axic.codeforces.method.GetProblemInfoFromHtml;
import com.axic.codeforces.method.MyTextView;


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
    private ProblemsDBManager db;
    private GetProblemInfoFromHtml getProblemInfoFromHtml;
    private String html = "error";
    boolean status = true;


//    private Handler handler = new Handler() {
//        public void handleMassage(Message msg){
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 0x110:
//                    tv.setText("comming ");
//                    Log.d("message","update");
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new ProblemsDBManager(getActivity(), 1);
        view = inflater.inflate(R.layout.problemsdetails, container, false);
        tv = (TextView) view.findViewById(R.id.details);

        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.details_swipe);
        mSwipe.setColorSchemeResources(android.R.color.black);
        mSwipe.setOnRefreshListener(this);
        checkNet = new CheckNet(getActivity());

        tv.setText("Loading...");

        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Log.d("onLongClick","true");
                return false;
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            contestId = bundle.getString("contestId");
            index = bundle.getString("index");
            Url = problemUrl + contestId + "/" + index;
            Log.d("url", Url);


        } else {
            tv.setText("error");
        }
        getProblemInfoFromHtml = new GetProblemInfoFromHtml(Url);

        getDataFromNet();


        return view;
    }

    public void onRefresh() {
        tv.setText("Loading...");
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
            //创建新线程获取ProblemDetails并显示到textView中
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("comingNewThread", "coming");
                    status = getProblemInfoFromHtml.run();
                    if (status) {
                        Log.d("status", status + "...");
                        html = getProblemInfoFromHtml.getHtml();
                        //test
//                        Log.d("header",getProblemInfoFromHtml.getHeaderData());


                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(Html.fromHtml(html));
                                Log.d("In ", "Ui");
                            }
                        });

                        //向数据库添加数据
                        db.addDetail(html, contestId, index);
                    } else {
                        Log.d("status", status + "--");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText("获取失败，请稍后重试");

//                                Toast.makeText(getActivity(),"服务器正在开小差~请稍候重试 >-<",Toast.LENGTH_LONG).show();
                                Log.d("In ", "Ui");
                            }
                        });
                    }
                    mSwipe.setRefreshing(false);
                }
            }).start();

        } else {
            getDataFromDB();
        }

    }

    public void onDestroy() {

        super.onDestroy();
        db.closeDB();
    }
}
