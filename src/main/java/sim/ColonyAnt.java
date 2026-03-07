package sim;// sim.ColonyAnt.java
// Base class for ants that belong to a colony, with a home location and optional food storage.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

import java.util.Random;

/**
 * Shared base class for colony ants that have a nest home and optional food storage location.
 * Intended to support behaviors like returning home, depositing food and foraging.
 */
public class ColonyAnt extends Ant {
    private final Point home;
    private Point foodStore;

    /**
     * Creates a colony ant with a required home location.
     *
     * @param world world the ant lives in
     * @param rng random generator
     * @param pos initial position
     * @param maxEnergy maximum energy
     * @param home colony home position (non-null)
     */
    public ColonyAnt(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home){
        super(world, rng, pos, maxEnergy);
        if (home == null) throw new IllegalArgumentException("home");
        this.home = home;
    }

    public final Point getHome() { return home; }

    public final Point getFoodStore() { return foodStore; }

    public final void setFoodStore(Point foodStore) { this.foodStore = foodStore; }

    /**
     * Returns a direction step toward the colony home location.
     *
     * @return direction toward home
     */
    public Direction returnHome() { return pathFind(home); }


    /**
     * Returns a direction step toward the food storage location.
     * If no food storage is configured, returns CENTER.
     *
     * @return direction toward food storage or CENTER if unset
     */
    public Direction depositFood() {
        if (foodStore == null) return Direction.CENTER;
        //TODO: handle logic when foodStore is null better
        // set behavior to findFood();
        return pathFind(foodStore);
    }

    /**
     * Foraging hook. Intended to choose a direction toward food sources
     * based on pheromones and exploration.
     *
     * @return direction to move next
     */
    public Direction findFood(){
        //TODO: logic for eating when hungry
        // should go to foodstore first then go outside and look for food
        // except for the queen, she is not allowed to leave the nest
        return null;
    }
}