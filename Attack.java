import java.util.ArrayList;
import java.util.List;

public class Attack {
    private String name;
    private List<String> energyCost;
    private int damage;

    public Attack(String name, List<String> energyCost, int damage) {
        this.name = name;
        // Create a new list to avoid external modifications.
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
