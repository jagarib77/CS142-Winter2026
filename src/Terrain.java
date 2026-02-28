// Terrain.java

public abstract class Terrain extends WorldObject {
    @Override
    public boolean isCarryable() { return false; }

    @Override
    public boolean isEdible() { return false; }

    @Override
    public char getSymbol() { return 'T'; }

    public boolean isTraversable() { return false; }

    public boolean isSolid() { return false; }
}