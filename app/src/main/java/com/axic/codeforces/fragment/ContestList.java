package com.axic.codeforces.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.axic.codeforces.R;
import com.axic.codeforces.activity.ProblemsListActivity;
import com.axic.codeforces.database.ContestDBManager;
import com.axic.codeforces.data.contestFalse;
import com.axic.codeforces.method.GsonRequest;
import com.axic.codeforces.method.CheckNet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 59786 on 2016/3/23.
 */
public class ContestList extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private RequestQueue mQueue;
    private String contestFalseUrl = "http://codeforces.com/api/contest.list?gym=false";
    private ListView listView;
    private SwipeRefreshLayout mSwipeLayout;
    private SimpleAdapter listAdapter;

    private ContestDBManager db;
    boolean status = true;//第一次运行从net获取数据，之后从数据库获取；
    boolean dbstatus = true;//第一次运行加入数据库，刷新获取新数据进行比较，不同则更新数据

    private CheckNet checkNet;


    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contentfragment, container, false);
//        view = getPersistentView(inflater, container, savedInstanceState);
        db = new ContestDBManager(getActivity(), 1);
        checkNet = new CheckNet(getActivity());

        listView = (ListView) view.findViewById(R.id.contentListview);

        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_green_dark);
        Log.d("start", "startt");
        if (db.helper.dbStatus) {
            getDataFromNet();
            status = false;
            db.helper.dbStatus = false;
            Log.d("getdatafromnet", "null");
        } else {
            Log.d("getdtatfromdb", "null");
            getDataFromDB();

        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()

        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("id", id + "");
                //获取选中项的数据,传给problemslistActivity
                HashMap<String, String> map = (HashMap<String, String>) listView.getItemAtPosition(position);
                String contestId = map.get("id");
                Log.d("getid", contestId);

                Intent mIntent = new Intent(getActivity(), ProblemsListActivity.class);
                mIntent.putExtra("contestId", contestId);
                startActivity(mIntent);
            }
        });

        return view;
    }

    private void getDataFromDB() {
        List<contestFalse.ResultBean> ctList = db.getDataFromDB();

        ArrayList<HashMap<String, String>> contestList = new ArrayList<HashMap<String, String>>();

        for (contestFalse.ResultBean rst : ctList) {
            String name = rst.getName();
            String id = rst.getId();
            String phase = rst.getPhase();
            Log.d("TAG", id + "," + name);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("name", name);
            map.put("id", id);
            map.put("phase", phase);
            contestList.add(map);
        }

        SimpleAdapter listAd = new SimpleAdapter(getActivity(), contestList, R.layout.listview,
                new String[]{"name", "id", "phase"}, new int[]{R.id.contestName, R.id.contestId, R.id.contestPhase});

        listView.setAdapter(listAd);
        Log.d("right", "right");

    }


//    private View getPersistentView(LayoutInflater inflater, ViewGroup container,
//                                   Bundle savedInstanceState) {
//        if (view == null) {
//            view = inflater.inflate(R.layout.contentfragment, container, false);
//        } else {
//            ((ViewGroup) view.getParent()).removeView(view);
//        }
//
//        return view;
//    }

    public void onRefresh() {
        if (checkNet.haveNet()) {
//            list.clear();

            getDataFromNet();
            mSwipeLayout.setRefreshing(false);
        } else {
            Toast.makeText(getActivity(), "当前无网络", Toast.LENGTH_LONG).show();
            getDataFromDB();
            mSwipeLayout.setRefreshing(false);
        }
    }

    public void getDataFromNet() {

        mQueue = Volley.newRequestQueue(getActivity());


        GsonRequest<contestFalse> GsonRequest = new GsonRequest<contestFalse>(
                contestFalseUrl, contestFalse.class,
                new Response.Listener<contestFalse>() {
                    @Override
                    public void onResponse(contestFalse mContest) {

                        List<contestFalse.ResultBean> ctList = mContest.getResult();
                        //若获取的列表size比数据库中的多，则更新数据库
                        //若比赛的phase改变也许要更新数据库
                        int dataSize = db.getDataFromDB().size();
                        if (ctList.size() > dataSize) {
//                                dbstatus = true;
                            Log.d("news", "新数据");


                            for (contestFalse.ResultBean rst : ctList) {
                                String name = rst.getName();
                                String id = rst.getId();
                                String phase = rst.getPhase();
                                String type = rst.getType();
//                                Log.d("TAG", id + "," + name);
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("name", name);
                                map.put("id", id);
                                map.put("type", type);
                                map.put("phase", phase);
                                list.add(map);
                            }

                            listAdapter = new SimpleAdapter(getActivity(), list, R.layout.listview,
                                    new String[]{"name", "id", "phase"}, new int[]{R.id.contestName, R.id.contestId, R.id.contestPhase});

                            listView.setAdapter(listAdapter);
                            Log.d("getData","FromNet");

                            //若不是第一次创建数据库，且数据有更新，则将更新的数据add到数据库
                            if (!dbstatus) {

                                contestFalse.ResultBean newResult = new contestFalse.ResultBean();
                                List<contestFalse.ResultBean> newData = new ArrayList<contestFalse.ResultBean>();
                                //获取更新的数据
                                for (dataSize++; dataSize <= ctList.size(); dataSize++) {
                                    String name = ctList.get(dataSize).getName();
                                    String id = ctList.get(dataSize).getId();
                                    String phase = ctList.get(dataSize).getPhase();
                                    String type = ctList.get(dataSize).getType();

                                    newResult.setName(name);
                                    newResult.setPhase(phase);
                                    newResult.setType(type);
                                    newResult.setId(id);

                                    newData.add(newResult);
                                }
                                db.add(newData);


                            }

                            if (dbstatus) {
                                //添加数据到数据库
                                db.add(ctList);
                                dbstatus = false;
                                Log.d("adddatatodb", "added");
                            }
                        } else if (ctList.size() == dataSize) {
                            //判断phase值有没有变化，有变化则更新
                            contestFalse.ResultBean newResult = new contestFalse.ResultBean();
                            boolean dataStatue = false;
                            //获取服务器数据
                            for (int i = 0; i <= 15; i++) {
//                                String name = ctList.get(i).getName();
                                String id = ctList.get(i).getId();
                                String phase = ctList.get(i).getPhase();
//                                String type = ctList.get(i).getType();
                                //判断是是否与数据库中的phase值相同
                                //不同则更新
//                                Log.d("phase","right");
                                if (!db.getPhaseById(id).equals(phase)) {
                                    db.updatePhase(id, phase);
//                                    Log.d("phase",phase);
//                                    Log.d("phase",db.getPhaseById(id));
                                    dataStatue = true;

                                }else{
                                    dataStatue = false;
                                }


                            }
                            //获取数据库数据并显示
                            Log.d("nonewdata", "nonewdata");
                            Log.d("getData", "FromDB");
                            getDataFromDB();


                            if(dataStatue){
                                Toast.makeText(getActivity(),"数据已更新",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(),"数据已是最新",Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TErr", error.getMessage(), error);
            }
        });
        //设置请求的超时时间和重连次数
        GsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                2,
                2
        ));
        mQueue.add(GsonRequest);


    }

    public void onDestroy() {

        super.onDestroy();
        db.closeDB();
    }


}
