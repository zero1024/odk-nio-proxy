package odk.accept;

import odk.IOEventHandler;
import odk.IOTaskRegister;
import odk.ProxyConfig;
import odk.transfer.TransferIOTask;

import java.io.IOException;
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
                IOTaskRegister.addTask(new TransferIOTask(config, localChannel));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}