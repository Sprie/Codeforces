package com.axic.codeforces.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.TextView;

import com.axic.codeforces.R;

/**
 * Created by 59786 on 2016/4/12.
 */
public class AboutApp extends Activity {
    private TextView aboutApp;
    private String aboutAppString = "This App based codefoces.com and all content's power is that web' own .<br>" +
            "App author: Axic<br>" +
            "Email: zhouaxic@gmail.com";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.about_app);
        aboutApp = (TextView) findViewById(R.id.about_app_textview);
        aboutApp.setText(Html.fromHtml(aboutAppString));
    }
}
