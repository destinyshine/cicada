package me.destinyshine.cicada.broker.launch;

import java.io.IOException;

import me.destinyshine.cicada.broker.connector.SocketChannelConnector;

/**
 * Created by liujianyu.ljy on 17/7/26.
 *
 * @author liujianyu.ljy
 * @date 2017/07/26
 */
public class BrokerLauncher {

    public static void main(String[] args) throws IOException {
        new SocketChannelConnector("127.0.0.1", 2017).connect();
    }
}
