package odk.event.transfer;

import odk.event.EventHandler;
import odk.task.TransferTask;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 12:05
 */
public class ConnectEventHandler implements EventHandler {


    private static final Logger logger = Logger.getLogger(ConnectEventHandler.class.getName());

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
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "Connecting has failed", e);
            }
            state.closeTransfer();
        }
    }


}
