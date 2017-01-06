package com.xyl.zhifutopanser.Model;

/**
 * User: ShaudXiao
 * Date: 2017-01-05
 * Time: 15:10
 * Company: zx
 * Description:
 * FIXME
 */


public class TopQuestion {

    private String url;
    private String title;
    private String imgUrl;
    private String vote;
    private String body;

    public TopQuestion() {}

    public TopQuestion(String url, String title, String imgUrl, String vote, String body) {
        this.url = url;
        this.title = title;
        this.imgUrl = imgUrl;
        this.vote = vote;
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "TopQuestion{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", vote='" + vote + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
