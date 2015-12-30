package odk.accept;

import odk.Board;
import odk.api.IOEventHandler;
import odk.config.ProxyConfig;
import odk.transfer.TransferIOTask;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 11:53
 */
public class AcceptIOEventHandler implements IOEventHandler {

    private ProxyConfig config;

    public AcceptIOEventHandler(ProxyConfig config) {
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
                Board.addTask(new TransferIOTask(localChannel, remoteChannel));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
