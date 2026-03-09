package sim;// sim.QueenAnt.java
// Colony queen role that can spawn new worker and guard ants.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

import util.Direction;
import util.Point;

import java.util.Random;

/**
 * Queen ant role. Intended to remain in the nest and spawn new colony ants.
 * Spawning costs energy and requires adjacent walkable tiles.
 */
public class QueenAnt extends ColonyAnt {
    /**
     * Creates a queen ant with a home location.
     */
    public QueenAnt(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home){
        super(world, rng, pos, maxEnergy, home);
    }

    /**
     * Convenience factory used to construct a sim.QueenAnt.
     *
     * @return newly created sim.QueenAnt instance
     */
    public static QueenAnt spawn(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home){
        return new QueenAnt(world, rng, pos, maxEnergy, home);
    }

    @Override
    public char getSymbol() {
        return 'Q';
    }

    /**
     * Attempts to spawn a new ant in an adjacent walkable tile.
     * Spawning costs energy and returns null if no valid space exists or energy is too low.
     *
     * @return a newly created ant, or null if spawning failed
     */
    public Ant spawnAnt() {
        if (getEnergy() < 50) return null; // avoids killing the queen ant to quickly

        // queen has a 1/50 chance to spawn any ant then a 1/80 chance to spawn a queen this
        // gives a total chance of 1/400 to spawn, with queen life being 500, there is a good
        // chance a new queen will be spawned but no guarantees.
        boolean spawnNewQueen = (rng().nextInt(80) == 0);

        int type = rng().nextInt(2);
        Point home = getHome();

        for (Direction d : Direction.allDirections()) {
            Point spawn = getPoint().add(d);
            if (!world().canMoveTo(spawn)) continue;

            if (spawnNewQueen){ // uses its own chance based logic
                changeEnergy(-50);
                return GuardAnt.spawn(world(), rng(), spawn, 500, home);
            }

            if (type == 0) {
                changeEnergy(-5);
                return GuardAnt.spawn(world(), rng(), spawn, 200, home);
            } else {
                changeEnergy(-5);
                return WorkerAnt.spawn(world(), rng(), spawn, 200, home);
            }
        }

        return null;
    }

}