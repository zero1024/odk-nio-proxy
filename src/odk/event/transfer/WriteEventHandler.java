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
public class WriteEventHandler implements EventHandler {

    private static final Logger logger = Logger.getLogger(WriteEventHandler.class.getName());

    private TransferTask.State state;

    public WriteEventHandler(TransferTask.State state) {
        this.state = state;
    }

    @Override
    public void handle(SelectionKey key) {
        try {
            int write = ((SocketChannel) key.channel()).write(state.getBuffer());
            if (write > 0) {
                if (state.getBuffer().remaining() == 0) {
                    state.clearBuffer();
                    state.registerLocalEvent(SelectionKey.OP_READ, new ReadFromLocalEventHandler(state));
                    state.registerRemoteEvent(SelectionKey.OP_READ, new ReadFromRemoteEventHandler(state));
                }
            } else if (write == -1) {
                state.closeTransfer();
            }
        } catch (IOException e) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "Writing has failed", e);
            }
            state.closeTransfer();
        }


    }


}
