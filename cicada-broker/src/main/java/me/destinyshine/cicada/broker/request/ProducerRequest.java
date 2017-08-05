package me.destinyshine.cicada.broker.request;

import java.nio.ByteBuffer;

/**
 * Created by liujianyu.ljy on 17/7/30.
 *
 * @author liujianyu.ljy
 * @date 2017/07/30
 */
public class ProducerRequest extends AbstractRequest implements Request {

    //short RequiredAcks
    //int timeout

    private final String topic;

    //int partition
    //int MessageSetSize
    //byte[] MessageSet

    //Crc => int32
    //MagicByte => int8
    //Attributes => int8
    //Key => bytes
    //Value => bytes

    private final ByteBuffer key;

    private final ByteBuffer payload;

    public ProducerRequest(short requestType,
                           short apiVersion,
                           int correlationId,
                           String clientId,
                           String topic,
                           ByteBuffer key,
                           ByteBuffer payload) {
        super(requestType, apiVersion, correlationId, clientId);
        this.topic = topic;
        this.key = key;
        this.payload = payload;
    }

    public String getTopic() {
        return topic;
    }

    public ByteBuffer getKey() {
        return key;
    }

    public ByteBuffer getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "ProducerRequest{" +
            "requestType=" + requestType +
            ", apiVersion=" + apiVersion +
            ", correlationId=" + correlationId +
            ", clientId='" + clientId + '\'' +
            ", topic='" + topic + '\'' +
            ", payload=" + payload +
            '}';
    }
}
