package com.games.nioserver;

import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2012-12-29
 * Time: 9:56 AM
 * To change this template use File | Settings | File Templates.
 */
public final class GameServer {

    private final InetAddress address;
    private final int port;

    private MessageHandler handler;
    private MessageListener listener;

    private GameServer() {
        // load and read properties
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("com/games/nioserver/gameserver.properties")) {
            Properties props = new Properties();
            props.load(in);
            address = InetAddress.getByName(props.getProperty("listener.address"));
            port = Integer.parseInt(props.getProperty("listener.port"));
            System.out.println("address=" + address + ", port=" + port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void start() throws Exception {
        handler = new MessageHandler(this);
        listener = new MessageListener(address, port, handler);
        handler.setListener(listener);
        // start the handler before the listener
        handler.start();
        System.out.println("handler started");
        listener.start();
        System.out.println("listener started");
    }

    public synchronized void stop() {
        // stop the listener before the handler
        listener.stop();
        handler.stop();
    }

    public static void main(String[] args) throws Exception {
        GameServer server = new GameServer();
        server.start();
    }
}
