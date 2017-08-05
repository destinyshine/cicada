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
        ByteBuffer message = ByteBufferUtils.readSizedBytes(buffer);
        return new ProducerRequestBuilder()
            .requestType(requestType)
            .apiVersion(apiVersion)
            .correlationId(correlationId)
            .clientId(clientId)
            .topic(topic)
            .message(message)
            .build();

    }

    @Override
    public ByteBuffer encode(Request request) {

        ProducerRequest producerRequest = (ProducerRequest)request;

        short requestType = producerRequest.getRequestType();
        short apiVersion = producerRequest.getApiVersion();
        int correlationId = producerRequest.getCorrelationId();
        byte[] clientId = producerRequest.getClientId().getBytes();
        byte[] topicBytes = producerRequest.getTopic().getBytes();
        ByteBuffer message = producerRequest.getMessage().getByteBuffer();

        int frameSize = Short.BYTES + Short.BYTES + Integer.BYTES
            + ByteBufferUtils.getStringNeedSize(clientId)
            + ByteBufferUtils.getStringNeedSize(topicBytes)
            + ByteBufferUtils.getBytesNeedSize(message);

        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES + frameSize);

        buffer.putInt(frameSize);

        buffer.putShort(requestType);
        buffer.putShort(apiVersion);
        buffer.putInt(correlationId);
        ByteBufferUtils.putSizedString(buffer, clientId);
        ByteBufferUtils.putSizedString(buffer, topicBytes);
        ByteBufferUtils.putSizedBytes(buffer, message);

        buffer.rewind();

        return buffer;

    }

}
