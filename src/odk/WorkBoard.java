package odk;

import odk.event.transfer.WriteEventHandler;
import odk.task.AcceptTask;
import odk.task.PoisonPill;
import odk.task.Task;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: operehod
 * Date: 30.12.2015
 * Time: 11:05
 */
public class WorkBoard {


    private static final Logger logger = Logger.getLogger(WriteEventHandler.class.getName());

    private static final Queue<Task> TASKS = new LinkedBlockingQueue<>();

    /**
     * Счетчик зарегистрированных AcceptTask (задач на ожидание подключения к серверу)
     */
    private static final AtomicInteger ACCEPT_TASK_COUNT = new AtomicInteger();

    public static void init(List<AcceptTask> startTasks) {
        TASKS.addAll(startTasks);
        ACCEPT_TASK_COUNT.set(startTasks.size());
    }


    public static Queue<Task> tasks() {
        return TASKS;
    }

    public static void addTask(Task task) {
        TASKS.add(task);
    }

    /**
     * Через этот метод worker сообщает об ошибке при регистрации AcceptTask.
     * Если все зарегистрированные AcceptTask будут провалены, то сервер будет остановлен
     */
    public static void reportAcceptTaskFail() {
        if (ACCEPT_TASK_COUNT.decrementAndGet() == 0) {

            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "All accept tasks has failed. Server is stopping...");
            }

            addTask(new PoisonPill());
        }
    }

}
