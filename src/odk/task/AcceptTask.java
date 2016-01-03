package odk.task;

import odk.WorkBoard;
import odk.event.AcceptEventHandler;
import odk.event.EventHandler;
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
 * Обработчик событий - AcceptEventHandler
 */
public class AcceptTask implements Task {

    private static final Logger logger = Logger.getLogger(AcceptTask.class.getName());

    private ProxyConfig config;
    private EventHandler handler;

    public AcceptTask(ProxyConfig config) {
        this.config = config;
        this.handler = new AcceptEventHandler(config);
    }

    @Override
    public void register(Worker worker) {
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.socket().bind(new InetSocketAddress(config.getLocalPort()));
            server.configureBlocking(false);
            server.register(worker.getSelector(), SelectionKey.OP_ACCEPT, handler);
        } catch (IOException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, "Cannot init server socket. Port [" + config.getLocalPort() + "]", e);
            }
            WorkBoard.reportAcceptTaskFail();
        }
    }
}
