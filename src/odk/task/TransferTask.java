package odk.task;

import odk.Worker;
import odk.config.ProxyConfig;
import odk.event.EventHandler;
import odk.event.transfer.ConnectEventHandler;
import odk.event.transfer.ReadFromLocalEventHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 14:57
 * Задача - передать данные с localChannel на remoteChannel, и обратно.
 */
public class TransferTask implements Task {

    private static final Logger logger = Logger.getLogger(TransferTask.class.getName());

    private SocketChannel localChannel;
    private SocketChannel remoteChannel;
    private ProxyConfig config;

    public TransferTask(SocketChannel localChannel, SocketChannel remoteChannel, ProxyConfig config) {
        this.localChannel = localChannel;
        this.remoteChannel = remoteChannel;
        this.config = config;
    }

    @Override
    /**
     * При регистрации задачи, создается два слушателя.
     * 1. Слушатель, ожидающий connect-а с удаленным хостом
     * 2. Слушатель, ожидающий поступления данных на локальный порт
     */
    public void register(Worker worker) {
        try {
            Selector selector = worker.getSelector();
            State state = new State(localChannel, remoteChannel, selector);
            remoteChannel.register(selector, SelectionKey.OP_CONNECT, new ConnectEventHandler(state));
            localChannel.register(selector, SelectionKey.OP_READ, new ReadFromLocalEventHandler(state));
        } catch (IOException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.log(Level.SEVERE, "Transfer is failed. Local port : [" + config.getLocalPort() +
                        "]. Remote address: [" + config.getRemoteHost() + ":" + config.getRemotePort() + "]", e);
            }
        }

    }

    /**
     * User: operehod
     * Date: 02.01.2016
     * Time: 12:19
     *
     * Класс, инкапсулирующий в себе состояние выполнения TransferTask.
     *
     */
    public static class State {


        private boolean remoteSocketConnected = false;
        private ByteBuffer buffer = ByteBuffer.allocate(1024);
        private boolean bufferFull = false;
        private SocketChannel localChannel;
        private SocketChannel remoteChannel;
        private Selector selector;


        public State(SocketChannel localChannel, SocketChannel remoteChannel, Selector selector) {
            this.localChannel = localChannel;
            this.remoteChannel = remoteChannel;
            this.selector = selector;
        }

        public boolean isBufferFull() {
            return bufferFull;
        }

        public ByteBuffer getBuffer() {
            return buffer;
        }

        public void sealBuffer() {
            buffer.flip();
            bufferFull = true;
        }


        public void clearBuffer() {
            buffer.clear();
            bufferFull = false;
        }


        public boolean isRemoteSocketConnected() {
            return remoteSocketConnected;
        }


        public void registerLocalEvent(int event) throws ClosedChannelException {
            localChannel.register(selector, event);
        }

        public void registerLocalEvent(int event, EventHandler handler) throws ClosedChannelException {
            localChannel.register(selector, event, handler);
        }


        public void registerRemoteEvent(int event, EventHandler handler) throws ClosedChannelException {
            remoteChannel.register(selector, event, handler);
        }

        public void registerRemoteEvent(int event) throws ClosedChannelException {
            remoteChannel.register(selector, event);
        }


        public void closeTransfer() throws IOException {
            remoteChannel.close();
            localChannel.close();
        }

        public void finishConnect() throws IOException {
            this.remoteSocketConnected = true;
            this.remoteChannel.finishConnect();

        }
    }
}
