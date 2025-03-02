import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a Pokémon card with name, type, HP, and a list of attacks.
 */
public class Pokemon implements Card {
    private String name;
    private String type;  // Fire, Grass, Water, Electric
    private int hp;
    private int maxHP;
    private List<Attack> attacks;
    private List<String> attachedEnergies;  // Track the energy types attached

    /**
     * @param name   Pokémon name.
     * @param type   Pokémon type ("Fire", "Water").
     * @param hp     Starting hit points.
     * @param attacks List of possible attacks.
     */
    public Pokemon(String name, String type, int hp, List<Attack> attacks) {
        this.name = name;
        this.type = type;
        this.hp = hp;
        this.maxHP = hp;
        this.attacks = attacks;
        this.attachedEnergies = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getHP() {
        return hp;
    }

    public List<Attack> getAttacks() {
        return attacks;
    }

    public List<String> getAttachedEnergies() {
        return attachedEnergies;
    }

    /**
     * Reduces HP by 'damage' amount, not below 0.
     */
    public void takeDamage(int damage) {
        hp -= damage;
        if (hp < 0) hp = 0;
    }

    /**
     * @return True if HP is 0.
     */
    public boolean isKnockedOut() {
        return hp == 0;
    }

    /**
     * Heals this Pokémon by 'amount', up to maxHP.
     */
    public void heal(int amount) {
        hp += amount;
        if (hp > maxHP) hp = maxHP;
    }

    /**
     * Attaches an energy card of the correct type or Basic.
     */
    public void attachEnergy(EnergyCard energy) {
        attachedEnergies.add(energy.getType());
    }

    /**
     * Checks if the energy card can be attached based on Pokémon's type or if the energy is "Basic".
     */
    public boolean isEnergyProficient(EnergyCard energy) {
        return energy.getType().equalsIgnoreCase("Basic") || energy.getType().equalsIgnoreCase(this.type);
    }

    /**
     * Checks if the Pokémon has enough attached energy to use the specified attack index.
     */
    public boolean canUseAttack(int attackIndex) {
        if (attackIndex < 0 || attackIndex >= attacks.size()) return false;
        List<String> cost = attacks.get(attackIndex).getEnergyCost();

        // Tally cost
        Map<String, Integer> costCount = new HashMap<>();
        for (String energyType : cost) {
            costCount.put(energyType, costCount.getOrDefault(energyType, 0) + 1);
        }

        // Tally attached energies
        Map<String, Integer> energyCount = new HashMap<>();
        for (String energyType : attachedEnergies) {
            energyCount.put(energyType, energyCount.getOrDefault(energyType, 0) + 1);
        }

        // Compare cost with what's attached
        for (Map.Entry<String, Integer> entry : costCount.entrySet()) {
            String reqType = entry.getKey();
            int reqAmt = entry.getValue();
            if (energyCount.getOrDefault(reqType, 0) < reqAmt) {
                return false;
            }
        }
        return true;
    }

    /**
     * Executes the attack at 'attackIndex', dealing damage to 'opponent'.
     */
    public int attack(Pokemon opponent, int attackIndex) {
        if (attackIndex < 0 || attackIndex >= attacks.size()) {
            System.out.println("Invalid attack index for " + name);
            return 0;
        }
        Attack chosenAttack = attacks.get(attackIndex);
        int damage = chosenAttack.getDamage();
        opponent.takeDamage(damage);
        return damage;
    }

    @Override
    public String toString() {
        return name + " [" + type + ", HP: " + hp + ", Attacks: " + attacks + ", Energies: " + attachedEnergies + "]";
    }
}
