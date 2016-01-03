package odk.event;

import odk.WorkBoard;
import odk.config.ProxyConfig;
import odk.task.TransferTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 11:53
 */
public class AcceptEventHandler implements EventHandler {


    private static final Logger logger = Logger.getLogger(AcceptEventHandler.class.getName());

    private ProxyConfig config;

    public AcceptEventHandler(ProxyConfig config) {
        this.config = config;
    }

    @Override
    public void handle(SelectionKey key) {
        if (key.isValid() && key.isAcceptable()) {
            try {

                ServerSocketChannel server = (ServerSocketChannel) key.channel();
                SocketChannel localChannel = server.accept();
                localChannel.configureBlocking(false);

                SocketChannel remoteChannel = SocketChannel.open();
                remoteChannel.configureBlocking(false);
                remoteChannel.connect(new InetSocketAddress(config.getRemoteHost(), config.getRemotePort()));

                WorkBoard.addTask(new TransferTask(localChannel, remoteChannel, config));
            } catch (IOException e) {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.log(Level.WARNING, "Transfer has failed. Local port : [" + config.getLocalPort() +
                            "]. Remote address: [" + config.getRemoteHost() + ":" + config.getRemotePort() + "]", e);
                }
            }
        }
    }
}
