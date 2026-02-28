// Sugar.java
// A resource that provides energy when eaten.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler

/**
 * Sugar resource. Carryable and edible, restoring a fixed amount of energy.
 */
public class Sugar extends Resource {
    @Override
    public boolean isCarryable() { return true; }

    @Override
    public boolean isEdible() { return true; }

    @Override
    public char getSymbol() { return '$'; }

    @Override
    public int energyValue() { return 50; }
}