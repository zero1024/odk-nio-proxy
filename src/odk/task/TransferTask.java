package odk.task;

import odk.Worker;
import odk.event.transfer.ConnectEventHandler;
import odk.event.transfer.ReadFromLocalEventHandler;
import odk.util.TransferState;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 14:57
 */
public class TransferTask implements Task {

    private SocketChannel localChannel;
    private SocketChannel remoteChannel;

    public TransferTask(SocketChannel localChannel, SocketChannel remoteChannel) {
        this.localChannel = localChannel;
        this.remoteChannel = remoteChannel;
    }

    @Override
    public void register(Worker worker) {
        try {
            Selector selector = worker.getSelector();
            TransferState state = new TransferState(localChannel, remoteChannel, selector);
            remoteChannel.register(selector, SelectionKey.OP_CONNECT, new ConnectEventHandler(state));
            localChannel.register(selector, SelectionKey.OP_READ, new ReadFromLocalEventHandler(state));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
