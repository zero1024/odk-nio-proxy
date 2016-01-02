package odk.util;

import odk.event.EventHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * User: operehod
 * Date: 02.01.2016
 * Time: 12:19
 */
public class TransferState {


    private boolean remoteSocketConnected = false;
    private ByteBuffer buffer = ByteBuffer.allocate(1024);
    private boolean bufferFull = false;
    private SocketChannel localChannel;
    private SocketChannel remoteChannel;
    private Selector selector;


    public TransferState(SocketChannel localChannel, SocketChannel remoteChannel, Selector selector) {
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
