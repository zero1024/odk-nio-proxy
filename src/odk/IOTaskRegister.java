package odk;

import java.nio.channels.Selector;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 11:45
 * <p>
 * Регистр задач (IOTask)
 */
public class IOTaskRegister {

    private static final Queue<IOTask> IO_TASKS = new ConcurrentLinkedQueue<>();

    /**
     * Добавление новой задачи.
     */
    public static void registerTask(IOTask task) {
        IO_TASKS.add(task);
        // будим все созданные в приложении селекторы, чтобы потоки ожили и смогли получить новые задачи из регистра
        SelectorFactory.getRegisteredSelectors().forEach(Selector::wakeup);
    }

    /**
     * Достаем задачу
     */
    public static IOTask pollTask() {
        return IO_TASKS.poll();
    }

}
