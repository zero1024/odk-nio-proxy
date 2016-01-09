package odk.config;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static odk.TestRunner.assertException;
import static odk.TestRunner.assertTrue;

/**
 * User: operehod
 * Date: 04.01.2016
 * Time: 12:07
 */
public class ProxyConfigParserTest {

    public static void testParseOneConfig() {
        //1. хороший сценарий
        Properties properties = new Properties();
        properties.setProperty("proxy1.localPort", "8080");
        properties.setProperty("proxy1.remoteHost", "www.odk.ru");
        properties.setProperty("proxy1.remotePort", "80");
        List<ProxyConfig> list = ProxyConfigParser.parse(properties);
        assertTrue(list.size() == 1);
        assertTrue(list.get(0).getName().equals("proxy1"));
        assertTrue(list.get(0).getLocalPort() == 8080);
        assertTrue(list.get(0).getRemoteHost().equals("www.odk.ru"));
        assertTrue(list.get(0).getRemotePort() == 80);

        //2. сделаем localPort строкой
        properties.clear();
        properties.setProperty("proxy1.localPort", "error");
        properties.setProperty("proxy1.remoteHost", "www.odk.ru");
        properties.setProperty("proxy1.remotePort", "80");
        assertException(() -> ProxyConfigParser.parse(properties), IllegalArgumentException.class);

        //3. сделаем remotePort строкой
        properties.clear();
        properties.setProperty("proxy1.localPort", "8080");
        properties.setProperty("proxy1.remoteHost", "www.odk.ru");
        properties.setProperty("proxy1.remotePort", "error");
        assertException(() -> ProxyConfigParser.parse(properties), IllegalArgumentException.class);

        //4. удалим одно из свойств
        properties.clear();
        properties.setProperty("proxy1.localPort", "8080");
        properties.setProperty("proxy1.remoteHost", "www.odk.ru");
        assertException(() -> ProxyConfigParser.parse(properties), IllegalArgumentException.class);

        //5. добавим лишнее свойство
        properties.clear();
        properties.setProperty("proxy1.localPort", "8080");
        properties.setProperty("proxy1.remoteHost", "www.odk.ru");
        properties.setProperty("proxy1.remotePort", "80");
        properties.setProperty("unexpectedKey", "unexpectedValue");
        assertException(() -> ProxyConfigParser.parse(properties), IllegalArgumentException.class);
    }


    public static void testParseTwoConfigs() {
        Properties properties = new Properties();
        properties.setProperty("proxy1.localPort", "8080");
        properties.setProperty("proxy1.remoteHost", "www.odk.ru");
        properties.setProperty("proxy1.remotePort", "80");
        properties.setProperty("proxy2.localPort", "8081");
        properties.setProperty("proxy2.remoteHost", "www.vk.ru");
        properties.setProperty("proxy2.remotePort", "81");
        List<ProxyConfig> list = ProxyConfigParser.parse(properties);
        Collections.sort(list, (ProxyConfig c1, ProxyConfig c2) -> c1.getName().compareTo(c2.getName()));
        assertTrue(list.size() == 2);
        assertTrue(list.get(0).getName().equals("proxy1"));
        assertTrue(list.get(0).getLocalPort() == 8080);
        assertTrue(list.get(0).getRemoteHost().equals("www.odk.ru"));
        assertTrue(list.get(0).getRemotePort() == 80);
        assertTrue(list.get(1).getName().equals("proxy2"));
        assertTrue(list.get(1).getLocalPort() == 8081);
        assertTrue(list.get(1).getRemoteHost().equals("www.vk.ru"));
        assertTrue(list.get(1).getRemotePort() == 81);
    }





}
