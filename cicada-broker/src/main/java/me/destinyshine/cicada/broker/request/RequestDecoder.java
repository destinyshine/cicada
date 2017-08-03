package me.destinyshine.cicada.broker.request;

import java.nio.ByteBuffer;

import me.destinyshine.cicada.broker.connector.RequestFrame;

/**
 * Created by liujianyu.ljy on 17/7/28.
 *
 * @author liujianyu.ljy
 * @date 2017/07/28
 */
public interface RequestDecoder {

    boolean support(RequestFrame receiver);

    Request decode(RequestFrame receiver);

    ByteBuffer encode(Request request);
}
