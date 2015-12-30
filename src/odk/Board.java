package odk;

import odk.accept.AcceptIOTask;
import odk.api.IOTask;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: operehod
 * Date: 30.12.2015
 * Time: 11:05
 */
public class Board {


    private static final Queue<Worker> WORKERS = new LinkedBlockingQueue<>();
    private static final Queue<IOTask> TASKS = new LinkedBlockingQueue<>();
    private static final AtomicInteger ACCEPT_TASK_COUNT = new AtomicInteger();

    public static void init(List<AcceptIOTask> startTask) {
        TASKS.addAll(startTask);
        ACCEPT_TASK_COUNT.set(startTask.size());
    }


    public static Queue<IOTask> tasks() {
        return TASKS;
    }


    public static void registerWorker(Worker worker) {
        WORKERS.add(worker);
    }

    public static void addTask(IOTask task) {
        TASKS.add(task);
        WORKERS.forEach(Worker::wakeup);
    }


    public static void reportAcceptTaskFail() {
        if (ACCEPT_TASK_COUNT.decrementAndGet() == 0) {
            addTask(new CloseIOTask());
        }
    }

}
