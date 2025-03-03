import java.util.List;

/**
 * Basic representation of a Pokémon card: name, type, attacks.
 */
public class PokemonCard extends AbstractCard {
    private String type;           // e.g., "Fire"
    private List<String> attacks;  // e.g., ["Scratch", "Ember"]

    public PokemonCard(String name, String type, List<String> attacks) {
        super(name);
        this.type = type;
        this.attacks = attacks;
    }

    public String getType() {
        return type;
    }

    public List<String> getAttacks() {
        return attacks;
    }

    @Override
    public String toString() {
        return getName() + " [" + type + ", Attacks: " + attacks + "]";
    }
}
