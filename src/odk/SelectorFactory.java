package odk;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * User: operehod
 * Date: 30.12.2015
 * Time: 11:05
 */
public class SelectorFactory {


    public static List<Selector> list = new CopyOnWriteArrayList<>();

    public static Selector createSelector() {
        try {
            Selector res = Selector.open();
            list.add(res);
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
