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


    private static final List<Selector> REGISTER = new CopyOnWriteArrayList<>();

    /**
     * Открытие и регистрация селектора
     */
    public static Selector openSelector() throws IOException {
        Selector res = Selector.open();
        REGISTER.add(res);
        return res;
    }

    public static List<Selector> getRegisteredSelectors() {
        return REGISTER;
    }
}
