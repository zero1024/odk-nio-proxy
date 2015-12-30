package odk;

import odk.accept.AcceptIOTask;
import odk.config.ProxyConfig;

import java.util.Collections;

/**
 * User: operehod
 * Date: 28.12.2015
 * Time: 19:53
 */
public class Runner {

    public static void main(String[] args) {

        ProxyConfig config = new ProxyConfig(8000, 80, "www.odnoklassniki.ru");

        int processors = Runtime.getRuntime().availableProcessors();


        Board.init(Collections.singletonList(new AcceptIOTask(config)));

        for (int i = 0; i < processors; i++) {
            new Thread(new Worker()).start();
        }

    }

}
