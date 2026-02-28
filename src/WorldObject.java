// WorldObject.java

public abstract class WorldObject {
    public boolean isCarryable() { return false; }

    public boolean isEdible() { return false; }

    public char getSymbol() { return '?'; }

    public int energyValue() { return 0; }
}