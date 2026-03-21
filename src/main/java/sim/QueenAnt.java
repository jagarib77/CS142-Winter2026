package sim;// sim.QueenAnt.java
// Colony queen role that can spawn new worker and guard ants.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

import resources.WorldObject;
import util.Direction;
import util.Point;

import java.util.List;
import java.util.Random;

/**
 * Queen ant role. Intended to remain in the nest and spawn new colony ants.
 * Spawning costs energy and requires adjacent walkable tiles.
 */
public class QueenAnt extends ColonyAnt {
    /**
     * Creates a queen ant with a home location.
     */
    public QueenAnt(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home, int ColonyID){
        super(world, rng, pos, maxEnergy, home, ColonyID);
    }

    /**
     * Initializes a queen ant with a given starting position, maximum energy capacity, current
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
     * @param home location of the home point
     * @param colonyID a number used to identify ants of its own colony
     * @param foodStore location of the food store (always at home point currently)
     */
    public QueenAnt(WorldGrid world, Random rng, Point pos, int maxEnergy, int currentEnergy,
                    WorldObject heldItem, boolean alive, List<Point> lastLocations, Point home,
                    int colonyID, Point foodStore) {
        super(world, rng, pos, maxEnergy, currentEnergy, heldItem, alive, lastLocations, home, colonyID, foodStore);
    }

    /**
     * Convenience factory used to construct a sim.QueenAnt.
     *
     * @return newly created sim.QueenAnt instance
     */
    public static QueenAnt spawn(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home, int colonyID){
        return new QueenAnt(world, rng, pos, maxEnergy, home, colonyID);
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

        // queen has a 1/50 chance to spawn any ant then a 1/40 chance to spawn a queen this
        // gives a total chance of 1/2000 to spawn, with queen life being 500, there is a good
        // chance a new queen will be spawned but no guarantees.
        boolean spawnNewQueen = (rng().nextInt(40) == 0);

        int type = rng().nextInt(2);
        Point home = getHome();

        for (Direction d : Direction.allDirections()) {
            Point spawn = getPoint().add(d);
            if (!this.canMoveTo(spawn)) continue;

            if (spawnNewQueen){ // uses its own chance based logic
                changeEnergy(-200);
                return QueenAnt.spawn(world(), rng(), spawn, 500, home, getColonyId());
            }

            if (type == 0) {
                changeEnergy(-5);
                return GuardAnt.spawn(world(), rng(), spawn, 200, home, getColonyId());
            } else {
                changeEnergy(-5);
                return WorkerAnt.spawn(world(), rng(), spawn, 200, home, getColonyId());
            }
        }

        return null;
    }

}
