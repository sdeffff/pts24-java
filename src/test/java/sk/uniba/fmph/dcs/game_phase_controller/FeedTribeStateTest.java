package sk.uniba.fmph.dcs.game_phase_controller;

import junit.framework.TestCase;
import org.junit.Test;

import sk.uniba.fmph.dcs.stone_age.*;

import java.util.*;

public class FeedTribeStateTest extends TestCase {
private Map<PlayerOrder, InterfaceFeedTribe> tribes;
    private class MockFeedTribe implements InterfaceFeedTribe {
        private int figures;
        private boolean feedTribeSuccess;
        private boolean doNotFeedSuccess;
        private boolean isTribeFed;

        public MockFeedTribe(int figures, boolean feedTribeSuccess, boolean doNotFeedSuccess, boolean isTribeFed) {
            this.figures = figures;
            this.feedTribeSuccess = feedTribeSuccess;
            this.doNotFeedSuccess = doNotFeedSuccess;
            this.isTribeFed = isTribeFed;
        }

        @Override
        public boolean feedTribeIfEnoughFood() {
            return feedTribeSuccess;
        }

        @Override
        public boolean feedTribe(Collection<Effect> resources) {
            if(resources.size() >= figures) {
                feedTribeSuccess = true;
                isTribeFed = true;
                return feedTribeSuccess;
            }
            return false;
        }

        @Override
        public boolean doNotFeedThisTurn() {
            if(feedTribeSuccess){
                return false;
            }
            return doNotFeedSuccess;
        }

        @Override
        public boolean isTribeFed() {
            return isTribeFed;
        }
    }

    public void setUp(){
        tribes = new HashMap<>();
    }

    public void testFeedTribeSuccess(){

        PlayerOrder player1 = new PlayerOrder(1, 3);
        InterfaceFeedTribe mockTribe1 = new MockFeedTribe(0,true,false,false);
        tribes.put(player1, mockTribe1);

        PlayerOrder player2 = new PlayerOrder(2, 3);
        InterfaceFeedTribe mockTribe2 = new MockFeedTribe(2, false, false,false);
        tribes.put(player2, mockTribe2);

        PlayerOrder player3 = new PlayerOrder(3, 3);
        InterfaceFeedTribe mockTribe3 = new MockFeedTribe(0, true, false,false);
        tribes.put(player3, mockTribe3);

        FeedTribeState feedTribeState = new FeedTribeState(tribes);

        ActionResult result1 = feedTribeState.feedTribe(player1, List.of(Effect.FOOD));
        assertEquals(ActionResult.ACTION_DONE, result1);

        ActionResult result2 = feedTribeState.feedTribe(player2, List.of(Effect.FOOD));
        assertEquals(ActionResult.FAILURE, result2);

        ActionResult result3 = feedTribeState.feedTribe(player3, List.of(Effect.FOOD));
        assertEquals(ActionResult.ACTION_DONE, result3);
    }

    public void testDoNotFeedThisTurn(){

        PlayerOrder player1 = new PlayerOrder(1, 3);
        InterfaceFeedTribe mockTribe1 = new MockFeedTribe(0,true,true,false);
        tribes.put(player1, mockTribe1);

        PlayerOrder player2 = new PlayerOrder(2, 3);
        InterfaceFeedTribe mockTribe2 = new MockFeedTribe(2, false, true,false);
        tribes.put(player2, mockTribe2);

        PlayerOrder player3 = new PlayerOrder(3, 3);
        InterfaceFeedTribe mockTribe3 = new MockFeedTribe(3, true, true,false);
        tribes.put(player3, mockTribe3);

        FeedTribeState feedTribeState = new FeedTribeState(tribes);

        ActionResult result1 = feedTribeState.doNotFeedThisTurn(player1);
        assertEquals(ActionResult.FAILURE, result1);

        feedTribeState.feedTribe(player2, List.of(Effect.FOOD));
        ActionResult result2 = feedTribeState.doNotFeedThisTurn(player2);
        assertEquals(ActionResult.ACTION_DONE, result2);

        feedTribeState.feedTribe(player3, List.of(Effect.FOOD, Effect.CLAY, Effect.STONE));
        ActionResult result3 = feedTribeState.doNotFeedThisTurn(player3);
        assertEquals(ActionResult.FAILURE, result3);
    }

    public void testAutomaticAction(){
        PlayerOrder player1 = new PlayerOrder(1, 3);
        InterfaceFeedTribe mockTribe1 = new MockFeedTribe(0,true,true,false);
        tribes.put(player1, mockTribe1);

        PlayerOrder player2 = new PlayerOrder(2, 3);
        InterfaceFeedTribe mockTribe2 = new MockFeedTribe(2, true, true,true);
        tribes.put(player2, mockTribe2);

        PlayerOrder player3 = new PlayerOrder(3, 3);
        InterfaceFeedTribe mockTribe3 = new MockFeedTribe(3, false, true,false);
        tribes.put(player3, mockTribe3);

        FeedTribeState feedTribeState = new FeedTribeState(tribes);

        HasAction result1 = feedTribeState.tryToMakeAutomaticAction(player1);
        assertEquals(HasAction.AUTOMATIC_ACTION_DONE, result1);

        HasAction result2 = feedTribeState.tryToMakeAutomaticAction(player2);
        assertEquals(HasAction.NO_ACTION_POSSIBLE, result2);

        HasAction result3 = feedTribeState.tryToMakeAutomaticAction(player3);
        assertEquals(HasAction.WAITING_FOR_PLAYER_ACTION, result3);

        feedTribeState.feedTribe(player3, List.of(Effect.FOOD, Effect.FOOD, Effect.FOOD));
        HasAction result4 = feedTribeState.tryToMakeAutomaticAction(player3);
        assertEquals(HasAction.NO_ACTION_POSSIBLE, result4);
    }

}