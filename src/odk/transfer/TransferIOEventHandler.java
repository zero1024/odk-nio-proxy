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

    private BufferWrapper localToRemoteBuffer = new BufferWrapper();
    private BufferWrapper remoteToLocalBuffer = new BufferWrapper();

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
        if (localToRemoteBuffer.isFull())
            remote.interestOps(SelectionKey.OP_WRITE);
    }


    private void readFromLocal(SelectionKey key) throws IOException {
        int read = localChannel.read(localToRemoteBuffer.get());

        if (read > 0) {
            localToRemoteBuffer.seal();
            local.interestOps(0);
            if (remoteServerIsConnected)
                remote.interestOps(SelectionKey.OP_WRITE);
        } else if (read == -1) {
            remoteChannel.close();
            localChannel.close();
        }
    }

    private void readFromRemote(SelectionKey key) throws IOException {
        int read = remoteChannel.read(remoteToLocalBuffer.get());

        if (read > 0) {
            remoteToLocalBuffer.seal();
            remote.interestOps(0);
            local.interestOps(SelectionKey.OP_WRITE);
        } else if (read == -1) {
            remoteChannel.close();
            localChannel.close();
        }
    }


    private void writeToLocal(SelectionKey key) throws IOException {
        int read = localChannel.write(remoteToLocalBuffer.get());

        if (remoteToLocalBuffer.get().remaining() == 0) {
            remoteToLocalBuffer.clear();
            local.interestOps(SelectionKey.OP_READ);
            remote.interestOps(SelectionKey.OP_READ);
        }
    }

    private void writeToRemote(SelectionKey key) throws IOException {
        remoteChannel.write(localToRemoteBuffer.get());

        if (localToRemoteBuffer.get().remaining() == 0) {
            localToRemoteBuffer.clear();
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
