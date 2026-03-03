// Tunnel.java
// Traversable terrain representing the nest interior.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler

/**
 * Terrain type representing a tunnel tile.
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