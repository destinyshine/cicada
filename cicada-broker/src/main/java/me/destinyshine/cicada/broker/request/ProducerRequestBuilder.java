package me.destinyshine.cicada.broker.request;

import java.nio.ByteBuffer;

public class ProducerRequestBuilder {
    private short requestType;
    private short apiVersion;
    private int correlationId;
    private String clientId;
    private String topic;
    private ByteBuffer key;
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

    public ProducerRequestBuilder key(ByteBuffer key) {
        this.key = key;
        return this;
    }

    public ProducerRequestBuilder payload(ByteBuffer payload) {
        this.payload = payload;
        return this;
    }

    public ProducerRequest build() {
        return new ProducerRequest(requestType, apiVersion, correlationId, clientId, topic, key, payload);
    }
}