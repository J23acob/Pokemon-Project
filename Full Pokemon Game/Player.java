import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player with a name, a deck, a hand, an active Pokémon, and a bench of up to 5 Pokémon.
 */
public class Player {
    private String name;
    private Deck deck;
    private List<Card> hand;
    private Pokemon activePokemon;
    private List<Pokemon> bench;
    private boolean knockedOutLastTurn;

    /**
     * @param name Player name.
     * @param deck Pre-constructed Deck of 60 cards.
     */
    public Player(String name, Deck deck) {
        this.name = name;
        this.deck = deck;
        this.hand = new ArrayList<>();
        this.bench = new ArrayList<>();
        this.knockedOutLastTurn = false;
    }

    public String getName() {
        return name;
    }

    /**
     * Draws 7 cards as the initial hand.
     */
    public void drawInitialHand() {
        hand.clear();
        hand.addAll(deck.drawCards(7));
        System.out.println(name + " draws initial hand: " + hand);
    }

    /**
     * Draws the top card from the deck.
     */
    public void drawCard() {
        Card card = deck.drawCard();
        if (card != null) {
            hand.add(card);
            System.out.println(name + " draws: " + card);
        } else {
            System.out.println(name + " cannot draw a card. Deck is empty.");
        }
    }

    /**
     * Draws one extra card (used when opponent mulligans).
     */
    public void drawExtraCard() {
        System.out.println(name + " draws an extra card as an advantage.");
        drawCard();
    }

    /**
     * Draws multiple cards at once.
     */
    public void drawCards(int n) {
        for (int i = 0; i < n; i++) {
            drawCard();
        }
    }

    /**
     * Checks if the hand contains a Pokémon.
     */
    public boolean hasPokemonInHand() {
        for (Card card : hand) {
            if (card instanceof Pokemon) {
                return true;
            }
        }
        return false;
    }

    /**
     * Discards the entire hand (for Professor's Research).
     */
    public void discardHand() {
        System.out.println(name + " discards their hand.");
        hand.clear();
    }

    /**
     * Mulligan if no Pokémon is in hand. Return the old hand to deck, shuffle, and draw 7 new cards.
     */
    public void mulligan() {
        System.out.println(name + " shuffles their hand back into the deck and draws a new hand.");
        for (Card card : hand) {
            deck.addCard(card);
        }
        hand.clear();
        deck.shuffle();
        hand.addAll(deck.drawCards(7));
        System.out.println(name + "'s new hand: " + hand);
    }

    /**
     * Picks the first Pokémon in hand as active, then places up to 5 more Pokémon onto the bench.
     */
    public void setupActiveAndBench() {
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            if (card instanceof Pokemon) {
                activePokemon = (Pokemon) card;
                hand.remove(i);
                System.out.println(name + " chooses " + activePokemon.getName() + " as their active Pokémon.");
                break;
            }
        }
        List<Card> toRemove = new ArrayList<>();
        for (Card card : hand) {
            if (card instanceof Pokemon && bench.size() < 5) {
                bench.add((Pokemon) card);
                toRemove.add(card);
            }
        }
        hand.removeAll(toRemove);
        System.out.println(name + " sets up bench: " + bench);
    }

    /**
     * Replaces the active Pokémon with the first on the bench (if available).
     * @return true if replacement successful, false if bench is empty.
     */
    public boolean replaceActivePokemon() {
        if (bench.size() > 0) {
            activePokemon = bench.remove(0);
            return true;
        }
        return false;
    }

    public Pokemon getActivePokemon() {
        return activePokemon;
    }

    public List<Card> getHand() {
        return hand;
    }

    public boolean hadKnockedOutLastTurn() {
        return knockedOutLastTurn;
    }

    public void setKnockedOutLastTurn(boolean value) {
        knockedOutLastTurn = value;
    }

    /**
     * Attempts to find a trainer card in hand by name and play it.
     */
    public void playTrainerCard(String trainerName) {
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            if (card instanceof TrainerCard) {
                TrainerCard trainer = (TrainerCard) card;
                if (trainer.getName().equalsIgnoreCase(trainerName)) {
                    System.out.println(name + " plays Trainer card: " + trainer);
                    trainer.applyEffect(this);
                    hand.remove(i);
                    return;
                }
            }
        }
        System.out.println(name + " does not have a " + trainerName + " card in hand.");
    }

    /**
     * Checks if this player has any valid moves (attack, attach proficient Energy, or a Trainer).
     */
    public boolean hasValidMove() {
        if (activePokemon != null) {
            for (int i = 0; i < activePokemon.getAttacks().size(); i++) {
                if (activePokemon.canUseAttack(i)) {
                    return true;
                }
            }
        }
        // Check for proficient Energy
        for (Card card : hand) {
            if (card instanceof EnergyCard) {
                if (activePokemon != null && activePokemon.isEnergyProficient((EnergyCard) card)) {
                    return true;
                }
            }
        }
        // Check for Trainer cards
        for (Card card : hand) {
            if (card instanceof TrainerCard) {
                return true;
            }
        }
        return false;
    }
}
