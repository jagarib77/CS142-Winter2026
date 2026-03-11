package sim;// sim.WorkerAnt.java
// Colony ant role intended for foraging and nest expansion behaviors.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

import util.Point;

import java.util.Random;

/**
 * Worker ant role. Intended responsibilities include foraging and digging tunnels.
 * Currently provides a spawn helper and symbol override.
 */
public class WorkerAnt extends ColonyAnt {
    /**
     * Creates a worker ant at a position with a home location.
     */
    public WorkerAnt(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home, int colonyID){
        super(world, rng, pos, maxEnergy, home, colonyID);
    }

    /**
     * Convenience factory used by the queen to create a worker ant.
     *
     * @return newly created sim.WorkerAnt instance
     */
    public static WorkerAnt spawn(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home, int colonyID){
        return new WorkerAnt(world, rng, pos, maxEnergy, home, colonyID);
    }

    @Override
    public char getSymbol() {
        return 'W';
    }

    public boolean forage(){
        if(getHeldItem() != null) return false;
        return pickupObject();
    }
}