package me.destinyshine.cicada.broker.connector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author destinyliu
 * @date 2016/2/20
 */
public class RequestFrame {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
    private ByteBuffer contentBuffer;
    private boolean completed;

    public RequestFrame() {
    }

    public void resolveBuffer(SocketChannel channel) throws IOException {
        if (sizeBuffer.hasRemaining()) {
            int read = channel.read(sizeBuffer);
            if (read == -1) {
                logger.warn("channel closed.");
                throw new IllegalStateException("channel closed.");
            }
        }
        if (contentBuffer == null && !sizeBuffer.hasRemaining()) {
            sizeBuffer.rewind();
            int contentSize = sizeBuffer.getInt();
            contentBuffer = ByteBuffer.allocate(contentSize);
        }
        if (contentBuffer != null) {
            channel.read(contentBuffer);
            if (!contentBuffer.hasRemaining()) {
                this.completed = true;
                this.contentBuffer.rewind();
            }
        }

    }

    /**
     * @return
     */
    public ByteBuffer getReadableFrame() {
        return contentBuffer;
    }

    public boolean isCompleted() {
        return completed;
    }

}
