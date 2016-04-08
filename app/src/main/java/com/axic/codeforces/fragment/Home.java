package com.axic.codeforces.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.axic.codeforces.R;

/**
 * Created by 59786 on 2016/3/27.
 */
public class Home extends Fragment {
    private View view;
    private RequestQueue mQueue;
    private ImageView imageView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homepage, container, false);
//        view = getPersistentView(inflater, container, savedInstanceState);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        getImageFromNet();
        return view;
    }

    //test
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
