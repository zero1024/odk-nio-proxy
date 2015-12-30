package odk.api;

import odk.Board;
import odk.Worker;

/**
 * User: operehod
 * Date: 30.12.2015
 * Time: 21:55
 */
public class CloseIOTask implements IOTask {
    @Override
    public void register(Worker worker) {
        Thread.currentThread().interrupt();
        Board.addTask(this);
    }
}
