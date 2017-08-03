package me.destinyshine.cicada.broker.request;

import java.nio.ByteBuffer;

/**
 * Created by liujianyu.ljy on 17/7/30.
 *
 * @author liujianyu.ljy
 * @date 2017/07/30
 */
public class ProducerRequest extends AbstractRequest implements Request {

    private final String topic;

    private final ByteBuffer payload;

    public ProducerRequest(short requestType, short apiVersion, int correlationId, String clientId, String topic,
                           ByteBuffer payload) {
        super(requestType, apiVersion, correlationId, clientId);
        this.topic = topic;
        this.payload = payload;
    }

    public String getTopic() {
        return topic;
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
