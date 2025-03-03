import java.util.ArrayList;
import java.util.List;

/**
 * A Pokémon that can battle: HP, attached energies, and a simple attack system.
 */
public class BattlePokemon extends PokemonCard {
    private int hp;
    private int maxHP;
    private List<String> attachedEnergies;

    public BattlePokemon(String name, String type, List<String> attacks, int hp) {
        super(name, type, attacks);
        this.hp = hp;
        this.maxHP = hp;
        this.attachedEnergies = new ArrayList<>();
    }

    public int getHP() {
        return hp;
    }

    public boolean isKnockedOut() {
        return hp == 0;
    }

    public void takeDamage(int dmg) {
        hp -= dmg;
        if (hp < 0) hp = 0;
    }

    /**
     * Attaches one universal energy card.
     */
    public void attachEnergy(EnergyCard energy) {
        attachedEnergies.add(energy.getName());
    }

    /**
     * Needs at least one energy to attack.
     */
    public boolean canAttack() {
        return attachedEnergies.size() >= 1;
    }

    /**
     * Attacks for a fixed 20 damage if canAttack() is true.
     */
    public int attack(BattlePokemon defender) {
        if (!canAttack()) return 0;
        int dmg = 20;
        defender.takeDamage(dmg);
        return dmg;
    }

    @Override
    public String toString() {
        return super.toString() + " [HP: " + hp + ", Energies: " + attachedEnergies + "]";
    }
}
