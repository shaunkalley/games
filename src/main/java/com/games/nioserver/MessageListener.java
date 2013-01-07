package com.games.nioserver;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-02
 * Time: 10:40 AM
 * To change this template use File | Settings | File Templates.
 */
public final class MessageListener {

    private final InetAddress address;
    private final int port;

    private final MessageHandler msgHandler;

    private Selector acceptReadSelector;
    private Selector writeSelector;
    private ConcurrentMap<String, SelectableChannel> sessionChannelMap;
    private ConcurrentMap<SelectableChannel, MessageBuffer> channelBufferMap;
    private ConcurrentLinkedQueue<SelectableChannel> writeChannels;
    private Thread acceptReadThread;
    private Thread writeThread;

    public MessageListener(InetAddress address, int port, MessageHandler msgHandler) {
        this.address = address;
        this.port = port;
        this.msgHandler = msgHandler;
    }

    public synchronized void start() throws Exception {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(address, port));
        acceptReadSelector = Selector.open();
        writeSelector = Selector.open();
        sessionChannelMap = new ConcurrentHashMap<>();
        channelBufferMap = new ConcurrentHashMap<>();
        writeChannels = new ConcurrentLinkedQueue<>();
        writeThread = new Thread(new WriteTask());
        writeThread.start();
        serverChannel.register(acceptReadSelector, SelectionKey.OP_ACCEPT);
        acceptReadThread = new Thread(new AcceptReadTask());
        acceptReadThread.start();
    }

    public synchronized void stop() {
        acceptReadThread.interrupt();
        writeThread.interrupt();
    }

    public void addOutgoingMessage(String sessionId, String message) {
        SelectableChannel channel = sessionChannelMap.get(sessionId);
        addOutgoingMessage(channel, message);
    }

    private void addOutgoingMessage(SelectableChannel channel, String message) {
        MessageBuffer buffer = channelBufferMap.get(channel);
        buffer.addOutgoingMessage(message);
        writeChannels.add(channel);
        writeSelector.wakeup();
    }

    private void closeChannel(Channel channel) {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class AcceptReadTask implements Runnable {

        public void run() {

            while (!Thread.currentThread().isInterrupted()) {

                try {

                    int n = acceptReadSelector.select();
                    if (n == 0) {
                        continue;
                    }

                    for (Iterator<SelectionKey> iter = acceptReadSelector.selectedKeys().iterator(); iter.hasNext();) {

                        SelectionKey key = iter.next();
                        iter.remove();

                        if (key.isValid() && key.isAcceptable()) {
                            ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                            SocketChannel channel = serverChannel.accept();
                            if (channel != null) {
                                try {
                                    channel.configureBlocking(false);
                                    channel.register(acceptReadSelector, SelectionKey.OP_READ);
                                    String sessionId = RandomStringUtils.randomAlphanumeric(24);
                                    sessionChannelMap.put(sessionId, channel);
                                    channelBufferMap.put(channel, new MessageBuffer());
                                    addOutgoingMessage(channel, sessionId);
                                } catch (IOException ioe) {
                                    closeChannel(key.channel());
                                }
                            }
                        }

                        if (key.isValid() && key.isReadable()) {
                            // read incoming messages
                            SocketChannel channel = (SocketChannel) key.channel();
                            MessageBuffer buffer = channelBufferMap.get(channel);
                            if (buffer != null) {
                                try {
                                    if (!buffer.read(channel)) {
                                        closeChannel(channel);
                                        continue;
                                    }
                                    for (String message : buffer.getIncomingMessages()) {
                                        try {
                                            msgHandler.handleIncomingMessage(message);
                                        } catch (MessageFormatException e) {
                                            addOutgoingMessage(channel, "REJECTED|" + message);
                                        }
                                    }
                                } catch (IOException e) {
                                    closeChannel(channel);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class WriteTask implements Runnable {

        public void run() {

            while (!Thread.currentThread().isInterrupted()) {

                try {

                    for (SelectableChannel channel; (channel = writeChannels.poll()) != null;) {
                        try {
                            channel.register(writeSelector, SelectionKey.OP_WRITE);
                        } catch (ClosedChannelException e) {
                            closeChannel(channel);
                        }
                    }

                    int n = writeSelector.select();
                    if (n == 0) {
                        continue;
                    }

                    for (Iterator<SelectionKey> iter = writeSelector.selectedKeys().iterator(); iter.hasNext();) {

                        SelectionKey key = iter.next();
                        iter.remove();

                        if (key.isValid() && key.isWritable()) {
                            // write outgoing messages
                            SocketChannel channel = (SocketChannel) key.channel();
                            MessageBuffer buffer = channelBufferMap.get(channel);
                            if (buffer != null) {
                                try {
                                    buffer.write(channel);
                                } catch (IOException e) {
                                    closeChannel(channel);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
