package terrain;// terrain.Rock.java
// Solid, untraversable terrain used as an obstacle.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

/**
 * terrain.Terrain type representing rock.
 * terrain.Rock is solid and blocks movement.
 */
public class Rock extends Terrain {
    @Override
    public boolean isCarryable() { return false; }

    @Override
    public boolean isEdible() { return false; }

    @Override
    public char getSymbol() { return 'R'; }

    @Override
    public boolean isTraversable() { return false; }

    @Override
    public boolean isSolid() { return true; }
}