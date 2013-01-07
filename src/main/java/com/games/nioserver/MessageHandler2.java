package com.games.nioserver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-03
 * Time: 9:14 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MessageHandler2 implements Service {

    private ExecutorService exec;

    public MessageHandler2() {
    }

    public synchronized void start() {
        if (exec != null) {
            throw new IllegalStateException("already started");
        }
        exec = Executors.newCachedThreadPool();
    }

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

    public abstract void handleIncomingMessage();

    private class MessageTask implements Runnable {

        private final String sessionId;
        private final String message;

        MessageTask(String sessionId, String message) {
            this.sessionId = sessionId;
            this.message = message;
        }

        public void run() {

            //acknowledgeMessage(sessionId, message);
        }
    }
}
