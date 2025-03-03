import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Deck of cards with shuffle, draw, and add functionality.
 */
public class Deck {
    private List<Card> cards;
    private Random rand;

    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
        rand = new Random();
    }

    public void shuffle() {
        Collections.shuffle(cards, rand);
    }

    /**
     * Draws one card from the top.
     */
    public Card drawCard() {
        if (cards.isEmpty()) return null;
        return cards.remove(0);
    }

    /**
     * Draws multiple cards at once.
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
    public void addCard(Card c) {
        cards.add(c);
    }

    public int size() {
        return cards.size();
    }
}
