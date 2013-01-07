package com.games.nioserver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-02
 * Time: 7:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class MessageHandler {

    private final GameServer server;

    private MessageListener listener;

    private ExecutorService exec;

    public MessageHandler(GameServer server) {
        this.server = server;
    }

    public void setListener(MessageListener listener) {
        this.listener = listener;
    }

    /**
     * Starts the message handler.
     *
     * @throws IllegalStateException if the message handler is already running
     */
    public synchronized void start() {
        if (exec != null) {
            throw new IllegalStateException("already running");
        }
        if (listener == null) {
            throw new AssertionError("no listener registered");
        }
        exec = Executors.newCachedThreadPool();
    }

    /**
     * Stops the message handler.  Does nothing if the message handler is not
     * running.
     */
    public synchronized void stop() {
        if (exec != null) {
            exec.shutdown();
            try {
                exec.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            exec = null;
        }
    }

    public void handleIncomingMessage(String message) throws MessageFormatException {
        int pipeIndex = message.indexOf('|');
        if (pipeIndex == -1) {
            throw new MessageFormatException("pipe separator not found: " + message);
        }
        String sessionId = message.substring(0, pipeIndex);
        message = message.substring(pipeIndex + 1);
        System.out.println("incoming message received: " + message + " (sessionId=" + sessionId + ")");
        exec.execute(new MessageTask(sessionId, message));
    }

    private void acknowledgeMessage(String sessionId, String message) {
        System.out.println("acknowledging message: " + message + " (sessionId=" + sessionId + ")");
        listener.addOutgoingMessage(sessionId, "ACK|" + message);
    }

    private class MessageTask implements Runnable {

        private final String sessionId;
        private final String message;

        MessageTask(String sessionId, String message) {
            this.sessionId = sessionId;
            this.message = message;
        }

        public void run() {
            acknowledgeMessage(sessionId, message);
        }
    }
}
