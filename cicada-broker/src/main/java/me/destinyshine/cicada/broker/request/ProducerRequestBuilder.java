package me.destinyshine.cicada.broker.request;

import java.nio.ByteBuffer;

public class ProducerRequestBuilder {
    private short requestType;
    private short apiVersion;
    private int correlationId;
    private String clientId;
    private String topic;
    private ByteBuffer message;
    private ByteBuffer payload;

    public ProducerRequestBuilder requestType(short requestType) {
        this.requestType = requestType;
        return this;
    }

    public ProducerRequestBuilder apiVersion(short apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    public ProducerRequestBuilder correlationId(int correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public ProducerRequestBuilder clientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public ProducerRequestBuilder topic(String topic) {
        this.topic = topic;
        return this;
    }

    public ProducerRequestBuilder message(ByteBuffer message) {
        this.message = message;
        return this;
    }

    public ProducerRequest build() {
        return new ProducerRequest(requestType, apiVersion, correlationId, clientId, topic, message);
    }
}