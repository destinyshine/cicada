package me.destinyshine.cicada.broker.request;

import java.nio.ByteBuffer;

import me.destinyshine.cicada.broker.connector.RequestTypes;
import me.destinyshine.cicada.broker.connector.RequestReceiver;
import me.destinyshine.cicada.broker.utils.ByteBufferUtils;

/**
 * Created by liujianyu.ljy on 17/7/28.
 *
 * @author liujianyu.ljy
 * @date 2017/07/28
 */
public class ProduceRequestExtractor implements RequestExtractor {

    @Override
    public boolean support(RequestReceiver receiver) {
        return RequestTypes.PRODUCE == receiver.getRequestType();
    }

    @Override
    public Request extractRequest(RequestReceiver receiver) {
        ByteBuffer buffer = receiver.getReadableContentBuffer();
        String topic = ByteBufferUtils.readSizedString(buffer);
        int messageSize = buffer.getInt();
        ByteBuffer messageBuffer = buffer.slice();
        messageBuffer.limit(messageSize);
        buffer.position(buffer.position() + messageSize);
        return new ProducerRequest(topic, messageBuffer);

    }

}
