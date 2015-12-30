package odk.transfer;

import odk.Worker;
import odk.api.IOTask;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 14:57
 */
public class TransferIOTask implements IOTask {

    private SocketChannel localChannel;
    private SocketChannel remoteChannel;

    public TransferIOTask(SocketChannel localChannel, SocketChannel remoteChannel) {
        this.localChannel = localChannel;
        this.remoteChannel = remoteChannel;
    }

    @Override
    public void register(Worker worker) {
        try {

            Selector selector = worker.getSelector();
            TransferIOEventHandler handler = new TransferIOEventHandler(localChannel, remoteChannel, selector);
            handler.registerRemoteEvent(SelectionKey.OP_CONNECT);
            handler.registerLocalEvent(SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
