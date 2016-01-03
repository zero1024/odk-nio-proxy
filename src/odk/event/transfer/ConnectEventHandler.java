package odk.event.transfer;

import odk.event.EventHandler;
import odk.task.TransferTask;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 12:05
 */
public class ConnectEventHandler implements EventHandler {

    private TransferTask.State state;


    public ConnectEventHandler(TransferTask.State state) {
        this.state = state;
    }

    @Override
    public void handle(SelectionKey key) {
        try {
            state.finishConnect();
            if (state.isBufferFull())
                state.registerRemoteEvent(SelectionKey.OP_WRITE, new WriteEventHandler(state));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
