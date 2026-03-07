package terrain;// terrain.Tunnel.java
// Traversable terrain representing the nest interior.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

/**
 * terrain.Terrain type representing a tunnel tile.
 * Tunnels are traversable and non-solid under current rules.
 */
public class Tunnel extends Terrain {
    @Override
    public boolean isCarryable() { return false; }

    @Override
    public boolean isEdible() { return false; }

    @Override
    public char getSymbol() { return '.'; }

    @Override
    public boolean isTraversable() { return true; }

    @Override
    public boolean isSolid() { return true; }
}