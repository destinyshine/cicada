package me.destinyshine.cicada.broker.encode;

import java.nio.ByteBuffer;

/**
 * Created by liujianyu.ljy on 17/8/5.
 *
 * @author liujianyu.ljy
 * @date 2017/08/05
 */
public class BytesMessage {

    //private int size;
    // private int crc32;
    // private byte magic;
    // private byte attribute;
    //private byte[] key;
    //private byte[] payload;

    private final ByteBuffer byteBuffer;

    public BytesMessage(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

}
