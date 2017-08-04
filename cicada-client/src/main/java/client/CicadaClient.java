package  client;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import me.destinyshine.cicada.broker.request.ProduceRequestDecoder;
import me.destinyshine.cicada.broker.request.ProducerRequest;
import me.destinyshine.cicada.broker.response.Acknowledge;

/**
 * Created by liujianyu.ljy on 17/8/4.
 *
 * @author liujianyu.ljy
 * @date 2017/08/04
 */
public class CicadaClient {

    private SocketChannel socketChannel;

    public CicadaClient(String serverHost, int serverPort) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress(serverHost, serverPort);
        socketChannel = SocketChannel.open(serverAddress);
    }

    public Acknowledge send(byte[] payload) throws IOException {
        ProducerRequest request = new ProducerRequest(
            (short)1,
            (short)2,
            3,
            "1.1.0",
            "mytopic",
            ByteBuffer.wrap(payload)
        );
        ByteBuffer frame = new ProduceRequestDecoder().encode(request);
        socketChannel.write(frame);
        return Acknowledge.OK;
    }

}
