package me.destinyshine.cicada.broker.connector;

import me.destinyshine.cicada.broker.request.Request;

/**
 * Created by liujianyu.ljy on 17/7/27.
 *
 * @author liujianyu.ljy
 * @date 2017/07/27
 */
public interface RequestHandler {

    boolean support(Request request);

    Response handle(Request request);
}
