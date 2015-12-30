package odk;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 11:45
 */
public class IOTaskRegister {

    private static final Queue<IOTask> IO_TASKS = new ConcurrentLinkedQueue<>();

    public static void addTask(IOTask task) {
        IO_TASKS.add(task);
    }

    public static IOTask pollTask() {
        return IO_TASKS.poll();
    }

}
