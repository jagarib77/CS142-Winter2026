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
    public WorkerAnt(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home){
        super(world, rng, pos, maxEnergy, home);
    }

    /**
     * Convenience factory used by the queen to create a worker ant.
     *
     * @return newly created sim.WorkerAnt instance
     */
    public static WorkerAnt spawn(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home){
        return new WorkerAnt(world, rng, pos, maxEnergy, home);
    }

    @Override
    public char getSymbol() {
        return 'W';
    }

    /**
     * Digging hook. Intended to convert diggable terrain into tunnel tiles and manage
     * carried dirt. Currently unimplemented.
     */
    public void digTunnel(){
        // TODO: add logic for ants to expand the nest
        return;
    }
}