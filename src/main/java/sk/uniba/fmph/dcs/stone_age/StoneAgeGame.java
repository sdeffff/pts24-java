package sk.uniba.fmph.dcs.stone_age;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoneAgeGame implements InterfaceStoneAgeGame {

    Map<Integer, PlayerOrder> players;
    InterfaceGamePhaseController gamePhaseController;
    StoneAgeObservable stoneAgeObservable;
    InterfaceGetState getState;

    public StoneAgeGame(InterfaceGamePhaseController gamePhaseController,
                        StoneAgeObservable stoneAgeObservable, InterfaceGetState getState,
                        List<Integer> playerIds) {
        players = new HashMap<>();
        int k = 0;
        for(Integer playerId : playerIds) {
            players.put(playerId, new PlayerOrder(playerId, playerIds.size()));
        }
        this.gamePhaseController = gamePhaseController;
        this.stoneAgeObservable = stoneAgeObservable;
        this.getState = getState;

    }

    @Override
    public boolean placeFigures(int playerId, Location location, int figuresCount) {
       return gamePhaseController.placeFigures(players.get(playerId), location, figuresCount);
    }

    @Override
    public boolean makeAction(int playerId, Location location, Collection<Effect> inputResources, Collection<Effect> outputResources) {
        return gamePhaseController.makeAction(players.get(playerId), location, inputResources, outputResources);
    }

    @Override
    public boolean skipAction(int playerId, Location location) {
        return gamePhaseController.skipAction(players.get(playerId), location);
    }

    @Override
    public boolean useTools(int playerId, int toolIndex) {
        return gamePhaseController.useTools(players.get(playerId), toolIndex);
    }

    @Override
    public boolean noMoreToolsThisThrow(int playerId) {
        return gamePhaseController.noMoreToolsThisThrow(players.get(playerId));
    }

    @Override
    public boolean feedTribe(int playerId, Collection<Effect> resources) {
        return gamePhaseController.feedTribe(players.get(playerId), resources);
    }

    @Override
    public boolean doNotFeedThisTurn(int playerId) {
        return gamePhaseController.doNotFeedThisTurn(players.get(playerId));
    }

    @Override
    public boolean makeAllPlayersTakeARewardChoice(int playerId, Effect reward) {
        return gamePhaseController.makeAllPlayersTakeARewardChoice(players.get(playerId), reward);
    }
}


