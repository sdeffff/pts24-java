package sk.uniba.fmph.dcs.stone_age;

import java.util.Collection;

public interface InterfaceStoneAgeGame {
    boolean placeFigures(int playerId, Location location, int figuresCount);

    boolean makeAction(int playerId, Location location, Collection<Effect> inputResources,
                            Collection<Effect> outputResources);

    boolean skipAction(int playerId, Location location);

    boolean useTools(int playerId, int toolIndex);

    boolean noMoreToolsThisThrow(int playerId);

    boolean feedTribe(int playerId, Collection<Effect> resources);

    boolean doNotFeedThisTurn(int playerId);

    boolean makeAllPlayersTakeARewardChoice(int playerId, Effect reward);

}
