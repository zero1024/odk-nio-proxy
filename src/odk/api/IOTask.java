package odk.api;

import odk.Worker;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 11:23
 * <p>
 * Некая "задача" которую должен выполнить worker. Задача состоит из ряда обработок различных событий на одном или нескольких сокетах.
 * Каждоый конкретной обработкой занимается IOEventHandler
 */
public interface IOTask {

    /**
     * Когда worker достает задачу из board, он вызывает этот метод, чтобы
     * привязать себя к этой задаче. Метод вызывает один раз.
     *
     * @param worker - поток, который будет выполнять данную задачу
     */
    public void register(Worker worker);

}