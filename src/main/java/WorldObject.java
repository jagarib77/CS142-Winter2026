// WorldObject.java
// Base type for any object that can exist in the world, including terrain and resources.
// Defines shared properties such as carryability, edibility and energy value.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler

/**
 * Base class for all objects that can exist in the world grid.
 * This includes both terrain types and interactable items such as resources.
 */
public abstract class WorldObject {
    public boolean isCarryable() { return false; }

    public boolean isEdible() { return false; }

    public char getSymbol() { return '?'; }

    public int energyValue() { return 0; }
}