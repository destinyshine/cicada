package me.destinyshine.cicada.broker.encode;

import java.nio.ByteBuffer;

import me.destinyshine.cicada.broker.connector.RequestTypes;
import me.destinyshine.cicada.broker.connector.RequestFrame;
import me.destinyshine.cicada.broker.request.ProducerRequest;
import me.destinyshine.cicada.broker.request.ProducerRequestBuilder;
import me.destinyshine.cicada.broker.request.Request;
import me.destinyshine.cicada.broker.utils.ByteBufferUtils;

/**
 * Created by liujianyu.ljy on 17/7/28.
 *
 * @author liujianyu.ljy
 * @date 2017/07/28
 */
public class ProduceRequestDecoder implements RequestDecoder {

    @Override
    public boolean support(RequestFrame frame) {
        ByteBuffer buf = frame.getReadableFrame();
        return RequestTypes.PRODUCE == buf.get(buf.position());
    }

    @Override
    public Request decode(RequestFrame frame) {
        ByteBuffer buffer = frame.getReadableFrame();
        short requestType = buffer.getShort();
        short apiVersion = buffer.getShort();
        int correlationId = buffer.getInt();
        String clientId = ByteBufferUtils.readSizedString(buffer);
        String topic = ByteBufferUtils.readSizedString(buffer);
        ByteBuffer key = ByteBufferUtils.readSizedBytes(buffer);
        ByteBuffer payload = ByteBufferUtils.readSizedBytes(buffer);
        return new ProducerRequestBuilder()
            .requestType(requestType)
            .apiVersion(apiVersion)
            .correlationId(correlationId)
            .clientId(clientId)
            .topic(topic)
            .key(key)
            .payload(payload)
            .build();

    }

    @Override
    public ByteBuffer encode(Request request) {

        ProducerRequest producerRequest = (ProducerRequest)request;

        short requestType = producerRequest.getRequestType();
        short apiVersion = producerRequest.getApiVersion();
        int correlationId = producerRequest.getCorrelationId();
        byte[] topicBytes = producerRequest.getTopic().getBytes();
        ByteBuffer key = producerRequest.getKey();
        ByteBuffer payload = producerRequest.getPayload();

        int frameSize = Short.BYTES + Short.BYTES + Integer.BYTES
            + ByteBufferUtils.getStringNeedSize(topicBytes)
            + ByteBufferUtils.getBytesNeedSize(key)
            + ByteBufferUtils.getBytesNeedSize(payload);

        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES + frameSize);

        buffer.putInt(frameSize);

        buffer.putShort(requestType);
        buffer.putShort(apiVersion);
        buffer.putInt(correlationId);
        ByteBufferUtils.putSizedString(buffer, topicBytes);
        ByteBufferUtils.putSizedBytes(buffer, key);
        ByteBufferUtils.putSizedBytes(buffer, payload);

        buffer.rewind();

        return buffer;

    }

}
