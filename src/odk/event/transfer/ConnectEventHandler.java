package odk.event.transfer;

import odk.event.EventHandler;
import odk.util.TransferState;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 12:05
 */
public class ConnectEventHandler implements EventHandler {

    private TransferState state;


    public ConnectEventHandler(TransferState state) {
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
