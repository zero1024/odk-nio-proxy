package odk;

import odk.event.EventHandler;
import odk.task.Task;

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

            if (logger.isLoggable(Level.INFO)) {
                logger.log(Level.INFO, "Worker started in thread: [{0}]", currentThread().getId());
            }

            this.selector = selector;
            cycleWork();

        } catch (IOException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, "Some problem with selector. Thread is stopped", e);
            }
        }

    }

    /**
     * Схема работы:
     * 1. worker ожидает io-событий и добавления новых задача в WorkBoard
     * 2. при появлении события, происходит стандартная обработка с использованием handler/attachment
     * 3. при появлении новой задачи поток регистриует ей у себя
     */
    private void cycleWork() throws IOException {
        while (!currentThread().isInterrupted()) {

            Task task = WorkBoard.tasks().poll();
            if (task != null) {
                task.register(this);
            }

            selector.select(100L);

            final Set<SelectionKey> keys = selector.selectedKeys();
            for (final SelectionKey key : keys) {
                EventHandler handler = (EventHandler) key.attachment();
                handler.handle(key);
            }
            keys.clear();
        }

        if (logger.isLoggable(Level.WARNING)) {
            logger.log(Level.WARNING, "Worker in thread stopped: [{0}]", currentThread().getId());
        }


    }

}
