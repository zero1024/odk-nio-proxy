package odk;

import odk.api.IOTask;

/**
 * User: operehod
 * Date: 30.12.2015
 * Time: 21:55
 * <p>
 * Если не удалось выполнить ни одну AcceptIOTask, то существование прокси-сервера становится бессмысленным.
 * Поэтому создается задача CloseIOTask которая должна передаться всем worker-ам, чтобы они смогли остановить свое выполнение.
 */
public class CloseIOTask implements IOTask {
    @Override
    public void register(Worker worker) {
        Thread.currentThread().interrupt();
        Board.addTask(this);
    }
}
