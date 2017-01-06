package com.xyl.zhifutopanser.utils;

import android.text.TextUtils;

import com.xyl.architectrue.utils.LogUtils;
import com.xyl.zhifutopanser.Model.AnswersModel;
import com.xyl.zhifutopanser.Model.TopQuestion;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: ShaudXiao
 * Date: 2017-01-05
 * Time: 10:24
 * Company: zx
 * Description:
 * FIXME
 */


public class JSoupUtils {

    private final static String TAG = "JSoupUtils";

    public static final  List<TopQuestion> getTopList(Document document) {
        Elements contentLinks = document.select("div.content");
        List<TopQuestion> list = new ArrayList<>();
        Iterator iterator = contentLinks.iterator();
        while (iterator.hasNext()) {
            TopQuestion answers = new TopQuestion();
            Element body = (Element) iterator.next();
            Elements questionLinks = body.select("a.question_link");
            if (questionLinks.iterator().hasNext()) {
                Element questionLink = questionLinks.iterator().next();
                answers.setTitle(questionLink.text());
                answers.setUrl("https://www.zhihu.com" + questionLink.attr("href"));
            }


            Elements votes = body.select("a.zm-item-vote-count.js-expand.js-vote-count");
            if (votes.size() > 0) {
                if (votes.iterator().hasNext()) {
                    Element aVotes = votes.iterator().next();
                    answers.setVote(aVotes.text());
                }
            }

            Elements divs = body.select("div.zh-summary.summary.clearfix");

            String descBody = divs.text();
            if (descBody.length() > 4) {
                descBody = descBody.substring(0, descBody.length() - 4);
            }
            answers.setBody(descBody);
            if (divs.size() > 0) {
                if (divs.iterator().hasNext()) {
                    Element aDiv = divs.iterator().next();

                    Element img = aDiv.children().first();
                    if (img.tagName().equals("img")) {
                        String imgUrl = img.attr("src");
                        answers.setImgUrl(imgUrl);
                    }
                }
            }
            if (!TextUtils.isEmpty(answers.getTitle()) && !TextUtils.isEmpty(answers.getUrl())) {
                LogUtils.i(TAG, answers.toString());
                list.add(answers);
            }
        }
        return list;
    }

    public static List<AnswersModel> getTopAnswers(Document document) {
        List<AnswersModel> list = new ArrayList<>();

        Element bodyAnswer = document.getElementById("zh-question-answer-wrap");
        Elements bodys = bodyAnswer.select("div.zm-item-answer");
        Element bodyWrapAnswer = document.getElementById("zh-question-collapsed-wrap");
        bodys.addAll(bodyWrapAnswer.select("div.zm-item-answer.zm-item-expanded"));

        if (bodys.iterator().hasNext()) {
            Iterator iterator = bodys.iterator();
            while (iterator.hasNext()) {
                AnswersModel answersModel = new AnswersModel();
                Element element = (Element) iterator.next();
                String url = element.getElementsByTag("link").attr("href");
                String vote = element.select("span.count").text();
                String content = element.select("div.zh-summary.summary.clearfix").text();
                if (content.length() > 4) {
                    content = content.substring(0, content.length() - 4);
                }
                String user = element.select("a.author-link").text();
                answersModel.setAuthor(user);
                answersModel.setContent(content);
                answersModel.setUrl(url);
                answersModel.setVote(vote);
                list.add(answersModel);

            }
        }

        return list;
    }

}
