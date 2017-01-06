package com.xyl.zhifutopanser.Model;

/**
 * User: ShaudXiao
 * Date: 2017-01-05
 * Time: 10:01
 * Company: zx
 * Description:
 * FIXME
 */


public class TopicModel {

    private int topicId;
    private String topicName;

    public TopicModel(int topicId, String topicName) {
        this.topicId = topicId;
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    @Override
    public String toString() {
        return "TopicModel{" +
                "topicId=" + topicId +
                ", topicName='" + topicName + '\'' +
                '}';
    }
}
