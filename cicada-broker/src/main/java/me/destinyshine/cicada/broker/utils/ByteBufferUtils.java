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
     *
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

    public static int getStringNeedSize(byte[] string) {
        return Short.BYTES + string.length;
    }

    public static int getBytesNeedSize(ByteBuffer bytes) {
        return Integer.BYTES + bytes.limit();
    }

    public static void putSizedString(ByteBuffer buffer, byte[] string) {
        buffer.putShort((short)string.length).put(string);
    }

    public static void putSizedString(ByteBuffer buffer, String string) {
        putSizedString(buffer, string.getBytes());
    }

    public static void putSizedBytes(ByteBuffer buffer, ByteBuffer bytes) {
        buffer.putInt(bytes.limit()).put(bytes);
    }
}
