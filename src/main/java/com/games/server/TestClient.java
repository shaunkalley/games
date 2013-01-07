package com.games.server;

import java.util.Arrays;

import com.meterware.httpunit.HttpUnitOptions;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;

/**
 * Created with IntelliJ IDEA.
 * User: shaun
 * Date: 2012-12-29
 * Time: 9:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestClient {

    public static void main(String[] args) throws Exception {

        HttpUnitOptions.setScriptingEnabled(false);
        HttpUnitOptions.setExceptionsThrownOnScriptError(false);
        HttpUnitOptions.setExceptionsThrownOnErrorStatus(false);

        WebConversation client = new WebConversation();
        doAnonymousLogin(client, "Player 1");
        System.out.println();
        doJoinGame(client);
    }

    public static void doAnonymousLogin(WebConversation client, String nickname) throws Exception {
        System.out.println("Logging in anonymously...");
        PostMethodWebRequest request = new PostMethodWebRequest("http://127.0.0.1:8080/games/anonLogin");
        request.setParameter("nickname", nickname);
        WebResponse response = client.getResponse(request);
        for (String name : response.getHeaderFieldNames()) {
            System.out.println("header: name=" + name + ", value=" + Arrays.toString(response.getHeaderFields(name)));
        }
        System.out.println("response code=" + response.getResponseCode());
        for (String name : response.getNewCookieNames()) {
            System.out.println("new cookie: name=" + name + ", value=" + response.getNewCookieValue(name));
        }
        System.out.println("text=" + response.getText());
    }

    public static void doJoinGame(WebConversation client) throws Exception {
        System.out.println("Joining game...");
        WebResponse response = client.getResponse("http://127.0.0.1:8080/games/euchre?action=joinGame");
        for (String name : response.getHeaderFieldNames()) {
            System.out.println("header: name=" + name + ", value=" + Arrays.toString(response.getHeaderFields(name)));
        }
        System.out.println("response code=" + response.getResponseCode());
        for (String name : response.getNewCookieNames()) {
            System.out.println("new cookie: name=" + name + ", value=" + response.getNewCookieValue(name));
        }
        System.out.println("text=" + response.getText());
    }
}
