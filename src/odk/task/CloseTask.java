package odk.task;

import odk.WorkBoard;
import odk.Worker;

/**
 * User: operehod
 * Date: 30.12.2015
 * Time: 21:55
 * <p>
 * Если не удалось выполнить ни одну AcceptIOTask, то существование прокси-сервера становится бессмысленным.
 * Поэтому создается задача CloseIOTask которая должна передаться всем worker-ам, чтобы они смогли остановить свое выполнение.
 */
public class CloseTask implements Task {
    @Override
    public void register(Worker worker) {
        Thread.currentThread().interrupt();
        WorkBoard.addTask(this);
    }
}
