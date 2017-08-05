package  client;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import me.destinyshine.cicada.broker.encode.ProduceRequestDecoder;
import me.destinyshine.cicada.broker.request.ProducerRequest;
import me.destinyshine.cicada.broker.request.ProducerRequestBuilder;
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
        ProducerRequest request = new ProducerRequestBuilder().requestType((short)1).apiVersion((short)2)
            .correlationId(3).clientId("1.1.0").topic("mytopic").payload(ByteBuffer.wrap(payload))
            .build();
        ByteBuffer frame = new ProduceRequestDecoder().encode(request);
        socketChannel.write(frame);
        return Acknowledge.OK;
    }

}
