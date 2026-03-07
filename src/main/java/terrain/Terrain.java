package terrain;// terrain.Terrain.java
// Base class for terrain tiles (dirt, tunnel, air and rock).
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

/**
 * Base class for terrain tiles in the world.
 * terrain.Terrain is generally not carryable or edible and may define movement properties.
 */
public abstract class Terrain extends WorldObject {
    @Override
    public boolean isCarryable() { return false; }

    @Override
    public boolean isEdible() { return false; }

    @Override
    public char getSymbol() { return 'T'; }

    /**
     * Returns whether entities can traverse this terrain under movement rules.
     */
    public boolean isTraversable() { return false; }

    /**
     * Returns whether this terrain is solid, which matters for support
     * checks like ants needs ground to stand on while in the air
     */
    public boolean isSolid() { return false; }
}