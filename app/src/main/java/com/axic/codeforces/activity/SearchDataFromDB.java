package com.axic.codeforces.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.axic.codeforces.R;
import com.axic.codeforces.data.ProblemsIndex;
import com.axic.codeforces.data.contestFalse;
import com.axic.codeforces.database.ContestDBManager;
import com.axic.codeforces.database.ProblemsDBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 59786 on 2016/3/30.
 */
public class SearchDataFromDB extends Activity implements View.OnClickListener {
    private TextView back;
    private EditText search;
    private TextView enter;
    private ListView searchList;
    private ContestDBManager contestDB;
    private ProblemsDBManager problemsDB;
    private int contestStatus;
    private int problemsStatus;
    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

            searchData();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            list.clear();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search);

        contestStatus = 0;
        problemsStatus = 0;

        contestDB = new ContestDBManager(this, 1);
        problemsDB = new ProblemsDBManager(this, 1);

        back = (TextView) findViewById(R.id.search_back);
        search = (EditText) findViewById(R.id.search_edit);
        enter = (TextView) findViewById(R.id.search_enter);
        searchList = (ListView) findViewById(R.id.search_list);

        back.setOnClickListener(this);
        enter.setOnClickListener(this);
        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("position", position + "");

                HashMap<String, String> map = (HashMap<String, String>) searchList.getItemAtPosition(position);
                String tags = map.get("tag");
                Log.d("Tag", tags);
                //选择的是problems
                if (tags.equals("Tags:")) {
                    String problemsId = map.get("id");
                    String problemsIndex = map.get("index");
Log.d("start","comming intent");
                    //新建一个活动页面，承载problemsDetails内容
                    Intent proIntent = new Intent(SearchDataFromDB.this, ProblemsDetailsActivity.class);
                    proIntent.putExtra("problemsId", problemsId);
                    proIntent.putExtra("problemsIndex", problemsIndex);
                    startActivity(proIntent);

                } else if (tags.equals("Phase:")) {//选择的是contest
                    String contestId = map.get("index");
                    String contestName = map.get("name");
                    Intent mIntent = new Intent(SearchDataFromDB.this, ProblemsListActivity.class);
                    mIntent.putExtra("contestId", contestId);
                    mIntent.putExtra("contestName", contestName);
                    startActivity(mIntent);
                }
            }
        });

        search.addTextChangedListener(textWatcher);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_back:
                this.finish();
                break;
            case R.id.search_enter:
                String searchStr = search.getText().toString();
                break;
//            case R.id.search_list:

//                break;
            default:
                break;

        }
    }

//
//    private void searchDataFromContest() {
//
//        List<contestFalse.ResultBean> ctList = contestDB.searchIdDataFromDB(search.getText().toString());
//        if (ctList != null) {
//
//            ArrayList<HashMap<String, String>> contestList = new ArrayList<HashMap<String, String>>();
//            if (contestStatus == 0) {
//                HashMap<String, String> map = new HashMap<String, String>();
////                map.put("name", name);
//                map.put("name", "Contest");
////                map.put("phase", phase);
//                contestList.add(map);
//            }
//            for (contestFalse.ResultBean rst : ctList) {
//                String name = rst.getName();
//                String id = rst.getId();
//                String phase = rst.getPhase();
////                Log.d("TAG", id + "," + name);
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("name", name);
//                map.put("id", id);
//                map.put("phase", phase);
//                contestList.add(map);
//            }
//
//            SimpleAdapter listAd = new SimpleAdapter(this, contestList, R.layout.listview,
//                    new String[]{"name", "id", "phase"}, new int[]{R.id.contestName, R.id.contestId, R.id.contestPhase});
////            ListView searchList = new ListView(this);
////            mLayout.addView(searchList);
//            searchList.setAdapter(listAd);
//        } else {
//            mLayout.removeAllViews();
//            contestStatus = 0;
//        }
//    }
//
//    private void searchDataFromProblems() {
//        List<ProblemsIndex.ResultBean.ProblemsBean> ctList = problemsDB.searchDataFromDBByEdit(search.getText().toString());
//        if (ctList != null) {
//
//            ArrayList<HashMap<String, String>> problemsList = new ArrayList<HashMap<String, String>>();
//            if (problemsStatus == 0) {
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("name", "Problems");
////                map.put("index", index);
////            map.put("points", points);
////            map.put("type", type);
////                map.put("tags", tags);
//                problemsList.add(map);
//
//            }
//            for (ProblemsIndex.ResultBean.ProblemsBean rst : ctList) {
//                //获取数据库的数据
//                String name = rst.getName();
//                String id = rst.getContestId();
//                String points = rst.getPoints();
//                String type = rst.getType();
//                String index = rst.getIndex();
//                String tags = rst.getStrTags();
//
////            Log.d("tags", tags);
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put("name", name);
//                map.put("index", index);
////            map.put("points", points);
////            map.put("type", type);
//                map.put("tags", tags);
//                map.put("tag", "Tags:");
//
//                problemsList.add(map);
//            }
//
//            SimpleAdapter listAdapter = new SimpleAdapter(this, problemsList, R.layout.poblemslistview,
//                    new String[]{"name", "index", "tags", "tag"},
//                    new int[]{R.id.problemName, R.id.problemIndex, R.id.problemTags, R.id.Tags});
////            ListView searchList = new ListView(this);
////            mLayout.addView(searchList);
//            searchList.setAdapter(listAdapter);
//            Log.d("right", "right");
//        } else {
//            mLayout.removeAllViews();
//            problemsStatus = 0;
//        }
//
//    }

    private void searchData() {
        List<contestFalse.ResultBean> CTList = contestDB.searchIdDataFromDB(search.getText().toString());
        ArrayList<HashMap<String, String>> contestList = new ArrayList<HashMap<String, String>>();

        if (CTList != null) {
            if (contestStatus == 0) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", "Contest");
                contestList.add(map);
            }
            for (contestFalse.ResultBean rst : CTList) {
                String name = rst.getName();
                String id = rst.getId();
                String phase = rst.getPhase();
//                Log.d("TAG", id + "," + name);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", name);
                map.put("index", id);
                map.put("tags", phase);
                map.put("tag", "Phase:");
                contestList.add(map);
            }

        } else {
        }


        List<ProblemsIndex.ResultBean.ProblemsBean> PBList = problemsDB.searchDataFromDBByEdit(search.getText().toString());


        if (PBList != null) {
            if (problemsStatus == 0) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", "Problems");
                contestList.add(map);
            }
            for (ProblemsIndex.ResultBean.ProblemsBean rst : PBList) {
                //获取数据库的数据
                String name = rst.getName();
                String id = rst.getContestId();
                String points = rst.getPoints();
                String type = rst.getType();
                String index = rst.getIndex();
                String tags = rst.getStrTags();

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", name);
                map.put("index", index);
                map.put("tags", tags);
                map.put("tag", "Tags:");
                map.put("id", id);
                contestList.add(map);
            }
        } else {

        }

        SimpleAdapter listAdapter = new SimpleAdapter(this, contestList, R.layout.poblemslistview,
                new String[]{"name", "index", "tags", "tag"},
                new int[]{R.id.problemName, R.id.problemIndex, R.id.problemTags, R.id.Tags});
        searchList.setAdapter(listAdapter);

    }
}