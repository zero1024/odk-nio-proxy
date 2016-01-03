package odk.event.transfer;

import odk.event.EventHandler;
import odk.task.TransferTask;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 12:05
 */
public class ReadFromRemoteEventHandler implements EventHandler {

    private static final Logger logger = Logger.getLogger(ReadFromRemoteEventHandler.class.getName());

    private TransferTask.State state;

    public ReadFromRemoteEventHandler(TransferTask.State state) {
        this.state = state;
    }

    @Override
    public void handle(SelectionKey key) {
        try {
            int read = ((SocketChannel) key.channel()).read(state.getBuffer());
            if (read > 0) {
                state.sealBuffer();
                state.clearRemoteEvents();
                state.registerLocalEvent(SelectionKey.OP_WRITE, new WriteEventHandler(state));
            } else if (read == -1) {
                state.closeTransfer();
            }
        } catch (IOException e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "Reading has failed", e);
            }
            state.closeTransfer();
        }


    }


}
