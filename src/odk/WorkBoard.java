package odk;

import odk.task.AcceptTask;
import odk.task.CloseTask;
import odk.task.Task;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: operehod
 * Date: 30.12.2015
 * Time: 11:05
 */
public class WorkBoard {


    private static final Queue<Task> TASKS = new LinkedBlockingQueue<>();
    private static final AtomicInteger ACCEPT_TASK_COUNT = new AtomicInteger();

    public static void init(List<AcceptTask> startTask) {
        TASKS.addAll(startTask);
        ACCEPT_TASK_COUNT.set(startTask.size());
    }


    public static Queue<Task> tasks() {
        return TASKS;
    }

    public static void addTask(Task task) {
        TASKS.add(task);
    }


    public static void reportAcceptTaskFail() {
        if (ACCEPT_TASK_COUNT.decrementAndGet() == 0) {
            addTask(new CloseTask());
        }
    }

}
