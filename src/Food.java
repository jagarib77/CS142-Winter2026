// Food.java

public class Food extends WorldObject {

    // only needed if we want food to restore a certain amount of energy
    private final int nutrition;

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