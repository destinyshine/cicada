package me.destinyshine.cicada.broker.connector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by destinyliu on 2016/2/20.
 */
public class SocketChannelReceiver {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final SocketChannel channel;

    private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
    private ByteBuffer contentBuffer;
    private boolean completed;
    private int requestType;

    public SocketChannelReceiver(SocketChannel channel) {
        Objects.requireNonNull(channel, "channel can not be null.");
        this.channel = channel;
    }

    public void resolveBuffer() throws IOException {
        if (sizeBuffer.hasRemaining()) {
            int read = channel.read(sizeBuffer);
            if (read == -1) {
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
            }
        }

    }

    public boolean isCompleted() {
        return completed;
    }

    public int getRequestType() {
        return requestType;
    }
}
