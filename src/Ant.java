// Ant.java
// Base class for all ants. Tracks position, energy, life state and an optional held item.
// Provides shared actions like move, pickup, drop and eat.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler

import java.util.Random;

/**
 * Abstract base class for all ants in the simulation.
 * Stores shared state (world reference, RNG, energy, position and held item)
 * and implements common behaviors used by all ant roles.
 */
public abstract class Ant {
    private final WorldGrid world;
    private final Random rng;

    private int currentEnergy;
    private final int maxEnergy;

    private int x;
    private int y;

    private WorldObject heldItem = null;
    private boolean alive = true;

    /**
     * Creates an ant at a given starting position with a maximum energy capacity.
     * Validates inputs and initializes current energy to the maximum.
     *
     * @param world world the ant lives in (non-null)
     * @param rng random generator used for decisions (non-null)
     * @param pos initial position (non-null)
     * @param maxEnergy maximum energy capacity, must be > 0
     */
    public Ant(WorldGrid world, Random rng, Point pos, int maxEnergy){
        if (world == null) throw new IllegalArgumentException("world is null");
        if (rng == null) throw new IllegalArgumentException("rng is null");
        if (pos == null) throw new IllegalArgumentException("pos is null");
        if (maxEnergy <= 0) throw new IllegalArgumentException("maxEnergy is <= 0");

        this.world = world;
        this.rng = rng;
        this.maxEnergy = maxEnergy;
        this.currentEnergy = maxEnergy;
        this.x = pos.x;;
        this.y = pos.y;
    }

    protected final WorldGrid world() { return world; }
    protected final Random rng() { return rng; }

    public final int getX() { return x; }
    public final int getY() { return y; }
    public final Point getPoint() { return new Point(x, y); }

    public final boolean isAlive() { return alive; }
    public final int getEnergy() { return currentEnergy; }
    public char getSymbol() { return 'A'; }
    public WorldObject getHeldItem(){ return heldItem; }

    /**
     * Adds delta to the ant's energy, clamping to max energy.
     * If energy drops to 0 or below, the ant is killed.
     *
     * @param delta positive or negative energy change
     */
    protected final void changeEnergy(int delta) {
        currentEnergy += delta;
        if (currentEnergy <= 0) kill();
        if (currentEnergy > maxEnergy) currentEnergy = maxEnergy;
    }

    /**
     * Kills this ant if it is alive. If the ant is holding an item, it attempts
     * to drop it on the current tile first (drop is not guaranteed).
     */
    public final void kill(){
        if (!alive) return;
        if (heldItem != null) dropItem(); // attempts to drop item, no guarantees though
        alive = false;
    }

    /**
     * Attempts to drop the currently held item onto the ant's current tile.
     *
     * @return true if the item was dropped, false otherwise
     */
    public boolean dropItem(){
        if (heldItem == null || world.getObjectAt(getPoint()) != null) return false;
        world.setObjectAt(getPoint(), heldItem);
        heldItem = null;
        return true;
    }

    /**
     * Attempts to move one tile in the given direction based on world movement rules.
     * Movement costs energy: -1 if not carrying an item, -2 if carrying an item.
     *
     * @param dir direction to move
     * @return true if the move succeeded, false otherwise
     */
    public boolean move(Direction dir){
        Point self = getPoint();
        if (world.canMoveTo(self.add(dir))){
            x += dir.dx;
            y += dir.dy;
            // costs extra to move while holding an item
            changeEnergy(heldItem == null ? -1 : -2);
            return true;
        }
        return false;
    }

    /**
     * checks the pheromones at this ants location then updates currentAction variable
     * return something like a direction or modify a value like current action
     */
    public void smell(){
        //TODO: does something based on strongest pheromone in its tile
        // unless previous behavior overrides the smell

        //TODO: probably should add in a variable that controls the and behavior based on
        // on some criteria either its hungry or has been told by pheromones what to do
        // or does some default behavior like wander
    }

    /**
     * Attempts to pick up a carryable world object from the ant's current tile.
     * Costs a fixed amount of energy on success.
     *
     * @return true if pickup succeeded, false otherwise
     */
    public boolean pickupObject() {
        WorldObject obj = world.getObjectAt(getPoint());
        if (heldItem != null || obj == null || !obj.isCarryable()) return false;
        heldItem = world.takeObject(getPoint());
        changeEnergy(-5);
        return true;
    }

    /**
     * Eats the currently held item if it is edible, converting its energy value
     * into ant energy and consuming the item.
     *
     * @return true if the ant ate successfully, false otherwise
     */
    public boolean eat(){
        if (heldItem == null || !heldItem.isEdible()) return false;
        changeEnergy(heldItem.energyValue());
        heldItem = null;
        return true;
    }

    public boolean isHungry(){ return currentEnergy < 50; }

    /**
     * Pathfinding hook for moving toward a target point.
     * Currently, unimplemented and returns CENTER by default.
     *
     * @param target destination to move toward
     * @return a direction step toward the target
     */
    public Direction pathFind(Point target){
        //TODO: complete pathfinding
        return Direction.CENTER;
    }
}