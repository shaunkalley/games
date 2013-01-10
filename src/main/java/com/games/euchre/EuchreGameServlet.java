package com.games.euchre;

import javax.servlet.annotation.WebServlet;

import com.games.server.GameServlet;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 3:25 PM
 * To change this template use File | Settings | File Templates.
 */
@WebServlet(name = "EuchreGameServlet", urlPatterns = { "/games/euchre/*" }, asyncSupported = true)
public class EuchreGameServlet extends GameServlet {

    private final EuchreGameProvider gameProvider = new EuchreGameProvider();

    public EuchreGameProvider getGameProvider() {
        return gameProvider;
    }
}
