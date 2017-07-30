package me.destinyshine.cicada.broker.request;

import java.nio.ByteBuffer;

/**
 * Created by liujianyu.ljy on 17/7/30.
 *
 * @author liujianyu.ljy
 * @date 2017/07/30
 */
public class ProducerRequest implements Request {

    private String topic;

    private ByteBuffer message;

    public ProducerRequest(String topic, ByteBuffer message) {
        this.topic = topic;
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ByteBuffer getMessage() {
        return message;
    }

    public void setMessage(ByteBuffer message) {
        this.message = message;
    }
}
