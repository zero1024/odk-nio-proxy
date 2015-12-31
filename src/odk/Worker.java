package odk;

import odk.api.IOEventHandler;
import odk.api.IOTask;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.currentThread;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 11:25
 */
public class Worker implements Runnable {

    private static final Logger logger = Logger.getLogger(Worker.class.getName());

    private Selector selector;


    public Selector getSelector() {
        return selector;
    }


    public Worker() {
    }

    @Override
    public void run() {
        try (Selector selector = Selector.open()) {
            this.selector = selector;
            Board.registerWorker(this);
            cycleWork();
        } catch (IOException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, "Some problem with selector. Thread is stopped", e);
            }
        }

    }

    /**
     * Схема работы:
     * 1. поток ожидает io-событий в блокирующем режиме
     * 2. при появлении события, происходит стандартная обработка с использованием handler/attachment
     * 3. если в очередь задач будет добавлена новая задача, то все селекторы будут разбужены, и worker выйдет из блокировки и возможно получит эту задачу из регистра
     * 4. получив новую задачу он зарегистрирует её и будет ожидать io-событий связанных с этой задачей.
     */
    private void cycleWork() throws IOException {
        while (!currentThread().isInterrupted()) {

            IOTask task = Board.tasks().poll();
            if (task != null) {
                task.register(this);
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
