package test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.stream.Stream;

import me.destinyshine.cicada.broker.request.ProduceRequestDecoder;
import me.destinyshine.cicada.broker.request.ProducerRequest;

/**
 * Created by liujianyu.ljy on 17/8/3.
 *
 * @author liujianyu.ljy
 * @date 2017/08/03
 */
public class ClientTest {

    public static void main(String[] args) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1", 20178);
        SocketChannel channel = SocketChannel.open(serverAddress);
        ProducerRequest request = new ProducerRequest(
            (short)1,
            (short)2,
            3,
            "1.1.0",
            "mytopic",
            ByteBuffer.wrap("hello".getBytes())
        );
        ByteBuffer frame = new ProduceRequestDecoder().encode(request);
        for (byte b : frame.array()) {
            System.out.print(b + ",");
        }
        channel.write(frame);
        //channel.finishConnect();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
