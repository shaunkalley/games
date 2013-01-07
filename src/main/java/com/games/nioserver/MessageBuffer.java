package com.games.nioserver;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2012-12-29
 * Time: 9:56 AM
 * To change this template use File | Settings | File Templates.
 *
 * A string-based incoming/outgoing message buffer.
 */
public class MessageBuffer {

    private static final int BUFFER_SIZE = 8192;

    private ByteBuffer incomingMessageBuffer;
    private BlockingQueue<String> outgoingMessages;

    public MessageBuffer() {
        incomingMessageBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        outgoingMessages = new LinkedBlockingQueue<>();
    }

    public boolean read(SocketChannel channel) throws IOException {
        assert channel != null;
        return channel.read(incomingMessageBuffer) != -1;
    }

    public List<String> getIncomingMessages() {
        incomingMessageBuffer.flip();
        List<String> incomingMessages = Arrays.asList(
            new String(incomingMessageBuffer.array(), 0, incomingMessageBuffer.limit()).split("[\\r\\n]+"));
        incomingMessageBuffer.clear();
        return incomingMessages;
    }

    public void addOutgoingMessage(String message) {
        outgoingMessages.add(message);
    }

    public void write(SocketChannel channel) throws IOException {
        assert channel != null;
        List<String> temp = new ArrayList<>();
        if (this.outgoingMessages.drainTo(temp) > 0) {
            StringBuilder sb = new StringBuilder();
            for (String message : temp) {
                sb.append(message).append('\n');
            }
            channel.write(ByteBuffer.wrap(sb.toString().getBytes()));
        }
    }
}
