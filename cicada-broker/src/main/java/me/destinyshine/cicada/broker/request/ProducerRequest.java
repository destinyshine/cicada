package me.destinyshine.cicada.broker.request;

import java.nio.ByteBuffer;

import me.destinyshine.cicada.broker.encode.BytesMessage;

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
    //--in-message>>
    //Crc => int32
    //MagicByte => int8
    //Attributes => int8
    //Key => bytes
    //Value => bytes

    private BytesMessage message;

    public ProducerRequest(short requestType,
                           short apiVersion,
                           int correlationId,
                           String clientId,
                           String topic,
                           ByteBuffer message) {
        super(requestType, apiVersion, correlationId, clientId);
        this.topic = topic;
        this.message = new BytesMessage(message);
    }

    public String getTopic() {
        return topic;
    }

    public BytesMessage getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ProducerRequest{" +
            "requestType=" + requestType +
            ", apiVersion=" + apiVersion +
            ", correlationId=" + correlationId +
            ", clientId='" + clientId + '\'' +
            ", topic='" + topic + '\'' +
            ", message=" + message +
            '}';
    }
}
