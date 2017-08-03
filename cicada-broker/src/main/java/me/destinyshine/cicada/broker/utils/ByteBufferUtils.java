package me.destinyshine.cicada.broker.utils;

import java.nio.ByteBuffer;

/**
 * Created by liujianyu.ljy on 17/7/30.
 *
 * @author liujianyu.ljy
 * @date 2017/07/30
 */
public abstract class ByteBufferUtils {

    /**
     * size int16
     * @param buffer
     * @return
     */
    public static String readSizedString(ByteBuffer buffer) {
        short size = buffer.getShort();
        byte[] bytes = new byte[size];
        buffer.get(bytes);
        return new String(bytes);
    }

    /**
     * size int32
     *
     * @param buffer
     * @return
     */
    public static ByteBuffer readSizedBytes(ByteBuffer buffer) {
        int messageSize = buffer.getInt();
        ByteBuffer messageBuffer = buffer.slice();
        messageBuffer.limit(messageSize);
        buffer.position(buffer.position() + messageSize);
        return buffer;
    }
}
