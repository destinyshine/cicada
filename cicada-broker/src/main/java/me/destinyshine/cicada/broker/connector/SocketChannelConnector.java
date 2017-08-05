package me.destinyshine.cicada.broker.connector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import me.destinyshine.cicada.broker.handler.RequestDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于nio 1的连接器。
 * Created by destinyliu on 2016/2/20.
 */
public class SocketChannelConnector {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ServerSocketChannel serverSocketChannel;

    private Thread acceptorExecutor;
    private Reactor[] reactorExecutors;

    private String host;
    private int port;

    private Selector acceptSelector;

    private int connectionsNum = 0;
    private int reactorThreadsNum;

    private RequestDispatcher requestDispatcher = new RequestDispatcher();

    public SocketChannelConnector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {

        reactorThreadsNum = Runtime.getRuntime().availableProcessors() * 2;

        initAcceptThread();
        initReactExecutors();
    }

    private void initAcceptThread() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(this.host, this.port));
        serverSocketChannel.configureBlocking(false);
        acceptSelector = Selector.open();
        serverSocketChannel.register(acceptSelector, SelectionKey.OP_ACCEPT);
        acceptorExecutor = new Thread(() -> {
            while (true) {
                try {
                    acceptSelector.select();
                    Iterator<SelectionKey> selKeyIt = acceptSelector.selectedKeys().iterator();
                    while (selKeyIt.hasNext()) {
                        SelectionKey key = selKeyIt.next();
                        handleAcceptable(key);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        acceptorExecutor.start();
    }

    private void initReactExecutors() throws IOException {
        //初始化
        reactorExecutors = new Reactor[reactorThreadsNum];
        for (int i = 0; i < reactorThreadsNum; i++) {
            Reactor reactor = new Reactor(Selector.open());
            reactor.start();
            reactorExecutors[i] = reactor;
        }
    }

    private void handleAcceptable(SelectionKey key) {
        try {
            if (key.isAcceptable()) {
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (socketChannel == null) {
                    return;
                }
                socketChannel.configureBlocking(false);
                //挑选一个Selector
                Reactor reactor = reactorExecutors[connectionsNum % reactorExecutors.length];
                reactor.pushToRegister(socketChannel, SelectionKey.OP_READ);
                connectionsNum++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class Reactor {

        private Selector selector;

        private ConcurrentLinkedQueue<RegisterArgs> waitingRegisterQueue;

        private Thread thread;

        public Reactor(Selector selector) {
            this.selector = selector;
            this.waitingRegisterQueue = new ConcurrentLinkedQueue<>();
            this.thread = new Thread(this::doSelectLoop);
        }

        public void start() {
            this.thread.start();
        }

        private void doSelectLoop() {
            logger.info("start read thread-" + Thread.currentThread().getName());

            while (true) {
                try {
                    RegisterArgs regArgs;
                    while ((regArgs = waitingRegisterQueue.poll()) != null) {
                        SelectionKey key = regArgs.channel.register(selector, regArgs.opts);
                        if (logger.isDebugEnabled()) {
                            logger.debug("register channel: %s, opts: %d", regArgs.channel.toString(), regArgs.opts);
                        }
                    }
                    selector.select();
                    Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                    while (selectionKeyIterator.hasNext()) {
                        SelectionKey key = selectionKeyIterator.next();
                        handleReadableSelection(key);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                } finally {
                }
            }
        }

        private void handleReadableSelection(SelectionKey key) {
            try {
                if (key.isValid() && key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel)key.channel();
                    RequestFrame requestFrame = (RequestFrame)key.attachment();
                    if (requestFrame == null) {
                        requestFrame = new RequestFrame();
                        key.attach(requestFrame);
                    }
                    try {
                        requestFrame.resolveBuffer(socketChannel);
                    } catch (IOException e) {
                        socketChannel.close();
                        key.cancel();
                    }
                    if (requestFrame.isCompleted()) {
                        key.attach(null);
                        requestDispatcher.dispatch(requestFrame);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public SelectionKey pushToRegister(SocketChannel channel, int opts) {
            this.waitingRegisterQueue.add(new RegisterArgs(channel, opts));
            selector.wakeup();
            if (logger.isDebugEnabled()) {
                logger.debug("wakeuped selector.");
            }
            return null;
        }

    }

    static class RegisterArgs {
        SocketChannel channel;
        int opts;

        public RegisterArgs(SocketChannel channel, int opts) {
            this.channel = channel;
            this.opts = opts;
        }
    }

}
