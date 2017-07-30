package me.destinyshine.cicada.broker.request;

import me.destinyshine.cicada.broker.connector.RequestReceiver;

/**
 * Created by liujianyu.ljy on 17/7/28.
 *
 * @author liujianyu.ljy
 * @date 2017/07/28
 */
public interface RequestExtractor {

    boolean support(RequestReceiver receiver);

    Request extractRequest(RequestReceiver receiver);
}
