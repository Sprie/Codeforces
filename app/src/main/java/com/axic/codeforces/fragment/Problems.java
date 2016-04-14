package com.axic.codeforces.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.axic.codeforces.database.ProblemsQuestionsDBManager;
import com.axic.codeforces.method.CheckNet;
import com.axic.codeforces.method.GetProblemsQuestionsInfoFromHtml;
import com.axic.codeforces.method.GsonRequest;
import com.axic.codeforces.method.TranslateMethod;

import java.io.UnsupportedEncodingException;
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
    private String contestName;
    private String url;
    private SwipeRefreshLayout mSwipe;
    private Context mContext;
    boolean dbstatus = true;//查看数据库是否有该contest的problems列表，

    private ProblemsDBManager db;
    //    private String title;
    private ProblemsQuestionsDBManager PQdb;

    private TextView showContestId;
    private TextView showContestName;
    private CheckNet CheckNet;

    private Callbacks mCallbacks;

    private TextView problemsQuestions;
    private TextView getContestMaterials;
    private String questionsData;

    private Thread mThread;
    private boolean mThreadState=true;
    private TranslateMethod translateMethod;

    //定义一个回调接口
    public interface Callbacks {
        void getData(String contestId, String index);

        void getData(String contestId);
    }

    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        db = new ProblemsDBManager(mContext, 1);
        PQdb = new ProblemsQuestionsDBManager(mContext, 1);
        CheckNet = new CheckNet(mContext);
        translateMethod = new TranslateMethod(mContext);


        //获取Activity传来的数据
        final Bundle bundle = getArguments();
        if (bundle != null) {
            contestId = bundle.getString("contestId");
            contestName = bundle.getString("contestName");
        } else {
            contestId = "600";
            contestName = "Educational Codeforces Round 2";
        }

        url = ProblemUrl + "?" + "contestId=" + contestId + "&" +
                "from=1&count=1&showUnofficial=true";
//        title = contestId + "  " + contestName;
//        Log.d("title",title);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mQueue = Volley.newRequestQueue(mContext);
        view = inflater.inflate(R.layout.problemslist, container, false);
        listView = (ListView) view.findViewById(R.id.problemslistview);

        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.problems_swipe);
        mSwipe.setOnRefreshListener(this);
        mSwipe.setColorSchemeResources(android.R.color.black);

        showContestId = (TextView) view.findViewById(R.id.contest_id);
        showContestName = (TextView) view.findViewById(R.id.contest_name);

        problemsQuestions = (TextView) view.findViewById(R.id.problem_question);
        getContestMaterials = (TextView) view.findViewById(R.id.get_contest_materials);


        showContestName.setText(contestName);
        showContestId.setText(contestId);

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
        getContestMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("getCM", "clicked");
                Fragment mat = new Materials();
                Bundle bundle2 = new Bundle();
                bundle2.putString("contestId", contestId);
                mat.setArguments(bundle2);
                mCallbacks.getData(contestId);

            }
        });
        //Questions 的textView的长按复制翻译
        problemsQuestions.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                //Called when action mode is first created The memu supplied
                //will be used to generate action buttons for the action mode

                //add a option  menu.add(groupId,itemId,order,title);
                menu.add(0, 1, 0, "Translate");
                // or you can set icon like menu.add(***).setIcon(R.id.*);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                //Remove the menu's option
//                menu.removeItem(android.R.id.selectAll);
                menu.removeItem(android.R.id.cut);
//                menu.removeItem(android.R.id.copy);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case 1:
                        int min = 0;
                        int max = problemsQuestions.getText().length();
                        if (problemsQuestions.isFocusable()) {
                            final int selStart =problemsQuestions.getSelectionStart();
                            final int selEnd = problemsQuestions.getSelectionEnd();

                            min = Math.max(0, Math.min(selStart, selEnd));
                            max = Math.max(0, Math.max(selStart, selEnd));
                        }
                        // Perform your option with the selected text
                        final CharSequence selectedText = problemsQuestions.getText().subSequence(min, max);
                        // Do some to the selectedText
                        try {
                            String str = new String (selectedText.toString().getBytes(),"UTF-8");
                            translateMethod.getTranslateFromBD(str);

                            Log.d("Translate", "start");
                            Log.d("Translate", selectedText.toString());

                            Log.d("Translate", "end");

                        } catch (UnsupportedEncodingException e) {
                            Log.d("String change ", "error");
                        }
                        //Finish and close the ActionMode
                        mode.finish();
                        return true;
                    default:
                        break;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                //Called when an action mode is about to be exited and destroyed
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
            problemsQuestions.setText(R.string.Loading);
//            list.clear();

            getDataFromNet();
//            mSwipe.setRefreshing(false);
        } else {
            Toast.makeText(mContext, "当前无网络", Toast.LENGTH_LONG).show();
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
            map.put("tag", "Tags:");

            problemsList.add(map);
        }

        SimpleAdapter listAdapter = new SimpleAdapter(mContext, problemsList, R.layout.poblemslistview,
                new String[]{"name", "index", "tags", "tag"},
                new int[]{R.id.problemName, R.id.problemIndex, R.id.problemTags, R.id.Tags});

        listView.setAdapter(listAdapter);
        Log.d("right", "right");

    }

    private void getQuestionsFromDB() {
        //从数据库获取ProblemsQuestions数据并显示
        problemsQuestions.setText(Html.fromHtml(PQdb.getQustions(contestId)));
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
                                map.put("tag", "Tags:");

                                list.add(map);
                            }
                            SimpleAdapter listAdapter = new SimpleAdapter(mContext, list, R.layout.poblemslistview,
                                    new String[]{"name", "index", "tags", "tag"},
                                    new int[]{R.id.problemName, R.id.problemIndex, R.id.problemTags, R.id.Tags});

                            //若未退出该页面
                            if (mThreadState) {

                                listView.setAdapter(listAdapter);


                            }


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
                    //若未退出该页面
                    if (mThreadState) {



                        Toast.makeText(mContext, "服务器正在开小差~请稍候重试 >-<", Toast.LENGTH_LONG).show();

                    }
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

        //添加questions of problems
        if (!PQdb.hasQuestions(contestId)) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("new Thread", "start");
//                    final Spanned Htmlque;
                    GetProblemsQuestionsInfoFromHtml getData = new GetProblemsQuestionsInfoFromHtml();
                    if (getData.run(contestId)) {
                        questionsData = getData.getHtml();
                        Log.d("questionsData", questionsData);
                        PQdb.addQuestions(contestId, questionsData);
                    } else {
                        questionsData = "error";
                    }
//                    Htmlque = Html.fromHtml(questionsData);
//                    Htmlque = Html.fromHtml("<br>2222<br>333<br>");
                    //若未退出该页面
                    if (mThreadState) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("questionsDataOnUI", questionsData);
                                problemsQuestions.setText(Html.fromHtml(questionsData));
                            }
                        });

                    } else {
                        mThread.interrupt();
                    }

                }
            });
            mThread.start();
        } else {
            getQuestionsFromDB();
        }


    }

    public void onPause() {
        if (mThread != null && mThread.isAlive()) {
            //Log.e("readCacheThread", "thread interrupt_1");
            mThreadState = false;
            //Log.e("status", ""+readCacheThread.isInterrupted());
        }

        super.onPause();
    }

    public void onDestroy() {

        super.onDestroy();

//        db.closeDB();
    }
}
