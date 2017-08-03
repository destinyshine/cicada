package me.destinyshine.cicada.broker.request;

/**
 * @author destinyliu
 */
public class AbstractRequest implements Request {

    protected final short requestType;

    protected final short apiVersion;

    protected final int correlationId;

    protected final String clientId;

    public AbstractRequest(short requestType, short apiVersion, int correlationId, String clientId) {
        this.requestType = requestType;
        this.apiVersion = apiVersion;
        this.correlationId = correlationId;
        this.clientId = clientId;
    }

    public short getRequestType() {
        return requestType;
    }

    public short getApiVersion() {
        return apiVersion;
    }

    public int getCorrelationId() {
        return correlationId;
    }

    public String getClientId() {
        return clientId;
    }
}
