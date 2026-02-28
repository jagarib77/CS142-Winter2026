// Resource.java

public abstract class Resource extends WorldObject {
    @Override
    public boolean isCarryable() { return true; }

    @Override
    public boolean isEdible() { return true; }

    @Override
    public char getSymbol() { return '?'; }

    @Override
    public int energyValue() { return 0; }
}