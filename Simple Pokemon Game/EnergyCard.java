/**
 * Represents a single energy card (universal in this version).
 */
public class EnergyCard extends AbstractCard {
    public EnergyCard(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return getName() + " (Energy)";
    }
}
