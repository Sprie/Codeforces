package com.axic.codeforces.method;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by 59786 on 2016/4/8.
 */
public class GetProblemsQuestionsInfoFromHtml {

    private String url = "http://codeforces.com/contest/";
    Document doc;

    public String getHtml() {
        return html;
    }

    private String html = "";


    public boolean run(String id) {
        try {
            doc = Jsoup.connect(url + id).timeout(8000).get();
            Log.d("url", url + id);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (doc != null) {
            //获取problem全部数据；
            Element HTML = doc.select("div.datatable").get(1);
            Log.d("HTML", HTML.html());
            Element table = HTML.select("table").first();
            Log.d("Table", table.html());
            Elements haveOrNoData = table.select(".question-response");
            Log.d("check", haveOrNoData.html() + " 0000");
            if (!haveOrNoData.html().equals("")) {
                for (Element el : haveOrNoData) {
                    html = html +
                            "<p>"+el.html() + "</p>";
                    Log.d("html", html);

                }

            } else {
                html = "<p> No questions </p>";
            }
//            Element s = haveOrNoData.get
//                Log.d("haveNo", haveOrNoData.html() + "...");
//            if(table.hasClass("problem-question broadcast-question")){
            //获取problem-statement类下的html代码
//            html = HTML.html();
//            Log.d("html", html);

//            } else {
//                html = "<div>" +
//                        "null" +
//                        "</div>";
//                Log.d("null", "null");
//            }
//            Elements image = HTML.select("img[src]");
//            if (image != null) {
//                //若HTML文本中有图片链接，获取该图片
//
//            }


        } else {
            html = "Error";
        }
        return true;
    }
}
