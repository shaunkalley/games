package com.games.server;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2013-01-04
 * Time: 8:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameFormationTest {

    static ExecutorService loggerExec;
    static ExecutorService exec;
    static Random random;

    public static void main(String[] args) throws Exception {

        loggerExec = Executors.newSingleThreadExecutor();
        exec = Executors.newCachedThreadPool();
        random = new Random();

        for (int i = 1; i <= 4; i++) {
            final int iFinal = i;
            exec.execute(new Runnable() {
                public void run() {
                    try {
                        String nickname = "Player " + iFinal;
                        WebConversation client = new WebConversation();
                        doAnonymousLogin(client, nickname);
                        doJoinGame(client, nickname);
                        doGetNextMessage(client, nickname);
                    } catch (Exception e) {
                        log(ExceptionUtils.getStackTrace(e));
                    }
                }
            });
        }

        exec.shutdown();
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        loggerExec.shutdown();
        loggerExec.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    }

    public static void doAnonymousLogin(WebConversation client, String nickname) throws Exception {
        Thread.sleep(random.nextInt(10000));
        StringBuilder sb = new StringBuilder();
        sb.append("Logging " + nickname + " in anonymously...");
        PostMethodWebRequest request = new PostMethodWebRequest("http://127.0.0.1:8080/games/anonLogin");
        request.setParameter("nickname", nickname);
        WebResponse response = client.getResponse(request);
        sb.append("  response code = " + response.getResponseCode());
        sb.append("  session id = " + response.getNewCookieValue("JSESSIONID"));
        sb.append("  response = " + response.getText());
        log(sb.toString());
    }

    public static void doJoinGame(WebConversation client, String nickname) throws Exception {
        Thread.sleep(random.nextInt(10000));
        StringBuilder sb = new StringBuilder();
        sb.append("Joining game (" + nickname + ")...");
        WebResponse response = client.getResponse("http://127.0.0.1:8080/games/euchre?action=joinGame");
        sb.append("  response code = " + response.getResponseCode());
        sb.append("  response = " + response.getText());
        log(sb.toString());
    }

    public static void doGetNextMessage(WebConversation client, String nickname) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("Getting next message (" + nickname + ")...");
        WebResponse response = client.getResponse("http://127.0.0.1:8080/games/euchre?action=getNextClientMessage");
        sb.append("  response code = " + response.getResponseCode());
        sb.append("  response = " + response.getText());
        log(sb.toString());
    }

    private static void log(final String s) {
        loggerExec.execute(new Runnable() {
            public void run() {
                System.out.println(s);
            }
        });
    }
}
