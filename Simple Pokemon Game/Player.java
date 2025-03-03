import java.util.ArrayList;
import java.util.List;

/**
 * Holds a deck, hand, prizes, active Pokémon, and bench.
 */
public class Player {
    private String name;
    private Deck deck;
    private List<Card> hand;
    private List<Card> prizes;
    private BattlePokemon activePokemon;
    private List<BattlePokemon> bench;

    public Player(String name, Deck deck) {
        this.name = name;
        this.deck = deck;
        this.hand = new ArrayList<>();
        this.prizes = new ArrayList<>();
        this.bench = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    /**
     * Draws 7 cards, mulligans if no Pokémon is found.
     */
    public void drawInitialHand() {
        hand.addAll(deck.drawCards(7));
        System.out.println(name + " draws initial hand: " + hand);
        if (!hasPokemonInHand()) {
            System.out.println(name + " has no Pokémon, Mulligan!");
            mulligan();
        }
    }

    /**
     * Checks if the hand contains any Pokémon card.
     */
    private boolean hasPokemonInHand() {
        for (Card c : hand) {
            if (c instanceof PokemonCard) {
                return true;
            }
        }
        return false;
    }

    private void mulligan() {
        for (Card c : hand) {
            deck.addCard(c);
        }
        hand.clear();
        deck.shuffle();
        hand.addAll(deck.drawCards(7));
        System.out.println(name + " new hand: " + hand);
    }

    /**
     * Draws 6 prize cards.
     */
    public void drawPrizes() {
        prizes.addAll(deck.drawCards(6));
        System.out.println(name + " sets aside 6 Prize cards.");
    }

    /**
     * Chooses first BattlePokemon as active, places up to 5 more on bench.
     */
    public void setupActivePokemon() {
        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);
            if (c instanceof BattlePokemon) {
                activePokemon = (BattlePokemon) c;
                hand.remove(i);
                System.out.println(name + " chooses " + activePokemon.getName() + " as active.");
                break;
            }
        }
        bench.clear();
        List<Card> toRemove = new ArrayList<>();
        for (Card c : hand) {
            if (c instanceof BattlePokemon && bench.size() < 5) {
                bench.add((BattlePokemon)c);
                toRemove.add(c);
            }
        }
        hand.removeAll(toRemove);
        System.out.println(name + " bench: " + bench);
    }

    public BattlePokemon getActivePokemon() {
        return activePokemon;
    }

    /**
     * If knocked out, tries to replace from bench. Returns false if bench is empty.
     */
    public boolean replaceKnockedOut() {
        if (bench.isEmpty()) return false;
        activePokemon = bench.remove(0);
        return true;
    }

    /**
     * Attaches first found EnergyCard from hand if available.
     */
    public boolean attachEnergyIfPossible() {
        if (activePokemon == null) return false;
        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);
            if (c instanceof EnergyCard) {
                activePokemon.attachEnergy((EnergyCard) c);
                hand.remove(i);
                System.out.println(name + " attaches " + c + " to " + activePokemon.getName());
                return true;
            }
        }
        return false;
    }

    /**
     * Player's turn: attach energy if possible, then attack if able.
     */
    public void takeTurn(Player opponent) {
        attachEnergyIfPossible();
        if (activePokemon != null && activePokemon.canAttack()) {
            int dmg = activePokemon.attack(opponent.getActivePokemon());
            System.out.println(name + " attacks " + opponent.getName() + "'s " + 
                               opponent.getActivePokemon().getName() +
                               " for " + dmg + " damage!");
            if (opponent.getActivePokemon().isKnockedOut()) {
                System.out.println(opponent.getName() + "'s " + opponent.getActivePokemon().getName() + " is knocked out!");
                if (!opponent.replaceKnockedOut()) {
                    System.out.println(opponent.getName() + " has no bench! " + name + " wins!");
                    System.exit(0);
                } else {
                    System.out.println(opponent.getName() + " promotes " + opponent.getActivePokemon().getName());
                }
            }
        } else {
            System.out.println(name + "'s Pokemon can't attack this turn.");
        }
    }
}
