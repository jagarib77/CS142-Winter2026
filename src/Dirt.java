// Dirt.java

public class Dirt extends Terrain {
    @Override
    public boolean isCarryable() { return true; }

    @Override
    public boolean isEdible() { return false; }

    @Override
    public char getSymbol() { return '#'; }

    @Override
    public boolean isTraversable() { return false; }

    @Override
    public boolean isSolid() { return true; }
}