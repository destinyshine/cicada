package me.destinyshine.cicada.broker.handler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;

import me.destinyshine.cicada.broker.connector.RequestHandler;
import me.destinyshine.cicada.broker.connector.Response;
import me.destinyshine.cicada.broker.encode.BytesMessage;
import me.destinyshine.cicada.broker.request.ProducerRequest;
import me.destinyshine.cicada.broker.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liujianyu.ljy on 17/7/30.
 *
 * @author liujianyu.ljy
 * @date 2017/07/30
 */
public class ProducerRequestHandler implements RequestHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean support(Request request) {
        return request instanceof ProducerRequest;
    }

    @Override
    public Response handle(Request request) {
        ProducerRequest producerRequest = (ProducerRequest)request;
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("handle request: {}" + request);
            }
            FileChannel fileChannel = new RandomAccessFile("messages.log", "rw").getChannel();
            ByteBuffer messageBytes = producerRequest.getMessage().getByteBuffer();
            ByteBuffer sizeBytes = ByteBuffer.allocate(Long.BYTES).putLong(getLastOffset());
            sizeBytes.flip();
            fileChannel.position(fileChannel.size());
            fileChannel.write(sizeBytes);
            fileChannel.write(messageBytes);
            fileChannel.force(true);
            fileChannel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private long getLastOffset() {
        return 1;
    }

}
