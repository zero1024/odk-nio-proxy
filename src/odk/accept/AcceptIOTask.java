package odk.accept;

import odk.IOEventHandler;
import odk.IOTask;
import odk.config.ProxyConfig;
import odk.Worker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 11:39
 * <p>
 * Задача - ожидать запросов на подключение к сокету. Такие задачи регистрируются единожды при старте приложения.
 * Связанные с этой задачей события - SelectionKey.OP_ACCEPT
 * Обработчик событий - AcceptIOEventHandler
 */
public class AcceptIOTask implements IOTask {

    private static final Logger logger = Logger.getLogger(Worker.class.getName());

    private ProxyConfig config;
    private IOEventHandler handler;

    public AcceptIOTask(ProxyConfig config) {
        this.config = config;
        this.handler = new AcceptIOEventHandler(config);
    }

    @Override
    public void assignTask(Worker worker) {
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.socket().bind(new InetSocketAddress(config.getLocalPort()));
            server.configureBlocking(false);
            server.register(worker.getSelector(), SelectionKey.OP_ACCEPT, handler);
        } catch (IOException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, "Cannot init server socket. Port [" + config.getLocalPort() + "]", e);
            }
        }
    }
}
