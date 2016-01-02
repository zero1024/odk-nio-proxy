package odk.event;

import java.nio.channels.SelectionKey;

/**
 * User: operehod
 * Date: 28.12.2015
 * Time: 19:54
 * <p>
 * Обработчик событий на сокетах
 */
public interface EventHandler {

    public void handle(SelectionKey key);


}
