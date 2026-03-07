package terrain;// terrain.Air.java
// Represents above-ground air tiles.
// terrain.Air is not traversable by default and only becomes walkable if supported from below.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

/**
 * terrain.Terrain type representing open air in the surface area of the world.
 * terrain.Air is typically non-solid and non-traversable unless movement rules allow it
 * (for example, if supported by solid terrain below).
 */
public class Air extends Terrain {
    @Override
    public boolean isCarryable() { return false; }

    @Override
    public boolean isEdible() { return false; }

    @Override
    public char getSymbol() { return ' '; }

    @Override
    public boolean isTraversable() { return false; }

    public boolean isUnmovable() { return true; }

    @Override
    public boolean isSolid() { return false; }
}