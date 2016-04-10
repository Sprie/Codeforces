package com.axic.codeforces.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
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

import java.io.InputStream;
import java.net.URL;


/**
 * Created by 59786 on 2016/3/25.
 */
public class ProblemsDetails extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View view;
    private TextView tv;
    private String problemUrl;
    private String contestId;
    private String index;
    private String Url;
    private SwipeRefreshLayout mSwipe;
    private CheckNet checkNet;
    private ProblemsDBManager db;
    private GetProblemInfoFromHtml getProblemInfoFromHtml;
    private String html = "error";
    private Spanned details;
    boolean status = true;
    private float density;
    private float scaledDensity;
    private Thread mThread;
    private boolean mThreadState = true;
    int screenWidth;
    int screenHeight;
    Context mContext;


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
        mContext = getActivity();
        db = new ProblemsDBManager(mContext, 1);
        view = inflater.inflate(R.layout.problemsdetails, container, false);
        tv = (TextView) view.findViewById(R.id.details);

        mSwipe = (SwipeRefreshLayout) view.findViewById(R.id.details_swipe);
        mSwipe.setColorSchemeResources(android.R.color.black);
        mSwipe.setOnRefreshListener(this);
        checkNet = new CheckNet(mContext);

        //test
        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenWidth = metric.widthPixels;  // 屏幕宽度（像素）
        screenHeight = metric.heightPixels;  // 屏幕高度（像素）
        density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        scaledDensity = metric.scaledDensity;
//        Log.d("dpi", width + "  " + height + "  " + density + "  " + densityDpi);
        Log.d("density", density + "");
        Toast.makeText(mContext,
                screenWidth + "  " + screenHeight + "  " + density + "  " + densityDpi,
                Toast.LENGTH_LONG).show();

        tv.setText(R.string.Loading);

        tv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Log.d("onLongClick", "true");
                return false;
            }
        });
        Bundle bundle = getArguments();
        //以下两种url的方法都可以用的，但第二种获取的比较丰富点-----------
        // 但是第一种方法连接恒快，所以用第一种连接

        problemUrl = "http://codeforces.com/problemset/problem/";
        if (bundle != null) {
            contestId = bundle.getString("contestId");
            index = bundle.getString("index");
            Url = problemUrl + contestId + "/" + index;
            Log.d("url", Url);


        } else {
            tv.setText("error");
        }
//        problemUrl = "http://codeforces.com/contest/";
//        if (bundle != null) {
//            contestId = bundle.getString("contestId");
//            index = bundle.getString("index");
//            Url = problemUrl + contestId + "/problem/" + index;
//            Log.d("url", Url);
//        } else {
//            tv.setText("error");
//        }

        getProblemInfoFromHtml = new GetProblemInfoFromHtml(Url);

        getDataFromNet();


        return view;
    }

    public void onRefresh() {
        tv.setText(R.string.Loading);
//        getDataFromNet();
//        mSwipe.setRefreshing(false);

        if (checkNet.haveNet()) {
//            list.clear();

            getDataFromNet();
            mSwipe.setRefreshing(false);
        } else {
            Toast.makeText(mContext, "当前无网络", Toast.LENGTH_LONG).show();

            getDataFromDB();
            mSwipe.setRefreshing(false);
        }
    }

    private void getDataFromDB() {

        new Thread(new Runnable() {
            @Override
            public void run() {


                //使用textview显示获取的html中的文字和图片
                details = Html.fromHtml(db.getDetails(contestId, index), new Html.ImageGetter() {
                    @Override
                    public Drawable getDrawable(String source) {
                        InputStream is = null;
                        try {
//                            DisplayMetrics dm = new DisplayMetrics();
//                            Log.d("dm.density", dm.density + " , "+dm.densityDpi);
                            Log.d("getImage", "success");
                            is = (InputStream) new URL(source).getContent();

                            Drawable d = Drawable.createFromStream(is, "src");
                            //屏幕适配
                            d = screenAdaptation(d);

                            is.close();
                            return d;
                        } catch (Exception e) {
                            Log.d("getImage", "failed");
                            return null;
                        }
                    }
                }, null);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(details);
                        Log.d("details", "getFromDB");
                    }
                });
            }

        }).start();

    }

    private void getDataFromNet() {
        if (db.getDetails(contestId, index) == null) {

            Log.d("details", "getFromNet");
            //创建新线程获取ProblemDetails并显示到textView中
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d("comingNewThread", "coming");
                    status = getProblemInfoFromHtml.run();

                    if (status) {
                        Log.d("status", status + "...");
                        html = getProblemInfoFromHtml.getHtml();
                        //test
//                        Log.d("header",getProblemInfoFromHtml.getHeaderData());

                        //使用textview显示获取的html中的文字和图片
                        details = Html.fromHtml(html, new Html.ImageGetter() {
                            @Override
                            public Drawable getDrawable(String source) {
                                InputStream is = null;
                                try {
                                    Log.d("getImage", "success");
                                    is = (InputStream) new URL(source).getContent();
                                    Log.d("data", is.toString());
                                    Drawable d = Drawable.createFromStream(is, "src");
                                    //屏幕适配
                                    d = screenAdaptation(d);
                                    is.close();
                                    return d;
                                } catch (Exception e) {
                                    Log.d("getImage", "failed");
                                    return null;
                                }
                            }
                        }, null);
                        //若未退出该页面
                        if (mThreadState) {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv.setText(details);
                                    Log.d("In ", "Ui");
                                }
                            });
                            //向数据库添加数据
                            db.addDetail(html, contestId, index);
                        } else {
                            mThread.interrupt();
                        }


                    } else {
                        Log.d("status", status + "--");
                        //若未退出该页面
                        if (mThreadState) {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv.setText("获取失败，请稍后重试");

//                                Toast.makeText(mContext,"服务器正在开小差~请稍候重试 >-<",Toast.LENGTH_LONG).show();
                                    Log.d("In ", "Ui");
                                }
                            });
                        } else {
                            mThread.interrupt();
                        }
                    }
                    mSwipe.setRefreshing(false);
                }
            });

            mThread.start();

        } else {
            getDataFromDB();
        }

    }

    //屏幕适配
    private Drawable screenAdaptation(Drawable d) {
        float height;
        if (density >= 3) {
            height = 10 * scaledDensity + 0.5f;
        } else {
            height = 16 * scaledDensity + 0.5f;
        }
        Log.d("hhhh", height + " /// " + scaledDensity);
        if (d.getIntrinsicHeight() < height) {
            d.setBounds(0, 0, (int) (height*2 / d.getIntrinsicHeight() * d.getIntrinsicWidth()),//因为density是float，而这儿的参数需要int
                    (int) height*2);//所以强制装换为int
        } else {
            Log.d("density", density + "");
            if (density >= 3.0) {
                int width, h;
                if ((int) (d.getIntrinsicWidth() * density * 3) > screenWidth-20) {
                    Log.d("small screen", "fill width");
                    width = screenWidth-20;
                    h = width * d.getIntrinsicHeight()/ d.getIntrinsicWidth() ;
                    d.setBounds(0, 0, width, h);//所以强制装换为int
                    Log.d("changedHW", width + " / / " + h);
                } else {
                    d.setBounds(0, 0, (int) (d.getIntrinsicWidth() * density * 3),//以为density是float，而这儿的参数需要int
                            (int) (d.getIntrinsicHeight() * density * 3));//所以强制装换为int
                    Log.d("*2", ">3");
                }
            } else {
                int width, h;
                if ((int) (d.getIntrinsicWidth() * density) > screenWidth) {
                    Log.d("small screen", "fill width");
                    width = screenWidth;
                    h = d.getIntrinsicHeight() / d.getIntrinsicWidth() * width;
                    d.setBounds(0, 0, width, h);//所以强制装换为int
                } else {

                    d.setBounds(0, 0, (int) (d.getIntrinsicWidth() * density),//以为density是float，而这儿的参数需要int
                            (int) (d.getIntrinsicHeight() * density));//所以强制装换为int
                }
            }
        }
        Log.d("height width", d.getIntrinsicHeight() + "  " + d.getIntrinsicWidth() + " , ");
        Log.d("height ", d.getIntrinsicHeight() + "  " + (int) (d.getIntrinsicHeight() * density) + " , "
                + (d.getIntrinsicHeight() * (int) density));
        Log.d("width", d.getIntrinsicWidth() + "  " + (int) (d.getIntrinsicWidth() * density) + " , "
                + (d.getIntrinsicWidth() * (int) density));

//                            Toast.makeText(mContext,dm.density + " , "+dm.densityDpi,Toast.LENGTH_LONG).show();
        return d;
    }


    public void onPause() {
        super.onPause();
        Log.d("problemsDetails", "onPause");
        if (mThread != null && mThread.isAlive()) {
            //Log.e("readCacheThread", "thread interrupt_1");
            mThreadState = false;
            //Log.e("status", ""+readCacheThread.isInterrupted());
        }
    }

    public void onDestroy() {
        Log.d("problemsDetails", "onDestroy");
        super.onDestroy();
        db.closeDB();
    }
}
