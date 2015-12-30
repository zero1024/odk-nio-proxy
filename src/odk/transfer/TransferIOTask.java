package odk.transfer;

import odk.IOTask;
import odk.ProxyConfig;
import odk.Worker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 14:57
 */
public class TransferIOTask implements IOTask {

    private ProxyConfig config;
    private SocketChannel localChannel;

    public TransferIOTask(ProxyConfig config, SocketChannel localChannel) {
        this.config = config;
        this.localChannel = localChannel;
    }

    @Override
    public void assignTaskToWorker(Worker worker) {
        try {
            Selector selector = worker.getSelector();
            SocketChannel remoteChannel = SocketChannel.open();
            remoteChannel.configureBlocking(false);
            remoteChannel.connect(new InetSocketAddress(config.getRemoteHost(), config.getRemotePort()));

            TransferIOEventHandler handler = new TransferIOEventHandler(localChannel, remoteChannel, selector);
            handler.registerRemoteEvent(SelectionKey.OP_CONNECT);
            handler.registerLocalEvent(SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
