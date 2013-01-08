//package com.games.euchre;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import com.games.server.AnonymousPlayer;
//import com.games.server.Game;
//import com.games.server.Player;
//import junit.framework.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import static com.games.euchre.Rank.*;
//import static com.games.euchre.Suit.*;
//
///**
//* Created with IntelliJ IDEA.
//* User: shaun
//* Date: 2013-01-01
//* Time: 3:02 PM
//* To change this template use File | Settings | File Templates.
//*/
//public class TrickTest {
//
//    private List<Player> players;
//    private Hand hand;
//
//    @Before
//    public void setup() {
//        EuchreGameOptions options = new EuchreGameOptions();
//        players = new ArrayList<>();
//        players.add(new AnonymousPlayer("A"));
//        players.add(new AnonymousPlayer("B"));
//        players.add(new AnonymousPlayer("C"));
//        players.add(new AnonymousPlayer("D"));
//        );
//        Game game = new EuchreGame(null, null);
//        hand = new Hand(game, 0, players.get(0));
//    }
//
//    @Test
//    public void testWinner() {
//        testWinner(SPADES, new Card(SPADES, JACK), new Card(CLUBS, JACK), new Card(SPADES, ACE), new Card(SPADES, KING), players.get(0));
//        testWinner(SPADES, new Card(CLUBS, JACK), new Card(SPADES, ACE), new Card(SPADES, KING), new Card(SPADES, QUEEN), players.get(0));
//        testWinner(SPADES, new Card(SPADES, NINE), new Card(HEARTS, ACE), new Card(DIAMONDS, ACE), new Card(CLUBS, ACE), players.get(0));
//        testWinner(SPADES, new Card(SPADES, NINE), new Card(HEARTS, ACE), new Card(HEARTS, KING), new Card(HEARTS, QUEEN), players.get(0));
//        testWinner(SPADES, new Card(HEARTS, ACE), new Card(HEARTS, KING), new Card(HEARTS, QUEEN), new Card(HEARTS, JACK), players.get(0));
//    }
//
//    public void testWinner(Suit trump, Card player1Card, Card player2Card, Card player3Card, Card player4Card, Player expectedWinner) {
//        hand.setTrump(trump);
//        hand.setCards(players.get(0), new ArrayList<>(Arrays.asList(player1Card, new Card(HEARTS, TEN), new Card(HEARTS, NINE), new Card(DIAMONDS, TEN), new Card(DIAMONDS, NINE))));
//        hand.setCards(players.get(1), new ArrayList<>(Arrays.asList(player2Card, new Card(CLUBS, ACE), new Card(HEARTS, KING), new Card(HEARTS, QUEEN), new Card(HEARTS, TEN))));
//        hand.setCards(players.get(2), new ArrayList<>(Arrays.asList(player3Card, new Card(DIAMONDS, ACE), new Card(DIAMONDS, KING), new Card(DIAMONDS, QUEEN), new Card(DIAMONDS, JACK))));
//        hand.setCards(players.get(3), new ArrayList<>(Arrays.asList(player4Card, new Card(HEARTS, ACE), new Card(HEARTS, KING), new Card(HEARTS, QUEEN), new Card(HEARTS, JACK))));
//        Trick trick = new Trick(hand, players.toArray(new Player[players.size()]));
//        trick.playCard(players.get(0), player1Card);
//        trick.playCard(players.get(1), player2Card);
//        trick.playCard(players.get(2), player3Card);
//        trick.playCard(players.get(3), player4Card);
//        trick.determineWinner();
//        Assert.assertEquals(expectedWinner, trick.getWinner());
//    }
//}
