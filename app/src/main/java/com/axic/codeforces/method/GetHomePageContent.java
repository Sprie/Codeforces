package com.axic.codeforces.method;

import android.util.Log;

import com.axic.codeforces.data.HomePage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 59786 on 2016/4/14.
 */
public class GetHomePageContent {

    private String basicUrl = "http://codeforces.com";
    private String url;
    private Document doc;

    public List<HomePage> getData() {
        return data;
    }

    private List<HomePage> data;

    public GetHomePageContent(String index) {
        url = basicUrl + "/page/" + index;
        data = new ArrayList<HomePage>();
    }

    public boolean run() {
        try {
            doc = Jsoup.connect(url).timeout(8000).get();
            Log.d("url", url);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        //
        getFromNet();

        return true;
    }

    private void getFromNet() {
        Elements topics = doc.select("div.topic");
        for (Element topic : topics) {
            //获取标题
            Element title = topic.select("div.title").first();
            String titleStr = title.text();
            Log.d("title", titleStr);
            //获取正文
            Element content = topic.select("div.content").first();
            Element con = content.select("div.ttypography").first();
            String contentStr = con.html();
            Log.d("content", contentStr);
            //获取read me链接
            Element link = title.select("a").first();
            String href = link.attr("href");
            Log.d("href", href);
            //获取topicId
            String topicId = topic.attr("topicId");
            Log.d("topicId",topicId);
//            Element links = topic.select("a").last();
//            Element co = topic.select("div.ttypography").first();
//            String mContent = co.html();
//            String link = links.attr("href");
            HomePage homePage = new HomePage();
            homePage.setTitle(titleStr);
            homePage.setContent(contentStr);
            homePage.setLink(href);
            homePage.setId(topicId);

            data.add(homePage);
        }
    }
}
