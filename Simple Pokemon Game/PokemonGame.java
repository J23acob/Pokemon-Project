import java.util.ArrayList;
import java.util.List;

/**
 * Cut-down Pokémon TCG with universal energies and minimal gameplay.
 */
public class PokemonGame {
    private Player player1;
    private Player player2;

    public PokemonGame() {
        Deck d1 = buildDeck();
        Deck d2 = buildDeck();
        d1.shuffle();
        d2.shuffle();
        player1 = new Player("Player 1", d1);
        player2 = new Player("Player 2", d2);
    }

    /**
     * Main flow: draw 7, check for Pokemon, draw 6 prizes, pick active, then battle.
     */
    public void startGame() {
        System.out.println("Starting streamlined Pokémon TCG game!");

        // Initial draw & mulligan checks.
        player1.drawInitialHand();
        player2.drawInitialHand();
        // 6 prizes each
        player1.drawPrizes();
        player2.drawPrizes();
        // Setup active Pokemon + bench
        player1.setupActivePokemon();
        player2.setupActivePokemon();

        // Decide who goes first
        boolean p1first = (Math.random() < 0.5);
        Player current = p1first ? player1 : player2;
        Player opponent = (current == player1) ? player2 : player1;
        System.out.println((p1first ? "Player 1" : "Player 2") + " goes first.");

        while (true) {
            System.out.println("\n--- " + current.getName() + "'s Turn ---");
            current.takeTurn(opponent);

            // Swap
            Player temp = current;
            current = opponent;
            opponent = temp;
        }
    }

    // main method <6 lines
    public static void main(String[] args) {
        new PokemonGame().startGame();
    }

    /**
     * Builds a 60-card deck: 4 Pokemon, 15 energies, remainder trainers.
     */
    private Deck buildDeck() {
        List<Card> cards = new ArrayList<>();
        // 4 Pokemon
        cards.add(new BattlePokemon("Charmander", "Fire", List.of("Flare"), 50));
        cards.add(new BattlePokemon("Bulbasaur", "Grass", List.of("Leaf Slash"), 50));
        cards.add(new BattlePokemon("Squirtle", "Water", List.of("Water Gun"), 50));
        cards.add(new BattlePokemon("Pikachu", "Electric", List.of("Spark"), 50));

        // 15 universal energies
        for (int i = 0; i < 15; i++) {
            cards.add(new EnergyCard("Universal Energy"));
        }

        // Remainder trainer cards to total 60
        int totalNow = cards.size();
        for (int i = totalNow; i < 60; i++) {
            cards.add(new TrainerCard("Trainer" + (i+1), "Demo effect"));
        }
        return new Deck(cards);
    }
}
