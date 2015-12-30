package odk;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * User: operehod
 * Date: 30.12.2015
 * Time: 11:05
 */
public class Board {


    private static final Queue<Worker> WORKERS = new LinkedBlockingQueue<>();
    private static final Queue<IOTask> TASKS = new LinkedBlockingQueue<>();

    /**
     * Открытие и регистрация селектора
     */
    public static Queue<IOTask> registerWorker(Worker worker) {
        WORKERS.add(worker);
        return TASKS;
    }

    public static void addTask(IOTask task) {
        TASKS.add(task);
        WORKERS.forEach(Worker::wakeup);
    }
}
