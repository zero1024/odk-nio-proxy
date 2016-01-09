package odk;

import odk.config.ProxyConfigParserTest;

/**
 * User: operehod
 * Date: 09.01.2016
 * Time: 11:06
 */
public class TestRunner {

    // запуск тестов
    public static void main(String[] args) {
        ProxyConfigParserTest.testParseOneConfig();
        ProxyConfigParserTest.testParseTwoConfigs();
    }


    //util для тестов
    public static void assertTrue(boolean bool) {
        if (!bool)
            fail();
    }

    public static void assertException(Callback callback, Class<? extends Exception> exceptionClass) {
        try {
            callback.doSome();
        } catch (Exception e) {
            if (e.getClass().equals(exceptionClass))
                return;
        }
        fail();
    }

    private static void fail() {
        throw new RuntimeException("Tests was failed");
    }

    public static interface Callback {
        public void doSome();
    }


}
