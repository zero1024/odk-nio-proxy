package odk;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 11:25
 */
public class Worker implements Runnable {

    private static final Logger logger = Logger.getLogger(Worker.class.getName());

    private Selector selector;
    private Queue<IOTask> tasks;


    public void wakeup() {
        this.selector.wakeup();
    }

    public Selector getSelector() {
        return selector;
    }


    public Worker() {
    }

    @Override
    public void run() {
        try (Selector selector = Selector.open()) {
            this.selector = selector;
            this.tasks = Board.registerWorker(this);
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
     * 3. если в регистр задач будет добавлена новая задача, то все селекторы будут разбужены, и worker выйдет из блокировки и возможно получит эту задачу из регистра
     * 4. получив новую задачу он зарегистрирует её и будем ожидать io-событий связанных с этой задачей.
     */
    private void cycleWork() throws IOException {
        while (!Thread.interrupted()) {

            IOTask task = tasks.poll();
            if (task != null) {
                task.assignTask(this);
            }

            selector.select();

            final Set<SelectionKey> keys = selector.selectedKeys();
            for (final SelectionKey key : keys) {
                IOEventHandler handler = (IOEventHandler) key.attachment();
                handler.handle(key);
            }
            keys.clear();
        }
    }

}
