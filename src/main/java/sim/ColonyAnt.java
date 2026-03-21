package sim;// sim.ColonyAnt.java
// Base class for ants that belong to a colony, with a home location and optional food storage.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

import pheromones.PheromoneType;
import resources.WorldObject;
import util.Direction;
import util.Point;

import java.util.List;
import java.util.Random;

/**
 * Shared base class for colony ants that have a nest home and optional food storage location.
 * Intended to support behaviors like returning home, depositing food and foraging.
 */
public class ColonyAnt extends Ant {
    private final Point home;
    private Point foodStore;
    private final int colonyID;

    /**
     * Creates a colony ant with a required home location.
     *
     * @param world world the ant lives in
     * @param rng random generator
     * @param pos initial position
     * @param maxEnergy maximum energy
     * @param home colony home position (non-null)
     */
    public ColonyAnt(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home, int colonyID){
        super(world, rng, pos, maxEnergy);
        if (home == null) throw new IllegalArgumentException("home");
        this.home = home;
        this.colonyID = colonyID;
        setFoodStore(home);
    }

    /**
     * Initializes a colony ant with a given starting position, maximum energy capacity, current
     * energy, held item, whether the ant is alive, its previous locations, its home's location,
     * its colonyID, and its food storage's location.
     *
     * @param world world the ant lives in (non-null)
     * @param rng random generator used for decisions (non-null)
     * @param pos initial position (non-null)
     * @param maxEnergy maximum energy capacity, must be > 0
     * @param heldItem the WorldObject the nt is holding
     * @param alive boolean for whether the ant is alive
     * @param lastLocations list storing ant's last locations in the world
     * @param home location of the home point (non-null)
     * @param colonyID a number used to identify ants of its own colony
     * @param foodStore location of the food store (always at home point currently)
     */
    public ColonyAnt(WorldGrid world, Random rng, Point pos, int maxEnergy, int currentEnergy,
               WorldObject heldItem, boolean alive, List<Point> lastLocations, Point home,
                     int colonyID, Point foodStore) {
        super(world, rng, pos, maxEnergy, currentEnergy, heldItem, alive, lastLocations);

        if (home == null) throw new IllegalArgumentException("home");
        this.home = home;
        this.colonyID = colonyID;
        this.foodStore = foodStore;
    }


    public final Point getHome() { return home; }

    public final Point getFoodStore() { return foodStore; }

    public final void setFoodStore(Point foodStore) { this.foodStore = foodStore; }

    public int getColonyId(){ return colonyID; }

    /**
     * Returns a direction step toward the colony home location.
     *
     * @return direction toward home
     */
    public Direction returnHome() { return pathFind(home); }

    public boolean attack(Ant a){
        if(!(a instanceof ColonyAnt enemy)) return false;
        if(enemy.getColonyId() == getColonyId()) return false;

        if(enemy.getPoint().equals(getPoint())){
            if(rng().nextDouble() < .5) enemy.kill();
            return true;
        }
        return false;
    }
}
