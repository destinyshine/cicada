package me.destinyshine.cicada.broker;

import java.util.ArrayList;
import java.util.List;

import me.destinyshine.cicada.broker.connector.RequestHandler;
import me.destinyshine.cicada.broker.connector.RequestFrame;
import me.destinyshine.cicada.broker.connector.Response;
import me.destinyshine.cicada.broker.request.ProduceRequestDecoder;
import me.destinyshine.cicada.broker.request.Request;
import me.destinyshine.cicada.broker.request.RequestDecoder;
import me.destinyshine.cicada.broker.request.handler.ProducerRequestHandler;

/**
 * Created by liujianyu.ljy on 17/7/28.
 *
 * @author liujianyu.ljy
 * @date 2017/07/28
 */
public class RequestDispatcher {
    private final List<RequestDecoder> requestDecoders = new ArrayList<>();

    private final List<RequestHandler> requestHandlers = new ArrayList<>();

    public RequestDispatcher() {
        this.requestDecoders.add(new ProduceRequestDecoder());
        this.requestHandlers.add(new ProducerRequestHandler());
    }

    public Response dispatch(RequestFrame receiver) {
        Request request = null;
        for (RequestDecoder decoder : requestDecoders) {
            if (decoder.support(receiver)) {
                request = decoder.decode(receiver);
                break;
            }
        }

        if (request == null) {
            throw new IllegalStateException("can not find extractor for request");
        }

        for (RequestHandler handler : requestHandlers) {
            if (handler.support(request)) {
                Response response = handler.handle(request);
                return response;
            }
        }

        return null;
    }

}
