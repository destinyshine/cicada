package me.destinyshine.cicada.broker;

import java.util.ArrayList;
import java.util.List;

import me.destinyshine.cicada.broker.connector.RequestHandler;
import me.destinyshine.cicada.broker.connector.RequestReceiver;
import me.destinyshine.cicada.broker.connector.Response;
import me.destinyshine.cicada.broker.request.ProduceRequestExtractor;
import me.destinyshine.cicada.broker.request.Request;
import me.destinyshine.cicada.broker.request.RequestExtractor;

/**
 * Created by liujianyu.ljy on 17/7/28.
 *
 * @author liujianyu.ljy
 * @date 2017/07/28
 */
public class RequestDispatcher {
    private final List<RequestExtractor> requestExtractors = new ArrayList<>();

    private final List<RequestHandler> requestHandlers = new ArrayList<>();

    private RequestDispatcher() {
        this.requestExtractors.add(new ProduceRequestExtractor());
    }

    public Response dispatch(RequestReceiver receiver) {
        Request request = null;
        for (RequestExtractor extractor : requestExtractors) {
            if (extractor.support(receiver)) {
                request = extractor.extractRequest(receiver);
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
