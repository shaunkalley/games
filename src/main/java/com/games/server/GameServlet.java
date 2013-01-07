package com.games.server;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-03
 * Time: 9:22 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class GameServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(GameServlet.class.getName());

    private static final long timeout = 600000L;

    private GameCoordinator gameCoordinator;

    private ExecutorService exec;

    public abstract GameProvider getGameProvider();

    @Override
    public void init() {
        gameCoordinator = new GameCoordinator(getGameProvider());
        logger.info("Starting thread pool");
        exec = Executors.newCachedThreadPool();
    }

    @Override
    public void destroy() {
        logger.info("Stopping thread pool");
        exec.shutdown();
    }

    /**
     * Handle asynchronous game requests.  All responses return JSON data.
     *
     * @param request the request
     * @param response the response
     * @throws ServletException if the request cannot be handled
     * @throws IOException if an I/O error occurs while handling the request
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Game request received: uri=" + request.getRequestURI() + ", session id=" + request.getSession().getId() + ", requested session id=" + request.getRequestedSessionId() + ", query string=" + request.getQueryString());
        AsyncContext asyncContext = request.startAsync();
        request.getAsyncContext().setTimeout(timeout);
        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                HttpServletRequest request = (HttpServletRequest) event.getSuppliedRequest();
                System.out.println("XXXXX AsyncListener.onTimeout: session id = " + request.getSession().getId() + ", timeout = " + event.getAsyncContext().getTimeout());
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                HttpServletRequest request = (HttpServletRequest) event.getSuppliedRequest();
                System.out.println("XXXXX AsyncListener.onError: session id = " + request.getSession().getId() + ", error = " + ExceptionUtils.getStackTrace(event.getThrowable()));
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
            }
        });
        exec.execute(new AsyncHandler(asyncContext));
    }

    private class AsyncHandler implements Runnable {

        private final AsyncContext asyncContext;

        AsyncHandler(AsyncContext asyncContext) {
            this.asyncContext = asyncContext;
        }

        public void run() {
            HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
            HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
            try {
                ClientMessage clientMessage = getClientMessage(request);
                // TODO: check to make sure user is not already playing another game
                ServerMessage serverMessage = gameCoordinator.handleMessage(clientMessage);
                String jsonResponse = serverMessage.toJson();
                response.setContentType("application/json");
                response.getWriter().write(jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
                logger.log(Level.WARNING, "Error handling request.", e);
                try {
                    response.sendError(SC_INTERNAL_SERVER_ERROR, e.getMessage());
                } catch (IOException ex) {
                    logger.log(Level.WARNING, "Error sending error status.", ex);
                }
            } finally {
                asyncContext.complete();
            }
        }

        private ClientMessage getClientMessage(HttpServletRequest request) {
            Map<String, Object> attributes = new HashMap<>();
            for (Enumeration<String> parameterNames = request.getParameterNames(); parameterNames.hasMoreElements();) {
                String parameterName = parameterNames.nextElement();
                attributes.put(parameterName, request.getParameter(parameterName));
            }
            return new ClientMessage(request.getSession().getId(), attributes);
        }
    }
}
