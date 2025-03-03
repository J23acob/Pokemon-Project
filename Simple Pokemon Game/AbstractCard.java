/**
 * Serves as a base class for specific card types.
 */
public abstract class AbstractCard implements Card {
    private String name;

    public AbstractCard(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
