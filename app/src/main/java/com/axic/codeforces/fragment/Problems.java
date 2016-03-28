package com.axic.codeforces.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.axic.codeforces.R;
import com.axic.codeforces.data.ProblemsIndex;
import com.axic.codeforces.database.ProblemsDBManager;
import com.axic.codeforces.method.CheckNet;
import com.axic.codeforces.method.GsonRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 59786 on 2016/3/23.
 */
public class Problems extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;
    //    View mainView;
    private RequestQueue mQueue;
    private String ProblemUrl = "http://codeforces.com/api/contest.standings";
    private ListView listView;
    private String contestId;
    private String url;
    private SwipeRefreshLayout mSwipe;
    boolean dbstatus = true;//查看数据库是否有该contest的problems列表，

    private ProblemsDBManager db;

    TextView tv;
    private CheckNet CheckNet;

    private Callbacks mCallbacks;

    //定义一个回调接口
    public interface Callbacks {
        public void getData(String contestId, String index);
    }

    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new ProblemsDBManager(getActivity(), 1);
        CheckNet = new CheckNet(getActivity());

        //获取Activity传来的数据
        final Bundle bundle = getArguments();
        if (bundle != null) {
            contestId = bundle.getString("contestId");
        } else {
            contestId = "600";
        }

        url = ProblemUrl + "?" + "contestId=" + contestId + "&" +
                "from=1&count=1&showUnofficial=true";


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mQueue = Volley.newRequestQueue(getActivity());
        view = inflater.inflate(R.layout.problemslist, container, false);
        listView = (ListView) view.findViewById(R.id.problemslistview);

        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.problems_swipe);
        mSwipe.setOnRefreshListener(this);
        mSwipe.setColorSchemeResources(android.R.color.holo_red_light);

        tv = (TextView) view.findViewById(R.id.contest_id);

        tv.setText(url);
//        if (db.getDataFromDBById(contestId) == null) {
//            getDataFromNet();
//        } else {
//
//            //获取数据库数据并显示
//            Log.d("nonewdata", "nonewdata");
//            Log.d("getData", "FromDB");
//            getDataFromDB();
//        }

        getDataFromNet();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("id", id + "");
                //获取选中项的数据,传给problemslistActivity
                HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(position);
                String index = map.get("index");

                Fragment pro = new ProblemsDetails();
                Bundle bundle1 = new Bundle();
                bundle1.putString("contestId", contestId);
                bundle1.putString("index", index);
                pro.setArguments(bundle1);
                mCallbacks.getData(contestId, index);
            }
        });


        return view;
    }

    public void onRefresh() {
//        if (db.helper.dbStatus) {
//            list.clear();
//            getDataFromNet();
//            mSwipe.setRefreshing(false);
//        } else {
//            getDataFromDB();
//        }
        if (CheckNet.haveNet()) {
//            list.clear();

            getDataFromNet();
//            mSwipe.setRefreshing(false);
        } else {
            Toast.makeText(getActivity(), "当前无网络", Toast.LENGTH_LONG).show();
            getDataFromDB();
            mSwipe.setRefreshing(false);
        }
    }

    private void getDataFromDB() {
        List<ProblemsIndex.ResultBean.ProblemsBean> ctList = db.getDataFromDBById(contestId);

        ArrayList<HashMap<String, String>> problemsList = new ArrayList<HashMap<String, String>>();

        for (ProblemsIndex.ResultBean.ProblemsBean rst : ctList) {
            //获取数据库的数据
            String name = rst.getName();
            String id = rst.getContestId();
            String points = rst.getPoints();
            String type = rst.getType();
            String index = rst.getIndex();
            String tags = rst.getStrTags();

//            Log.d("tags", tags);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", name);
            map.put("index", index);
//            map.put("points", points);
//            map.put("type", type);
            map.put("tags", tags);
            problemsList.add(map);
        }

        SimpleAdapter listAdapter = new SimpleAdapter(getActivity(), problemsList, R.layout.poblemslistview,
                new String[]{"name", "index", "tags"},
                new int[]{R.id.problemName, R.id.problemIndex, R.id.problemTags});

        listView.setAdapter(listAdapter);
        Log.d("right", "right");
    }

    private void getDataFromNet() {
//        if (db.getDataFromDBById(contestId) == null) {
//            getDataFromNet();
//        } else {
//
//            //获取数据库数据并显示
//            Log.d("nonewdata", "nonewdata");
//            Log.d("getData", "FromDB");
//            getDataFromDB();
//        }
        if (db.getDataFromDBById(contestId) == null) {

            //从传来的url联网获取数据
            GsonRequest<ProblemsIndex> GsonRequest = new GsonRequest<ProblemsIndex>(
                    url, ProblemsIndex.class,
                    new Response.Listener<ProblemsIndex>() {
                        @Override
                        public void onResponse(ProblemsIndex mProblem) {
                            List<ProblemsIndex.ResultBean.ProblemsBean> ctList = mProblem.getResult().getProblems();

                            //
//                        int dataSize = db.getDataFromDBById().size();

                            dbstatus = true;
                            Log.d("news", "新数据");
//                        list.clear();
                            for (ProblemsIndex.ResultBean.ProblemsBean rst : ctList) {
                                String index = rst.getIndex();
                                String name = rst.getName();
                                String type = rst.getType();
                                String points = rst.getPoints();
                                String tags;

                                List<String> tag = rst.getTags();
                                if (tag.size() > 0) {
                                    tags = tag.get(0);
                                    for (int i = 1; i < tag.size(); i++) {
                                        tags = tags + "、" + tag.get(i);
                                    }
                                } else {
                                    tags = "null";
                                }
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("name", name);
                                map.put("index", index);
                                map.put("tags", tags);

                                list.add(map);
                            }
                            SimpleAdapter listAdapter = new SimpleAdapter(getActivity(), list, R.layout.poblemslistview,
                                    new String[]{"name", "index", "tags"},
                                    new int[]{R.id.problemName, R.id.problemIndex, R.id.problemTags});

                            listView.setAdapter(listAdapter);

                            if (dbstatus) {
                                //添加数据到数据库
                                db.add(ctList);
                                dbstatus = false;

                            }

                            mSwipe.setRefreshing(false);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TErr", error.getMessage(), error);
                    Toast.makeText(getActivity(), "服务器正在开小差~请稍候重试 >-<", Toast.LENGTH_LONG).show();
                    mSwipe.setRefreshing(false);
                }
            });

            GsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));

            mQueue.add(GsonRequest);
        } else {
            getDataFromDB();
            mSwipe.setRefreshing(false);
        }
    }

    public void onDestroy() {

        super.onDestroy();
//        db.closeDB();
    }
}
