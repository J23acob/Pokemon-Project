import java.util.ArrayList;
import java.util.List;

/**
 * Represents a specific attack a Pokémon can perform.
 */
public class Attack {
    private String name;
    private List<String> energyCost;
    private int damage;

    /**
     * @param name  Attack name.
     * @param energyCost  List of energy types required (e.g., ["Fire", "Basic"]).
     * @param damage  Amount of damage the attack does.
     */
    public Attack(String name, List<String> energyCost, int damage) {
        this.name = name;
        this.energyCost = new ArrayList<>(energyCost);
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public List<String> getEnergyCost() {
        return energyCost;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public String toString() {
        return name + " (Cost: " + energyCost + ", Damage: " + damage + ")";
    }
}
