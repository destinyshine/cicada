package me.destinyshine.cicada.broker.request;

import java.nio.ByteBuffer;

import me.destinyshine.cicada.broker.connector.Response;

/**
 * <pre>
 * TopicName => string
 * Partition => int32
 * ErrorCode => int16
 * Offset => int64
 * Timestamp => int64
 * ThrottleTime => int32
 * </pre>
 *
 * @author liujianyu.ljy
 * @date 2017/07/30
 */
public class ProduceResponse implements Response {

    private String topic;

    private short responseCode;

    private long offset;

    @Override
    public ByteBuffer toResponseBytes() {
        return null;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public short getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(short responseCode) {
        this.responseCode = responseCode;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}
