/**
 * An energy card that can be attached to a Pokémon.
 */
public class EnergyCard implements Card {
    private String type;  //"Fire", "Water", "Basic"

    /**
     * @param type Energy type (Basic or matches a Pokémon type).
     */
    public EnergyCard(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Energy: " + type;
    }
}
