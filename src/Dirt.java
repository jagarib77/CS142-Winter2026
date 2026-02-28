// Dirt.java
// Solid terrain that typically blocks movement but may be diggable later.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler

/**
 * Terrain type representing dirt.
 * Dirt is solid and not traversable under current movement rules.
 */
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