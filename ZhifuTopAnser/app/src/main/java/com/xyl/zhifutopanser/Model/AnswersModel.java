package com.xyl.zhifutopanser.Model;

/**
 * User: ShaudXiao
 * Date: 2017-01-05
 * Time: 17:08
 * Company: zx
 * Description:
 * FIXME
 */


public class AnswersModel {

    private String vote;
    private String content;
    private String author;
    private String url;

    public AnswersModel() {}

    public AnswersModel(String vote, String content, String author, String url) {
        this.vote = vote;
        this.content = content;
        this.author = author;
        this.url = url;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
