// Food.java
// Simple carryable, edible object with a nutrition value.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler

/**
 * Represents a food item that can be carried and eaten.
 * Nutrition is stored as a positive integer and can be used to restore energy.
 */
public class Food extends WorldObject {

    // only needed if we want food to restore a certain amount of energy
    private final int nutrition;

    /**
     * Creates a food item with the given nutrition value.
     *
     * @param nutrition energy restored when eaten, must be > 0
     */
    public Food(int nutrition) {
        if (nutrition <= 0) throw new IllegalArgumentException("nutrition must be > 0");
        this.nutrition = nutrition;
    }

    public int getNutrition() {
        return nutrition;
    }

    @Override
    public boolean isCarryable() {
        return true;
    }

    @Override
    public boolean isEdible() {
        return true;
    }

    @Override
    public char getSymbol() {
        return 'F';
    }
}