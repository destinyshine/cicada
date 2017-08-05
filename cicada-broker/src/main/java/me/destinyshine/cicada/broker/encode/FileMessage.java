package me.destinyshine.cicada.broker.encode;

/**
 * Created by liujianyu.ljy on 17/8/5.
 *
 * @author liujianyu.ljy
 * @date 2017/08/05
 */
public class FileMessage {

    private long offset;
    private int size;
    private int crc32;
    private byte magic;
    private byte attribute;
    private byte[] key;
    private byte[] payload;
}
