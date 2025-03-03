/**
 * A Trainer card with an effect description (not actively used here).
 */
public class TrainerCard extends AbstractCard {
    private String effectDescription;

    public TrainerCard(String name, String effectDescription) {
        super(name);
        this.effectDescription = effectDescription;
    }

    public String getEffectDescription() {
        return effectDescription;
    }

    @Override
    public String toString() {
        return getName() + " [Effect: " + effectDescription + "]";
    }
}
