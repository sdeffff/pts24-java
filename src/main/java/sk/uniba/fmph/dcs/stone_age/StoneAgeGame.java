
package sk.uniba.fmph.dcs.stone_age;

import org.json.JSONObject;
import sk.uniba.fmph.dcs.game_board.*;
import sk.uniba.fmph.dcs.game_phase_controller.GamePhaseControllerFactory;
import sk.uniba.fmph.dcs.player_board.PlayerBoard;
import sk.uniba.fmph.dcs.player_board.PlayerBoardFacade;
import sk.uniba.fmph.dcs.player_board.PlayerBoardFactory;

import java.util.*;

public final class StoneAgeGame implements InterfaceStoneAgeGame {
    private final Map<Integer, PlayerOrder> idToPlayerOrder;
    private final InterfaceGetState gameBoard;
    private final ArrayList<PlayerBoard> playerBoards;
    private final InterfaceGamePhaseController gamePhaseController;
    private final StoneAgeObservable observable;
    public Map<PlayerOrder, PlayerBoardFacade> playerBoardsMap;
    private static final int MAX_PLAYERS = 4;
    private static final int MIN_PLAYERS = 2;
    private static final int NUMBER_OF_RESOURCES_ARB_BUILDING_7 = 7;
    private static final int NUMBER_OF_RESOURCES_VAR_BUILDING_4 = 4;
    private static final int NUMBER_OF_RESOURCE_TYPES_2 = 2;
    private static final int NUMBER_OF_RESOURCE_TYPES_4 = 4;

    public StoneAgeGame(final ArrayList<Integer> players, final ArrayList<InterfaceStoneAgeObserver> observers) {
        this(players, observers, new CurrentThrow(new Throw()), new Throw(), initializeDefaultCards(), initializeBuildings());
    }
    public StoneAgeGame(final ArrayList<Integer> players, final ArrayList<InterfaceStoneAgeObserver> observers, final CurrentThrow currentThrow,
                        final ThrowInterface throw1, final ArrayList<CivilizationCard> cards, final ArrayList<Building> buildings) {
        if (players.size() > MAX_PLAYERS || players.size() < MIN_PLAYERS || players.size() != observers.size()) {
            throw new IllegalArgumentException("2-4 players may play the game");
        }

        observable = initializeObservable(players, observers);
        idToPlayerOrder = initializeMap(players);
        playerBoards = new ArrayList<>();
        playerBoardsMap = new HashMap<>();
        ArrayList<sk.uniba.fmph.dcs.game_board.Player> playersPlayer = new ArrayList<>();

        for (int player : players) {
            PlayerBoard pB = PlayerBoardFactory.createPlayerBoard();
            playerBoards.add(pB);
            PlayerBoardFacade pBF = new PlayerBoardFacade(pB);
            playerBoardsMap.put(idToPlayerOrder.get(player), pBF);
            playersPlayer.add(new Player(idToPlayerOrder.get(player), pBF));
        }

        GameBoard gb = GameBoardFactory.createGameBoard(playersPlayer, throw1, currentThrow, buildings);
        gamePhaseController = GamePhaseControllerFactory.createGamePhaseController(gb.getLocationToFigureLocationMap(),
                new HashMap<>(playerBoardsMap), new HashMap<>(playerBoardsMap), idToPlayerOrder.get(players.getFirst()),
                gb.getCurrentThrow(), gb.getRewardMenu());
        gameBoard = gb;
    }

    @Override
    public boolean placeFigures(final int playerId, final Location location, final int figuresCount) {
        if (!idToPlayerOrder.containsKey(playerId)) {
            return false;
        }
        boolean result = gamePhaseController.placeFigures(idToPlayerOrder.get(playerId), location, figuresCount);
        notifyObservable();
        return result;
    }

    @Override
    public boolean makeAction(final int playerId, final Location location, final Collection<Effect> usedResources,
                              final Collection<Effect> desiredResources) {
        if (!idToPlayerOrder.containsKey(playerId)) {
            return false;
        }
        boolean result = gamePhaseController.makeAction(idToPlayerOrder.get(playerId), location, usedResources,
                desiredResources);
        notifyObservable();
        return result;
    }

    @Override
    public boolean skipAction(final int playerId, final Location location) {
        if (!idToPlayerOrder.containsKey(playerId)) {
            return false;
        }
        boolean result = gamePhaseController.skipAction(idToPlayerOrder.get(playerId), location);
        notifyObservable();
        return result;
    }

    @Override
    public boolean useTools(final int playerId, final int toolIndex) {
        if (!idToPlayerOrder.containsKey(playerId)) {
            return false;
        }
        boolean result = gamePhaseController.useTools(idToPlayerOrder.get(playerId), toolIndex);
        notifyObservable();
        return result;
    }

    @Override
    public boolean noMoreToolsThisThrow(final int playerId) {
        if (!idToPlayerOrder.containsKey(playerId)) {
            return false;
        }
        boolean result = gamePhaseController.noMoreToolsThisThrow(idToPlayerOrder.get(playerId));
        notifyObservable();
        return result;
    }

    @Override
    public boolean feedTribe(final int playerId, final Collection<Effect> resources) {
        if (!idToPlayerOrder.containsKey(playerId)) {
            return false;
        }
        boolean result = gamePhaseController.feedTribe(idToPlayerOrder.get(playerId), resources);
        notifyObservable();
        return result;
    }

    @Override
    public boolean doNotFeedThisTurn(final int playerId) {
        if (!idToPlayerOrder.containsKey(playerId)) {
            return false;
        }
        boolean result = gamePhaseController.doNotFeedThisTurn(idToPlayerOrder.get(playerId));
        notifyObservable();
        return result;
    }

    @Override
    public boolean makeAllPlayersTakeARewardChoice(final int playerId, final Effect reward) {
        if (!idToPlayerOrder.containsKey(playerId)) {
            return false;
        }
        boolean result = gamePhaseController.makeAllPlayersTakeARewardChoice(idToPlayerOrder.get(playerId), reward);
        notifyObservable();
        return result;
    }

    private void notifyObservable() {
        Map<String, String> state = new HashMap<>();
        state.put("GameBoard", gameBoard.state());
        state.put("GamePhaseController", gamePhaseController.state());
        int i = 1;
        for (InterfaceGetState pB : playerBoards) {
            state.put("Player" + i, pB.state());
            i++;
        }
        observable.notify(new JSONObject(state).toString());
    }

    private static Map<Integer, PlayerOrder> initializeMap(final ArrayList<Integer> players) {
        Map<Integer, PlayerOrder> map = new HashMap<>();
        for (int i = 0; i < players.size(); i++) {
            map.put(players.get(i), new PlayerOrder(i, players.size()));
        }
        return map;
    }

    private static StoneAgeObservable initializeObservable(final ArrayList<Integer> players,
                                                           final ArrayList<InterfaceStoneAgeObserver> observers) {
        StoneAgeObservable observable = new StoneAgeObservable();
        for (int i = 0; i < players.size(); i++) {
            observable.registerObserver(players.get(i), observers.get(i));
        }
        return observable;
    }

    private static ArrayList<CivilizationCard> initializeDefaultCards() {
        ArrayList<CivilizationCard> cards = new ArrayList<>();
        cards.add(new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                List.of(EndOfGameEffect.FARMER)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                List.of(EndOfGameEffect.POTTERY)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                List.of(EndOfGameEffect.BUILDER)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                List.of(EndOfGameEffect.BUILDER)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                List.of(EndOfGameEffect.WRITING)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                List.of(EndOfGameEffect.FARMER)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                List.of(EndOfGameEffect.POTTERY)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.AllPlayersTakeReward),
                List.of(EndOfGameEffect.TOOL_MAKER)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.STONE, ImmediateEffect.STONE),
                List.of(EndOfGameEffect.TRANSPORT)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.CARD), List.of(EndOfGameEffect.WRITING)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.ArbitraryResource, ImmediateEffect.ArbitraryResource),
                List.of(EndOfGameEffect.MEDICINE)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.ThrowWood),
                List.of(EndOfGameEffect.SHAMAN, EndOfGameEffect.SHAMAN)));
        cards.add(new CivilizationCard(List.of(ImmediateEffect.TOOL), List.of(EndOfGameEffect.BUILDER)));
        Collections.shuffle(cards);
        return cards;
    }

    private static ArrayList<Building> initializeBuildings() {
        ArrayList<Building> buildings = new ArrayList<>();
        buildings.add(new ArbitraryBuilding(NUMBER_OF_RESOURCES_ARB_BUILDING_7));
        buildings.add(new VariableBuilding(NUMBER_OF_RESOURCES_VAR_BUILDING_4, NUMBER_OF_RESOURCE_TYPES_4));
        buildings.add(new SimpleBuilding(List.of(Effect.WOOD, Effect.WOOD, Effect.GOLD)));
        buildings.add(new SimpleBuilding(List.of(Effect.CLAY, Effect.STONE, Effect.GOLD)));
        buildings.add(new VariableBuilding(NUMBER_OF_RESOURCES_VAR_BUILDING_4, NUMBER_OF_RESOURCE_TYPES_2));
        return buildings;
    }

    public ArrayList<PlayerBoard> getPlayerBoards() {
        return playerBoards;
    }
}