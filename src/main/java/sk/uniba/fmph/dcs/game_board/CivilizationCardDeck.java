package sk.uniba.fmph.dcs.game_board;

import sk.uniba.fmph.dcs.stone_age.EndOfGameEffect;
import sk.uniba.fmph.dcs.stone_age.ImmediateEffect;

import java.util.*;

/**
 * The {@code CivilizationCardDeck} class represents the deck of civilization cards used in the game.
 */
public class CivilizationCardDeck {

    /**
     * A deque representing the deck of civilization cards.
     * Cards are drawn from the front of the deque.
     */
    private Deque<CivilizationCard> deck;

    /**
     * Constructs a new {@code CivilizationCardDeck} and initializes it with all the civilization cards.
     * The deck is shuffled upon initialization.
     */
    public CivilizationCardDeck() {
        deck = new LinkedList<>();
        initializeDeck();
    }

    /**
     * Initializes the deck with all the civilization cards and shuffles it.
     * According to the Stone Age game rules, there are 36 civilization cards.
     */
    private void initializeDeck() {
        List<CivilizationCard> cards = createAllCivilizationCards();
        Collections.shuffle(cards); // Shuffle the cards to randomize the deck
        deck.addAll(cards);
    }

    /**
     * Creates all 36 the civilization cards as per the Stone Age game rules.
     *
     * @return a list of all civilization cards
     */
    private List<CivilizationCard> createAllCivilizationCards() {
        List<CivilizationCard> cards = new ArrayList<>();

        for (int i = 0; i < 4; i++) { // 8
            cards.add(new CivilizationCard(Collections.singletonList(ImmediateEffect.FOOD), null));
            cards.add(new CivilizationCard(Collections.singletonList(ImmediateEffect.WOOD), null));
        }

        for (int i = 0; i < 2; i++) { // 14 + 2
            cards.add(new CivilizationCard(Collections.singletonList(ImmediateEffect.CLAY), null));
            cards.add(new CivilizationCard(Collections.singletonList(ImmediateEffect.STONE), null));
            cards.add(new CivilizationCard(Collections.singletonList(ImmediateEffect.GOLD), null));
            cards.add(new CivilizationCard(Collections.singletonList(ImmediateEffect.ArbitraryResource), null));

            cards.add(new CivilizationCard(Collections.singletonList(ImmediateEffect.GOLD),
                    Collections.singletonList(EndOfGameEffect.WRITING))); // green
            cards.add(new CivilizationCard(Collections.singletonList(ImmediateEffect.STONE),
                    Collections.singletonList(EndOfGameEffect.MEDICINE))); // green
            cards.add(new CivilizationCard(Collections.singletonList(ImmediateEffect.CLAY),
                    Collections.singletonList(EndOfGameEffect.POTTERY))); // green
        }
        cards.add(new CivilizationCard(Collections.singletonList(ImmediateEffect.TOOL),
                Collections.singletonList(EndOfGameEffect.ART))); // green
        cards.add(new CivilizationCard(Collections.singletonList(ImmediateEffect.AGRICULTURE),
                Collections.singletonList(EndOfGameEffect.MUSIC))); // green

        for (int i = 0; i < 5; i++) {
            cards.add(new CivilizationCard(Collections.singletonList(ImmediateEffect.TOOL), null));
        }

        for (int i = 0; i < 3; i++) {
            cards.add(new CivilizationCard(Collections.singletonList(ImmediateEffect.AGRICULTURE), null));
        }
        cards.add(new CivilizationCard(null, Collections.singletonList(EndOfGameEffect.FARMER)));
        cards.add(new CivilizationCard(null, Collections.singletonList(EndOfGameEffect.TOOL_MAKER)));
        cards.add(new CivilizationCard(null, Collections.singletonList(EndOfGameEffect.BUILDER)));
        cards.add(new CivilizationCard(null, Collections.singletonList(EndOfGameEffect.SHAMAN)));

        return cards;
    }

    /**
     * Draws the top card from the deck.
     * If the deck is empty, returns an empty {@code Optional}.
     *
     * if the deck is not empty;
     * otherwise, an empty {@code Optional}
     */
    public Optional<CivilizationCard> getTop() {
        if (deck.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(deck.pollFirst()); // Remove and return the top card
        }
    }

    /**
     * @return a string representing the state of the deck
     */
    public String state() {
        return "CivilizationCardDeck: " + deck.size() + " cards remaining.";
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }
}