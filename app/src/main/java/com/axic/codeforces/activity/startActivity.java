package com.axic.codeforces.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.axic.codeforces.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 59786 on 2016/4/6.
 * 程序启动界面，使用计时器完成
 */
public class StartActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(StartActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        Timer timer = new Timer() ;
        timer.schedule(task,4000);
    }
}
