package com.axic.codeforces.method;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by 59786 on 2016/4/9.
 * textview中的链接处理
 */
public class TextViewClickSpan extends ClickableSpan {
    String text;
    Context mContext;

    public TextViewClickSpan(String text,Context mContext) {
        super();
        this.text = text;
        this.mContext = mContext;
    }

    @Override
    public void updateDrawState(TextPaint ds){
        ds.setColor(Color.parseColor("#948435"));//指定颜色值
        ds.setUnderlineText(true);//下划线
    }

    @Override
    public void onClick(View widght){
        //点击超链接时调用
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri url = Uri.parse(text);
        intent.setData(url);
        mContext.startActivity(intent);
    }
}
