package odk.event.transfer;

import odk.event.EventHandler;
import odk.util.TransferState;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 12:05
 */
public class ReadFromLocalEventHandler implements EventHandler {

    private TransferState state;


    public ReadFromLocalEventHandler(TransferState state) {
        this.state = state;
    }

    @Override
    public void handle(SelectionKey key) {
        try {
            int read = ((SocketChannel) key.channel()).read(state.getBuffer());
            if (read > 0) {
                state.sealBuffer();
                state.registerLocalEvent(0);
                if (state.isRemoteSocketConnected())
                    state.registerRemoteEvent(SelectionKey.OP_WRITE, new WriteEventHandler(state));
            } else if (read == -1) {
                state.closeTransfer();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
