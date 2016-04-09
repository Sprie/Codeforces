package com.axic.codeforces.method;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            //去除table中无用数据
//            table.cl

            Log.d("check", haveOrNoData.html() + "\n 0000");
            if (!haveOrNoData.html().equals("")) {
//                for (Element el : haveOrNoData) {
//                    html = html +
//                            el.html() + "\n\n";
//                    Log.d("html", html);
//
//                }
                html = table.text();
                Log.d("table", html + "\n1");
                html = html.replace("# ", "");
                html = html.replace("Party ", "");
                html = html.replace("When ", "");
                html = html.replace("Question ", "");
                html = html.replace("Answer", "");
                html = html.replace(" Announcement ", "Announcement<br>");
                html = html.replace("*****", "<br><font color=\"red\">");
                //正则表达式
//
                Pattern pattern2 = Pattern.compile("20..-..-.. ..:..:..");
                Matcher matcher2 = pattern2.matcher(html);
                while (matcher2.find()) {
                    html = matcher2.replaceAll("</font><br><br>" + matcher2.group() + "<br>");
                }
                Pattern pattern = Pattern.compile("</font><br><br>");//括号中为正则表达式
                Matcher matcher = pattern.matcher(html);//括号中为要匹配的字符串
                html = matcher.replaceFirst("");
////
//                while(matcher.find()){
//                    html = matcher.replaceAll(matcher.group()+"<br>");
//                }
                Log.d("table", html + "\n2");
//                html = html.replace("<br>","\n");

            } else {
                html = " No questions \n";
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

            return true;
        } else {
            html = "Error";
        }
        return false;
    }
}
