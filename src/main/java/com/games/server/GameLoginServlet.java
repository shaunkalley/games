package com.games.server;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.*;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
@WebServlet(name = "GameLoginServlet", urlPatterns = { "/games/login", "/games/fbLogin", "/games/anonLogin", "/games/register" })
public class GameLoginServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(GameLoginServlet.class.getName());

    private static final String requestUriPrefix = "/games/";

    /**
     * Handle synchronous, form-based requests (login, Facebook login, anonymous login, register).
     *
     * @param request the request
     * @param response the response
     * @throws ServletException if the request cannot be handled
     * @throws IOException if an I/O error occurs while handling the request
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Login request received: uri=" + request.getRequestURI() + ", session id=" + request.getSession().getId() + ", requested session id=" + request.getRequestedSessionId())    ;
        String action = request.getRequestURI().substring(requestUriPrefix.length()); // trim the "/games/" prefix from the request uri
        switch (action) {
            case "login":
                handleLogin(request, response);
                break;
            case "fbLogin":
                handleFacebookLogin(request, response);
                break;
            case "anonLogin":
                handleAnonymousLogin(request, response);
                break;
            case "register":
                handleRegistration(request, response);
                break;
            default:
                response.sendError(SC_NOT_FOUND);
                break;
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO
        response.sendError(SC_NOT_IMPLEMENTED);
    }

    private void handleFacebookLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO
        response.sendError(SC_NOT_IMPLEMENTED);
    }

    private void handleAnonymousLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nickname = request.getParameter("nickname");
        if (nickname == null) {
            response.sendError(SC_BAD_REQUEST, "nickname required");
            return;
        }
        // TODO: do checks on the nickname for uniqueness, etc.
        AnonymousPlayer player = new AnonymousPlayer(nickname);
        String loginId = GlobalGameCoordinator.playerLoggedIn(request.getSession().getId(), player);
        response.addCookie(new Cookie("loginId", loginId));
        response.setStatus(SC_OK);
    }

    private void handleRegistration(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO
        response.sendError(SC_NOT_IMPLEMENTED);
    }
}
