package odk.transfer;

import odk.api.IOEventHandler;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * User: operehod
 * Date: 29.12.2015
 * Time: 12:05
 */
public class TransferIOEventHandler implements IOEventHandler {

    private boolean remoteServerIsConnected = false;
    private SocketChannel localChannel;
    private SocketChannel remoteChannel;
    private Selector selector;

    private BufferWrapper buffer = new BufferWrapper();

    SelectionKey local;
    SelectionKey remote;


    public TransferIOEventHandler(SocketChannel localChannel, SocketChannel remoteChannel, Selector selector) {
        this.localChannel = localChannel;
        this.remoteChannel = remoteChannel;
        this.selector = selector;
    }

    @Override
    public void handle(SelectionKey key) {

        try {
            if (key.channel() == localChannel) {
                if (key.isValid() && key.isReadable()) {
                    readFromLocal(key);
                }
                if (key.isValid() && key.isWritable()) {
                    writeToLocal(key);
                }

            } else if (key.channel() == remoteChannel) {

                if (key.isValid() && key.isConnectable()) {
                    connectToRemote(key);
                }
                if (key.isValid() && key.isReadable()) {
                    readFromRemote(key);
                }
                if (key.isValid() && key.isWritable()) {
                    writeToRemote(key);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void connectToRemote(SelectionKey key) throws IOException {
        remoteServerIsConnected = true;
        remoteChannel.finishConnect();
        if (buffer.isFull())
            remote.interestOps(SelectionKey.OP_WRITE);
    }


    private void readFromLocal(SelectionKey key) throws IOException {
        int read = localChannel.read(buffer.get());

        if (read > 0) {
            buffer.seal();
            local.interestOps(0);
            if (remoteServerIsConnected)
                remote.interestOps(SelectionKey.OP_WRITE);
        } else if (read == -1) {
            remoteChannel.close();
            localChannel.close();
        }
    }

    private void readFromRemote(SelectionKey key) throws IOException {
        int read = remoteChannel.read(buffer.get());

        if (read > 0) {
            buffer.seal();
            remote.interestOps(0);
            local.interestOps(SelectionKey.OP_WRITE);
        } else if (read == -1) {
            remoteChannel.close();
            localChannel.close();
        }
    }


    private void writeToLocal(SelectionKey key) throws IOException {
        int read = localChannel.write(buffer.get());

        if (buffer.get().remaining() == 0) {
            buffer.clear();
            local.interestOps(SelectionKey.OP_READ);
            remote.interestOps(SelectionKey.OP_READ);
        }
    }

    private void writeToRemote(SelectionKey key) throws IOException {
        remoteChannel.write(buffer.get());

        if (buffer.get().remaining() == 0) {
            buffer.clear();
            remote.interestOps(SelectionKey.OP_READ);
            local.interestOps(SelectionKey.OP_READ);
        }
    }


    public void registerRemoteEvent(int event) throws ClosedChannelException {
        remote = remoteChannel.register(selector, event, this);
    }

    public void registerLocalEvent(int event) throws ClosedChannelException {
        local = localChannel.register(selector, event, this);
    }


    public static class BufferWrapper {
        private ByteBuffer buffer = ByteBuffer.allocate(1024);

        private boolean full = false;

        public boolean isFull() {
            return full;
        }

        public ByteBuffer get() {
            return buffer;
        }

        public void seal() {
            buffer.flip();
            full = true;
        }


        public void clear() {
            buffer.clear();
            full = false;
        }


    }


}
