package me.destinyshine.cicada.broker.utils;

import java.nio.ByteBuffer;

/**
 * Created by liujianyu.ljy on 17/7/30.
 *
 * @author liujianyu.ljy
 * @date 2017/07/30
 */
public abstract class ByteBufferUtils {

    public static String readSizedString(ByteBuffer buffer) {
        int size = buffer.getInt();
        byte[] bytes = new byte[size];
        buffer.get(bytes);
        return new String(bytes);
    }
}
