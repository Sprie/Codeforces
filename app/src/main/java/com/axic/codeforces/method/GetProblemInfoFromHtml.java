package com.axic.codeforces.method;


import android.os.Message;
import android.util.Log;


import com.axic.codeforces.fragment.ProblemsDetails;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * Created by 59786 on 2016/3/22.
 */
public class GetProblemInfoFromHtml {
    Document doc;


    String html;
    String url;

    public String getTitle() {
        return title;
    }

    String title;


    public String getHtml() {
        return html;
    }


    public GetProblemInfoFromHtml(String url) {
        this.url = url;
    }

    //发生错误返回false
    public boolean run() {


        try {
            doc = Jsoup.connect(url).timeout(8000).get();
            Log.d("url", url);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (doc != null) {
            //获取problem全部数据；
            Elements HTML = doc.select("div.problem-statement");
            //获取problem-statement类下的html代码
            html = HTML.html();
            if (html != null) {
                Log.d("html", html);

            } else {
                Log.d("null", "null");
            }
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







/*  可以使用TextView直接将HTML格式的文本直接显示，而且保留格式，非常好用
    //获取problem的头数据
    private void getHeader(Element data) {
        //获取title
        Element problemTitle = data.getElementsByClass("title").first();
        title = problemTitle.text();

        //获取time信息
        Element time_limit = data.getElementsByClass("time-limit").first();
        Element time_title = time_limit.getElementsByClass("property-title").first();
        timePropertyTitle = time_title.text() + ": ";
        timeLimit = time_limit.ownText();

        //获取memory信息
        Element memory_limit = data.getElementsByClass("memory-limit").first();
        Element memory_title = memory_limit.getElementsByClass("property-title").first();
        memoryPropertyTitle = memory_title.text() + ": ";
        memoryLimit = memory_limit.ownText();

        //获取input信息
        Element input_file = data.getElementsByClass("input-file").first();
        Element input_title = input_file.getElementsByClass("property-title").first();
        inputPropertyTitle = input_title.text() + ": ";
        inputFile = input_file.ownText();

        //获取output信息
        Element output_file = data.getElementsByClass("output-file").first();
        Element output_title = input_file.getElementsByClass("property-title").first();
        outputPropertyTitle = output_title.text() + ": ";
        outputFile = output_file.ownText();
    }

    //获取题目的介绍内容
    private void getInfo(Element data) {
        /**可以直接获取问题的详细信息，但需要对其中的数据进行格式化，幂的格式化，
         *        行的格式化，主要是各种数学公式的格式化
         *           有的有序行也需要解析出来；
         *           包括图片的识别与解析
         /

    Element info = data.getElementsByTag("div").get(11);
    problemInfo=info.text();
    Log.d("div",problemInfo);

}

    //获取input和output的介绍内容
    private void getIOput(Element data) {
        /**同样需要对获取到的内容进行格式化
         *
         /
        //获取input介绍内容
        Element input_specification = data.getElementsByClass("input-specification").first();
        Element input_section_title = input_specification.getElementsByClass("section-title").first();
        inputSpecification = input_specification.ownText();
        inputSectionTitle = input_section_title.text();

        //获取output介绍内容
        Element output_specification = data.getElementsByClass("output-specification").first();
        Element output_section_title = output_specification.getElementsByClass("section-title").first();
        outputSpecification = output_specification.ownText();
        outputSectionTitle = output_section_title.text();
    }

    //获取Example的内容
    private void getExample(Element data, boolean have2, Element sample_tests) {
        /**
         * 数据需要格式化
         /

        Element section_title = sample_tests.getElementsByClass("section-title").first();
        exampleSectionTitle = section_title.ownText();

        Element sample_test = sample_tests.getElementsByClass("sample-test").first();
        //examle1  input
        Element example_input1 = sample_tests.getElementsByTag("pre").first();
        exampleInput1 = example_input1.ownText();
        exampleInputTitle1 = "Input";
        //example1 output
        Element example_output1 = sample_tests.getElementsByTag("pre").get(1);
        exampleoutput1 = example_output1.ownText();
        exampleoutputTitle1 = "Output";
        if (have2) {
            //examle2  input
            Element example_input2 = sample_tests.getElementsByTag("pre").get(2);
            exampleInput2 = example_input2.ownText();
            exampleInputTitle2 = "Input";
            //example2 output
            Element example_output2 = sample_tests.getElementsByTag("pre").get(3);
            exampleoutput2 = example_output2.ownText();
            exampleoutputTitle2 = "Output";
        }
    }

    //获取Node数据
    private void getNode(Element data, Element node) {
        /**
         * 数据需要解析，可能含有图片资源
         *
         /

        nodeTitle = "Node";
        nodeInformation = node.text().substring(5);
        Log.d("node", nodeInformation);

    }*/
}
