package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;
import sk.uniba.fmph.dcs.stone_age.ImmediateEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CivilizationCardPlaceTest {
    private CivilizationCardDeck deck;
    private CivilizationCardDeck getDeck(){
        List<CivilizationCard> allCards = new ArrayList<>();
        // Dice roll (10 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.WRITING)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.SUNDIAL)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.POTTERY)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.TRANSPORT)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.FARMER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.FARMER, EndOfGameEffect.FARMER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.BUILDER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.TOOL_MAKER, EndOfGameEffect.TOOL_MAKER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.AllPlayersTakeReward), Arrays.asList(EndOfGameEffect.TOOL_MAKER, EndOfGameEffect.TOOL_MAKER)));

        // Food (7 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.MEDICINE)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.POTTERY)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.WEAVING)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.WEAVING)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.FARMER, EndOfGameEffect.FARMER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD, ImmediateEffect.FOOD), Arrays.asList(EndOfGameEffect.BUILDER)));

        // Resource (5 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.STONE, ImmediateEffect.STONE), Arrays.asList(EndOfGameEffect.TRANSPORT)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.STONE), Arrays.asList(EndOfGameEffect.FARMER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.STONE), Arrays.asList(EndOfGameEffect.SHAMAN)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.CLAY), Arrays.asList(EndOfGameEffect.SHAMAN, EndOfGameEffect.SHAMAN)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.GOLD), Arrays.asList(EndOfGameEffect.SHAMAN)));

        // Resources with dice roll (3 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.ThrowStone), Arrays.asList(EndOfGameEffect.SHAMAN)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.ThrowGold), Arrays.asList(EndOfGameEffect.ART)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.WOOD), Arrays.asList(EndOfGameEffect.SHAMAN, EndOfGameEffect.SHAMAN)));

        // Victory points (3 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.POINT, ImmediateEffect.POINT, ImmediateEffect.POINT), Arrays.asList(EndOfGameEffect.MUSIC)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.POINT, ImmediateEffect.POINT, ImmediateEffect.POINT), Arrays.asList(EndOfGameEffect.MUSIC)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.POINT, ImmediateEffect.POINT, ImmediateEffect.POINT), Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)));

        // Extra tool tile (1 card)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.Tool), Arrays.asList(EndOfGameEffect.ART)));

        // Agriculture (2 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.Field), Arrays.asList(EndOfGameEffect.SUNDIAL)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.Field), Arrays.asList(EndOfGameEffect.FARMER)));

        // Civilization card for final scoring (1 card)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.CARD), Arrays.asList(EndOfGameEffect.WRITING)));

        // One-use tool (3 cards)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.OneTimeTool2), Arrays.asList(EndOfGameEffect.BUILDER, EndOfGameEffect.BUILDER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.OneTimeTool3), Arrays.asList(EndOfGameEffect.BUILDER)));
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.OneTimeTool4), Arrays.asList(EndOfGameEffect.BUILDER)));

        // Any 2 resources (1 card)
        allCards.add(new CivilizationCard(Arrays.asList(ImmediateEffect.ArbitraryResource, ImmediateEffect.ArbitraryResource), Arrays.asList(EndOfGameEffect.MEDICINE)));
        Collections.shuffle(allCards);
        return new CivilizationCardDeck(allCards);
    }

    
}
