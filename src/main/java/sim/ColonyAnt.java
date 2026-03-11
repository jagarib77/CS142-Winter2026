package sim;// sim.ColonyAnt.java
// Base class for ants that belong to a colony, with a home location and optional food storage.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

import pheromones.PheromoneType;
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