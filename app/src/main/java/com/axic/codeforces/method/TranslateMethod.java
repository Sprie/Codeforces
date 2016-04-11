package com.axic.codeforces.method;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.axic.codeforces.data.Translate;
import com.axic.codeforces.dialog.MyDialog;

import org.apaches.commons.codec.digest.DigestUtils;

import java.util.List;
import java.util.Random;

/**
 * Created by 59786 on 2016/4/11.
 */
public class TranslateMethod {

    private Context context;

    public TranslateMethod(Context context) {
        this.context = context;
    }

    String src = "apple";
    String dst = "苹果";

    public void getTranslateFromBD(String q) {
        final String appId = "20160410000018259";

        final String token = "TzzWMGet6wYhigkTtPQz";

        String url = "http://api.fanyi.baidu.com/api/trans/vip/translate";

        final Random random = new Random();

        //http://api.fanyi.baidu.com/api/trans/vip/translate
        // ?q=apple&from=en&to=zh&appid=2015063000000001&salt=1435660288&sign=f89f9594663708c1605f3d736d01d2d4
        int salt = random.nextInt(10000);
        //计算MD5
        StringBuilder md5String = new StringBuilder();
        md5String.append(appId).append(q).append(salt).append(token);
        String md5 = DigestUtils.md5Hex(md5String.toString());

        url = url + "?q=" + q + "&from=en&to=zh&appid=" + appId + "&salt=" + salt + "&sign=" + md5;
        //请求数据

        RequestQueue mQueue;
        mQueue = Volley.newRequestQueue(context);
        GsonRequest<Translate> gsonRequest = new GsonRequest<Translate>(
                url, Translate.class,
                new Response.Listener<Translate>() {
                    @Override
                    public void onResponse(Translate translate) {

                        List<Translate.TransResultBean> trans = translate.getTrans_result();
                        String src = "null", dst = "null";

                        if (trans != null) {Log.d("size",trans.size()+"...");
                            for (Translate.TransResultBean tr : trans) {
                                src = tr.getSrc();
                                dst = tr.getDst();
                                Log.d("TAG", "souce is " + src);
                                Log.d("TAG", "dst is " + dst);
                            }
                            //显示在屏幕上
//                            Toast.makeText(context, src + "\n" + dst, Toast.LENGTH_LONG).show();
                            MyDialog myDialog = new MyDialog(context,src,dst);
                            myDialog.show();
                        } else {
                            Toast.makeText(context, "Error Select,please don't select null line and specific char.", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAGeee", error.getMessage(), error);
                Toast.makeText(context,"Error select,please don't select null line and specific char.",Toast.LENGTH_LONG).show();
            }
        });
        mQueue.add(gsonRequest);
//        Log.d("TAG22", "dst is " + dst);
//        if (dst != null) {
//            return dst;
//        }
    }
}
