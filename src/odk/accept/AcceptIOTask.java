package odk.accept;

import odk.IOTask;
import odk.ProxyConfig;
import odk.Worker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 11:39
 */
public class AcceptIOTask implements IOTask {


    private ProxyConfig config;

    public AcceptIOTask(ProxyConfig config) {
        this.config = config;
    }

    @Override
    public void assignTaskToWorker(Worker worker) {
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.socket().bind(new InetSocketAddress(config.getLocalPort()));
            server.configureBlocking(false);
            server.register(worker.getSelector(), SelectionKey.OP_ACCEPT, new AcceptIOEventHandler(config));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
