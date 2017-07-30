package me.destinyshine.cicada.broker.connector;

import java.nio.ByteBuffer;

/**
 * Created by liujianyu.ljy on 17/7/27.
 *
 * @author liujianyu.ljy
 * @date 2017/07/27
 */
public interface Response {

    ByteBuffer toResponseBytes();
}
