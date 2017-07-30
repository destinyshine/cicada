package me.destinyshine.cicada.broker.connector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于nio 1的连接器。
 * Created by destinyliu on 2016/2/20.
 */
public class SocketChannelConnector {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ServerSocketChannel serverSocketChannel;

    private ExecutorService executorService;

    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    private String host;
    private int port;

    private Selector acceptSelector;

    private ConnectionsSelectorRunner[] selectorRunners;

    private int maxtonnectionsPerSelector = 10;

    private int connectionsNum = 0;
    private int readThreadsNum;


    public SocketChannelConnector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {

        readThreadsNum = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(readThreadsNum + 1);

        initAcceptThread();
        initReadThreads();
    }

    private void initAcceptThread() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(this.host, this.port));
        serverSocketChannel.configureBlocking(false);
        acceptSelector = Selector.open();
        serverSocketChannel.register(acceptSelector, SelectionKey.OP_ACCEPT);
        executorService.submit((Runnable)() -> {
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
    }

    private void initReadThreads() throws IOException {
        //初始化
        selectorRunners = new ConnectionsSelectorRunner[readThreadsNum];
        for (int i = 0; i < readThreadsNum; i++) {
            ConnectionsSelectorRunner selectorRunner = new ConnectionsSelectorRunner(Selector.open());
            executorService.submit(selectorRunner);
            selectorRunners[i] = selectorRunner;
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
                ConnectionsSelectorRunner selectorRunner = selectorRunners[connectionsNum % selectorRunners.length];
                selectorRunner.pushToRegister(socketChannel, SelectionKey.OP_READ);
                connectionsNum++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ConnectionsSelectorRunner implements Runnable {

        private Selector selector;

        private int connections = 0;

        private ConcurrentLinkedQueue<RegisterArgs> waitingRegisterQueue;

        public ConnectionsSelectorRunner(Selector selector) {
            this.selector = selector;
            this.waitingRegisterQueue = new ConcurrentLinkedQueue<>();
        }

        @Override
        public void run() {
            logger.info("start read thread-" + Thread.currentThread().getName());

            while (true) {
                try {
                    RegisterArgs regArgs;
                    while ((regArgs = waitingRegisterQueue.poll()) != null) {
                        SelectionKey key = regArgs.channel.register(selector, regArgs.opts);
                        if (logger.isDebugEnabled()) {
                            logger.debug("register channel: %s, opts: %d", regArgs.channel.toString(), regArgs.opts);
                        }
                        connections++;
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
                if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel)key.channel();
                    RequestReceiver receiver = (RequestReceiver)key.attachment();
                    if (receiver == null) {
                        key.attach(new RequestReceiver(socketChannel));
                    }
                    receiver.resolveBuffer();
                    if (receiver.isCompleted()) {

                        key.attach(null);
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
