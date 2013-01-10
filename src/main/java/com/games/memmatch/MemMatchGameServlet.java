package com.games.memmatch;

import javax.servlet.annotation.WebServlet;

import com.games.server.GameServlet;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-10
 * Time: 12:55 AM
 * To change this template use File | Settings | File Templates.
 */
@WebServlet(name = "MemMatchGameServlet", urlPatterns = { "/games/memmatch/*" }, asyncSupported = true)
public class MemMatchGameServlet extends GameServlet {

    private final MemMatchGameProvider gameProvider = new MemMatchGameProvider();

    public MemMatchGameProvider getGameProvider() {
        return gameProvider;
    }
}
