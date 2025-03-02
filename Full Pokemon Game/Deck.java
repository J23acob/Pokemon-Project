import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Represents a deck of 60 cards (Pokémon, Energy, Trainer).
 */
public class Deck {
    private List<Card> cards;
    private Random rand;

    /**
     * @param cards Pre-constructed list of 60 cards.
     */
    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
        this.rand = new Random();
    }

    /**
     * Shuffles the deck in place.
     */
    public void shuffle() {
        Collections.shuffle(cards, rand);
    }

    /**
     * Draws the top card from the deck.
     * @return The drawn card, or null if deck is empty.
     */
    public Card drawCard() {
        if (cards.isEmpty()) return null;
        return cards.remove(0);
    }

    /**
     * Draws multiple cards from the top.
     * @param n Number of cards to draw.
     * @return List of drawn cards.
     */
    public List<Card> drawCards(int n) {
        List<Card> drawn = new ArrayList<>();
        for (int i = 0; i < n && !cards.isEmpty(); i++) {
            drawn.add(drawCard());
        }
        return drawn;
    }

    /**
     * Adds a card to the bottom of the deck.
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * @return The number of cards remaining in the deck.
     */
    public int remainingCards() {
        return cards.size();
    }
}
