package terrain;// terrain.Dirt.java
// Solid terrain that typically blocks movement but may be diggable later.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

/**
 * terrain.Terrain type representing dirt.
 * terrain.Dirt is solid and not traversable under current movement rules.
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