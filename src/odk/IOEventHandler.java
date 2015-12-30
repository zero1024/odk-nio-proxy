package odk;

import java.nio.channels.SelectionKey;

/**
 * User: operehod
 * Date: 28.12.2015
 * Time: 19:54
 * <p>
 * Обработка событий на сокетах
 */
public interface IOEventHandler {

    public void handle(SelectionKey key);


}
