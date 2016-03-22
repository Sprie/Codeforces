package com.axic.codeforces;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Context context;
    Gson gson;
//    private String[] date =
//    String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue mQueue = Volley.newRequestQueue(this);
        gson = new Gson();

        final ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();


//        StringRequest stringRequest = new StringRequest("http://codeforces.com/api/contest.list?gym=true",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("Tag",response);
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Log.e("TAG", volleyError.getMessage(), volleyError);
//                    }
//        });
//        mQueue.add(stringRequest);
/*
        GsonRequest<contest> GsonRequest = new GsonRequest<contest>(
                "http://codeforces.com/api/contest.list?gym=true",contest.class,
                new Response.Listener<contest>(){
                    @Override
                    public void onResponse(contest mContest){
                                List<contest.ResultBean> ctList = mContest.getResult();

                                for(contest.ResultBean rst : ctList) {
                                    String name = rst.getName();
                                    Log.d("TAG", "name is " + name);
                                    HashMap<String, String> map = new HashMap<String, String>();
                                    map.put("name", name);
                                    list.add(map);
                                }
                        SimpleAdapter listAdapter = new SimpleAdapter(MainActivity.this, list,R.layout.listview,
                                new String[] {"name"},new int[]{R.id.contestName});
                        ListView listView = (ListView) findViewById(R.id.listview);
                        listView.setAdapter(listAdapter);
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e("TErr","Error");
                    }
        });
        mQueue.add(GsonRequest);*/
        new GetProblemInfoFromHtml().start();


    }


}
