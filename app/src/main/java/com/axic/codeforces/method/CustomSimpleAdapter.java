package com.axic.codeforces.method;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.axic.codeforces.R;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by 59786 on 2016/4/14.
 */
public class CustomSimpleAdapter extends SimpleAdapter {

    private Context context;
    private int resource;
    private List<? extends Map<String, ?>> data;
    private Activity activity;

    public CustomSimpleAdapter(Context context,
                               List<? extends Map<String, ?>> data,
                               int resource,
                               String[] from,
                               int[] to, Activity activity) {
        super(context, data, resource, from, to);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup group) {
        Log.d("getView","getView");
//        final LayoutInflater layoutInflater = LayoutInflater.from(context);
//        View view = layoutInflater.inflate(resource, null);
        View view = super.getView(position, convertView, group);

        final TextView content = (TextView) view.findViewById(R.id.home_content);
        TextView title = (TextView) view.findViewById(R.id.home_title);
        TextView read_me = (TextView) view.findViewById(R.id.home_read_me);
//        TextView text = (TextView) view.findViewById(R.id.text);

        title.setText(data.get(position).get("title").toString());
        new Thread(new Runnable() {
            @Override
            public void run() {


                final Spanned contentData = Html.fromHtml(data.get(position).get("content").toString(),
                        new Html.ImageGetter() {
                            @Override
                            public Drawable getDrawable(String source) {
                                InputStream is = null;
                                try {
//                            DisplayMetrics dm = new DisplayMetrics();
//                            Log.d("dm.density", dm.density + " , "+dm.densityDpi);
                                    Log.d("getImage", "success");
                                    is = (InputStream) new URL(source).getContent();

                                    Drawable d = Drawable.createFromStream(is, "src");
//                                    ProblemsDetails pd = new ProblemsDetails();
                                    //屏幕适配
//                                    d = pd.screenAdaptation(d);
                                    d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                                    is.close();
                                    return d;
                                } catch (Exception e) {
                                    Log.d("getImage", "failed");
                                    return null;
                                }
                            }
                        }, null);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        content.setText(contentData);
                    }
                });
            }
        }).start();

        read_me.setText(data.get(position).get("link").toString());
        return view;
    }

}
