import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Main class: orchestrates the game flow, including initial setup and battle loop.
 */
public class PokemonGame {
    private Player player1;
    private Player player2;
    private Random rand;

    public PokemonGame() {
        rand = new Random();
        Deck deck1 = createDeck();
        Deck deck2 = createDeck();
        deck1.shuffle();
        deck2.shuffle();
        player1 = new Player("Player 1", deck1);
        player2 = new Player("Player 2", deck2);
    }

    /**
     * Starts the main game flow (initial draws, mulligans, setup, and turn-by-turn battle).
     */
    public void startGame() {
        System.out.println("Starting the Pokémon Game!");
        System.out.println("\nDrawing initial hands...");
        player1.drawInitialHand();
        player2.drawInitialHand();

        // Check for playable hands (mulligans if no Pokémon).
        boolean p1Playable = player1.hasPokemonInHand();
        boolean p2Playable = player2.hasPokemonInHand();
        if (!p1Playable && !p2Playable) {
            System.out.println("Both players have no playable Pokémon. Both mulligan.");
            player1.mulligan();
            player2.mulligan();
        } else if (!p1Playable) {
            System.out.println(player1.getName() + " has no playable Pokémon! Mulligan!");
            player1.mulligan();
            System.out.println(player2.getName() + " gets an extra card advantage.");
            player2.drawExtraCard();
        } else if (!p2Playable) {
            System.out.println(player2.getName() + " has no playable Pokémon! Mulligan!");
            player2.mulligan();
            System.out.println(player1.getName() + " gets an extra card advantage.");
            player1.drawExtraCard();
        }

        // Demonstration: auto-play Bill if Player 1 has it.
        player1.playTrainerCard("Bill");

        // Setup active Pokémon + bench for both players.
        player1.setupActiveAndBench();
        player2.setupActiveAndBench();
        if (player1.getActivePokemon() == null || player2.getActivePokemon() == null) {
            System.out.println("One or both players do not have a valid active Pokémon. The game cannot continue.");
            return;
        }

        System.out.println("\nBattle begins!");
        System.out.println(player1.getName() + " active: " + player1.getActivePokemon());
        System.out.println(player2.getName() + " active: " + player2.getActivePokemon());

        // Decide who goes first by coin flip.
        Scanner scanner = new Scanner(System.in);
        boolean player1Starts = coinFlip();
        Player current, opponent;
        if (player1Starts) {
            current = player1;
            opponent = player2;
            System.out.println(player1.getName() + " will attack first.");
        } else {
            current = player2;
            opponent = player1;
            System.out.println(player2.getName() + " will attack first.");
        }

        // Main battle loop: ends when one player runs out of Pokémon.
        while (true) {
            System.out.println("\n" + current.getName() + "'s turn:");
            if (current == player1) {
                // Interactive turn for Player 1.
                if (!current.hasValidMove()) {
                    System.out.println("No valid moves available. Ending your turn automatically.");
                } else {
                    boolean turnOver = false;
                    while (!turnOver) {
                        System.out.println("\nChoose an action:");
                        System.out.println("1: Attack");
                        System.out.println("2: Attach an Energy Card");
                        System.out.println("3: Play a Trainer Card");
                        System.out.println("4: End Turn");
                        System.out.print("Enter your choice: ");
                        int action = scanner.nextInt();
                        scanner.nextLine();

                        switch (action) {
                            case 1:
                                // Attack
                                List<Attack> attacks = current.getActivePokemon().getAttacks();
                                System.out.println("Attacks:");
                                for (int i = 0; i < attacks.size(); i++) {
                                    String availability = current.getActivePokemon().canUseAttack(i)
                                            ? "Available" : "Not available";
                                    System.out.println(i + ": " + attacks.get(i) + " (" + availability + ")");
                                }
                                System.out.print("Choose an attack by entering its number: ");
                                int attackChoice = scanner.nextInt();
                                scanner.nextLine();
                                if (!current.getActivePokemon().canUseAttack(attackChoice)) {
                                    System.out.println("You don't have enough energy for that attack.");
                                    break;
                                }
                                int damage = current.getActivePokemon().attack(opponent.getActivePokemon(), attackChoice);
                                System.out.println("You used " + current.getActivePokemon().getAttacks().get(attackChoice).getName() +
                                                   " for " + damage + " damage.");
                                turnOver = true;
                                break;

                            case 2:
                                // Attach an energy card
                                List<Integer> energyIndices = new ArrayList<>();
                                for (int i = 0; i < current.getHand().size(); i++) {
                                    if (current.getHand().get(i) instanceof EnergyCard) {
                                        energyIndices.add(i);
                                    }
                                }
                                if (energyIndices.isEmpty()) {
                                    System.out.println("No Energy cards in hand. Returning to options menu.");
                                    break;
                                }
                                System.out.println("Energy cards in hand:");
                                for (int index : energyIndices) {
                                    System.out.println(index + ": " + current.getHand().get(index));
                                }
                                System.out.println("-1: Cancel and return to main menu");
                                System.out.print("Choose an Energy card to attach by entering its index (or -1 to cancel): ");
                                int energyChoice = scanner.nextInt();
                                scanner.nextLine();
                                if (energyChoice == -1) {
                                    System.out.println("Cancelling energy attachment and returning to options menu.");
                                    break;
                                }
                                if (current.getHand().get(energyChoice) instanceof EnergyCard) {
                                    EnergyCard ec = (EnergyCard) current.getHand().get(energyChoice);
                                    if (current.getActivePokemon().isEnergyProficient(ec)) {
                                        current.getActivePokemon().attachEnergy(ec);
                                        current.getHand().remove(energyChoice);
                                        System.out.println("Attached " + ec + " to " + current.getActivePokemon().getName());
                                    } else {
                                        System.out.println("No proficient energy is available. " 
                                                + current.getActivePokemon().getName() 
                                                + " is " + current.getActivePokemon().getType() 
                                                + " type and cannot use " + ec.getType() + " energy.");
                                    }
                                } else {
                                    System.out.println("Invalid choice.");
                                }
                                break;

                            case 3:
                                // Play a trainer card
                                List<Integer> trainerIndices = new ArrayList<>();
                                for (int i = 0; i < current.getHand().size(); i++) {
                                    if (current.getHand().get(i) instanceof TrainerCard) {
                                        trainerIndices.add(i);
                                    }
                                }
                                if (trainerIndices.isEmpty()) {
                                    System.out.println("No Trainer cards in hand.");
                                    break;
                                }
                                System.out.println("Trainer cards in hand:");
                                for (int index : trainerIndices) {
                                    System.out.println(index + ": " + current.getHand().get(index));
                                }
                                System.out.print("Choose a Trainer card to play by entering its index: ");
                                int trainerChoice = scanner.nextInt();
                                scanner.nextLine();
                                if (current.getHand().get(trainerChoice) instanceof TrainerCard) {
                                    String trainerName = ((TrainerCard) current.getHand().get(trainerChoice)).getName();
                                    current.playTrainerCard(trainerName);
                                } else {
                                    System.out.println("Invalid choice.");
                                }
                                break;

                            case 4:
                                // End turn
                                System.out.println("Ending turn without further action.");
                                turnOver = true;
                                break;

                            default:
                                System.out.println("Invalid option.");
                        }
                    }
                }
            } else {
                // Automated turn for Player 2
                if (!current.getActivePokemon().canUseAttack(0)) {
                    // Attach all proficient energies if needed
                    boolean attachedAny = false;
                    for (int i = 0; i < current.getHand().size(); i++) {
                        if (current.getHand().get(i) instanceof EnergyCard) {
                            EnergyCard ec = (EnergyCard) current.getHand().get(i);
                            if (current.getActivePokemon().isEnergyProficient(ec)) {
                                current.getActivePokemon().attachEnergy(ec);
                                System.out.println(current.getName() + " attaches " + ec + " to " + current.getActivePokemon().getName());
                                current.getHand().remove(i);
                                i--; // adjust index after removal
                                attachedAny = true;
                            }
                        }
                    }
                    if (attachedAny && current.getActivePokemon().canUseAttack(0)) {
                        int damage = current.getActivePokemon().attack(opponent.getActivePokemon(), 0);
                        System.out.println(current.getName() + "'s " + current.getActivePokemon().getName() +
                                           " uses " + current.getActivePokemon().getAttacks().get(0).getName() +
                                           " for " + damage + " damage.");
                    } else {
                        System.out.println(current.getName() + " cannot attack this turn.");
                    }
                } else {
                    int damage = current.getActivePokemon().attack(opponent.getActivePokemon(), 0);
                    System.out.println(current.getName() + "'s " + current.getActivePokemon().getName() +
                                       " uses " + current.getActivePokemon().getAttacks().get(0).getName() +
                                       " for " + damage + " damage.");
                }
            }

            // After the current player's turn, check if the opponent's active Pokémon is knocked out.
            System.out.println(opponent.getName() + "'s " + opponent.getActivePokemon().getName() +
                               " now has " + opponent.getActivePokemon().getHP() + " HP.");
            if (opponent.getActivePokemon().isKnockedOut()) {
                System.out.println(opponent.getName() + "'s " + opponent.getActivePokemon().getName() + " is knocked out!");
                opponent.setKnockedOutLastTurn(true);
                if (!opponent.replaceActivePokemon()) {
                    System.out.println(opponent.getName() + " has no Pokémon left to replace the active one.");
                    System.out.println(current.getName() + " wins the game!");
                    break;
                } else {
                    System.out.println(opponent.getName() + " replaces the active Pokémon with: " + opponent.getActivePokemon());
                }
            } else {
                opponent.setKnockedOutLastTurn(false);
            }

            // Swap players for next turn.
            Player temp = current;
            current = opponent;
            opponent = temp;
        }
        scanner.close();
    }

    /**
     * Simulates a coin flip. 
     * @return true if heads, false for tails.
     */
    private boolean coinFlip() {
        return rand.nextBoolean();
    }

    /**
     * Builds a 60-card deck with:
     * - 20 Pokémon (mix of Charmander, Bulbasaur, Squirtle, Psyduck, Bellsprout, Flareon).
     * - 20 Trainer cards (Professor's Research, Bill, Lana, Gym Trainer).
     * - 20 Energy cards (4 each of Water, Grass, Fire, Electric, Basic).
     */
    private Deck createDeck() {
        List<Card> cards = new ArrayList<>();
        // Pokémon
        for (int i = 0; i < 3; i++) {
            cards.add(createCharmander());
            cards.add(createBulbasaur());
            cards.add(createSquirtle());
            cards.add(createPsyduck());
        }
        for (int i = 0; i < 4; i++) {
            cards.add(createBellsprout());
            cards.add(createFlareon());
        }
        // Trainer
        for (int i = 0; i < 4; i++) {
            cards.add(new TrainerCard("Professor's Research", "Discard your hand and draw 7 cards"));
            cards.add(new TrainerCard("Bill", "Draw 2 cards"));
            cards.add(new TrainerCard("Lana", "Heal 50 damage to any Water type Pokémon"));
            cards.add(new TrainerCard("Gym Trainer", "Draw 2 cards; if any of your Pokémon were knocked out last turn then draw an additional 2 cards"));
        }
        for (int i = 0; i < 4; i++) {
            cards.add(new TrainerCard("Professor's Research", "Discard your hand and draw 7 cards"));
        }
        // Energy
        for (int i = 0; i < 4; i++) {
            cards.add(new EnergyCard("Water"));
            cards.add(new EnergyCard("Grass"));
            cards.add(new EnergyCard("Fire"));
            cards.add(new EnergyCard("Electric"));
            cards.add(new EnergyCard("Basic"));
        }
        return new Deck(cards);
    }

    // Helper methods to construct Pokémon with attacks.
    private Pokemon createCharmander() {
        List<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Scratch", Arrays.asList("Basic"), 10));
        attacks.add(new Attack("Ember", Arrays.asList("Fire", "Basic"), 30));
        return new Pokemon("Charmander", "Fire", 50, attacks);
    }
    private Pokemon createFlareon() {
        List<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Super Singe", Arrays.asList("Basic", "Fire"), 20));
        attacks.add(new Attack("Flamethrower", Arrays.asList("Fire", "Basic", "Basic", "Basic"), 70));
        return new Pokemon("Flareon", "Fire", 80, attacks);
    }
    private Pokemon createBulbasaur() {
        List<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Razor Leaf", Arrays.asList("Basic", "Grass"), 30));
        return new Pokemon("Bulbasaur", "Grass", 60, attacks);
    }
    private Pokemon createBellsprout() {
        List<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Vine Whip", Arrays.asList("Grass"), 10));
        return new Pokemon("Bellsprout", "Grass", 50, attacks);
    }
    private Pokemon createSquirtle() {
        List<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Water Gun", Arrays.asList("Water"), 20));
        return new Pokemon("Squirtle", "Water", 60, attacks);
    }
    private Pokemon createPsyduck() {
        List<Attack> attacks = new ArrayList<>();
        attacks.add(new Attack("Headache", Arrays.asList("Water"), 20));
        return new Pokemon("Psyduck", "Water", 60, attacks);
    }

    /**
     * Entry point for running the game directly.
     */
    public static void main(String[] args) {
        PokemonGame game = new PokemonGame();
        game.startGame();
    }
}
