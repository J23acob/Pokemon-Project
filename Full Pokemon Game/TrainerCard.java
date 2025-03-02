/**
 * A trainer card that applies an effect when played.
 */
public class TrainerCard implements Card {
    private String name;
    private String effectDescription;

    /**
     * @param name  Trainer card name.
     * @param effectDescription Brief description of its effect.
     */
    public TrainerCard(String name, String effectDescription) {
        this.name = name;
        this.effectDescription = effectDescription;
    }

    public String getName() {
        return name;
    }

    public String getEffectDescription() {
        return effectDescription;
    }

    /**
     * Applies the effect of this trainer card to a player.
     * Effects: 
     *  - Professor's Research: Discards hand, draws 7 cards.
     *  - Bill: Draws 2 cards.
     *  - Lana: Heals 50 HP from an active Water-type Pokémon.
     *  - Gym Trainer: Draw 2 cards; if a Pokémon was knocked out last turn, draw 2 more.
     */
    public void applyEffect(Player player) {
        System.out.println("Applying effect of " + name + ": " + effectDescription);

        // Each effect name is handled separately below.
        if (name.equalsIgnoreCase("Professor's Research")) {
            player.discardHand();
            player.drawCards(7);
        } else if (name.equalsIgnoreCase("Bill")) {
            player.drawCards(2);
        } else if (name.equalsIgnoreCase("Lana")) {
            Pokemon active = player.getActivePokemon();
            if (active != null && active.getType().equalsIgnoreCase("Water")) {
                active.heal(50);
                System.out.println(active.getName() + " is healed by 50 HP.");
            } else {
                System.out.println("No Water type Pokémon to heal.");
            }
        } else if (name.equalsIgnoreCase("Gym Trainer")) {
            player.drawCards(2);
            if (player.hadKnockedOutLastTurn()) {
                player.drawCards(2);
            }
        } else {
            System.out.println("No defined effect for " + name);
        }
    }

    @Override
    public String toString() {
        return name + " [Effect: " + effectDescription + "]";
    }
}
