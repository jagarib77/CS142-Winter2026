package resources;// resources.Resource.java
// Base class for edible, carryable world objects such as sugar.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

/**
 * Base class for resources that ants can carry and eat.
 * Subclasses should override symbol and energy value.
 */
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