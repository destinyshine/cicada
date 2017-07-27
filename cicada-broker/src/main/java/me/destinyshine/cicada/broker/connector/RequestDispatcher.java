package me.destinyshine.cicada.broker.connector;

import java.util.HashMap;
import java.util.Map;

import me.destinyshine.cicada.broker.connector.handler.ProduceMessageHandler;

/**
 * Created by liujianyu.ljy on 17/7/27.
 *
 * @author liujianyu.ljy
 * @date 2017/07/27
 */
public class RequestDispatcher {

    private final Map<Integer, Class<? extends RequestHandler>> handlerMapping = new HashMap<>();

    {
        handlerMapping.put(RequestTypes.PRODUCE, ProduceMessageHandler.class);
    }

    public void dispatch(SocketChannelReceiver receiver)
        throws IllegalAccessException, InstantiationException {
        handlerMapping.get(receiver.getRequestType()).newInstance().handle(receiver);
    }
}
