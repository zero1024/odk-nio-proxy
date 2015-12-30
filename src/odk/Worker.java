package odk;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 11:25
 */
public class Worker implements Runnable {

    private Selector selector;

    public Selector getSelector() {
        return selector;
    }

    @Override
    public void run() {
        try (Selector selector = Selector.open()) {
            this.selector = selector;
            cycleWork();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void cycleWork() throws IOException {
        while (!Thread.interrupted()) {
            IOTask task = IOTaskRegister.pollTask();
            if (task != null) {
                task.assignTaskToWorker(this);
            }

            selector.select(100L);

            final Set<SelectionKey> keys = selector.selectedKeys();
            for (final SelectionKey key : keys) {
                IOEventHandler handler = (IOEventHandler) key.attachment();
                handler.handle(key);
            }
            keys.clear();
        }
    }
}
