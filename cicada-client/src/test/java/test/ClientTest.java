package test;

import java.io.IOException;

import client.CicadaClient;

/**
 * Created by liujianyu.ljy on 17/8/3.
 *
 * @author liujianyu.ljy
 * @date 2017/08/03
 */
public class ClientTest {

    public static void main(String[] args) throws IOException {
        CicadaClient client = new CicadaClient("127.0.0.1", 20178);
        client.send("hello".getBytes());
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
