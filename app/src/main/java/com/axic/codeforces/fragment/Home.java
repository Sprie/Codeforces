package com.axic.codeforces.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.axic.codeforces.R;
import com.axic.codeforces.data.HomePage;
import com.axic.codeforces.method.CustomSimpleAdapter;
import com.axic.codeforces.method.GetHomePageContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by 59786 on 2016/3/27.
 */
public class Home extends Fragment {
    private View view;
    private RequestQueue mQueue;
    private ImageView imageView;
    private ListView listView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homepage, container, false);
//        view = getPersistentView(inflater, container, savedInstanceState);
//        imageView = (ImageView) view.findViewById(R.id.imageView);
//        getImageFromNet();

        listView = (ListView) view.findViewById(R.id.home_listview);


        new Thread(new Runnable() {
            @Override
            public void run() {
                GetHomePageContent getContent = new GetHomePageContent("1");

                List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                if (getContent.run()) {
                    //获取数据
                    List<HomePage> homeData = getContent.getData();
                    //将数据放入listview的item中
                    for (HomePage home : homeData) {
//                    HomePage home = new HomePage();
//                    home = homeData.get(i);
                        String title = home.getTitle();
                        String content = home.getContent();
                        String link = home.getLink();
                        String id = home.getId();

                        HashMap<String, String> data = new HashMap<String, String>();
                        data.put("title", title);
                        data.put("content", content);
                        data.put("link", link);
//                    data.put("id", id);
                        list.add(data);
                    }
                } else {


                    HashMap<String, String> data = new HashMap<String, String>();
                    data.put("title", "null");
                    data.put("content", "null");
                    data.put("link", "null");
//                    data.put("id", id);
                    list.add(data);
                }
                final CustomSimpleAdapter adapter = new CustomSimpleAdapter(getActivity(), list, R.layout.home_listview,
                        new String[]{"title", "content", "link"}, new int[]{R.id.home_title, R.id.home_content, R.id.home_read_me},
                        getActivity());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(adapter);
                    }
                });
            }
        }).start();

        return view;
    }

    //volley获取网络图片
    public void getImageFromNet() {
        mQueue = Volley.newRequestQueue(getActivity());

        ImageRequest imageRequest = new ImageRequest(
                //url
                "http://pic.to8to.com/attch/day_160218/20160218_a9c8ab4599980f55577bp7at2oEwM7s9.png",
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setImageResource(R.drawable.back);
            }
        }
        );
        mQueue.add(imageRequest);
    }

//    private View getPersistentView(LayoutInflater inflater, ViewGroup container,
//                                   Bundle savedInstanceState) {
//        if (view == null) {
//            view = inflater.inflate(R.layout.homepage, container, false);
//        } else {
//            ((ViewGroup) view.getParent()).removeView(view);
//        }
//
//        return view;
//    }
}
