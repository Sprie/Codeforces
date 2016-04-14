package com.axic.codeforces.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.axic.codeforces.R;

/**
 * Created by 59786 on 2016/4/11.
 */
public class MyDialog extends Dialog implements View.OnClickListener{
    private TextView transSource;
    private TextView transDst;
    private String source;
    private String dst;
    private Button back;
    private Button add;

    public MyDialog(Context context, String source, String dst) {
        super(context);
        this.source = source;
        this.dst = dst;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog);
        transSource = (TextView) findViewById(R.id.trans_source);
        transDst = (TextView) findViewById(R.id.trans_dst);
        transSource.setText(source);
        transDst.setText(dst);
        back = (Button) findViewById(R.id.trans_back);
        add = (Button) findViewById(R.id.trans_add);
        back.setOnClickListener(this);
        add.setOnClickListener(this);
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.trans_back:
                this.cancel();
                break;
            case R.id.trans_add:
                //method
                break;
            default:
                break;
        }
    }
}
