package sim;

import util.Direction;
import util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Tracks the colony id, home location and starter ants.
 */
public class ColonyState {
    private final int colonyId;
    private final Point home;
    private final List<Ant> ants;

    public ColonyState(int colonyId, Point home) {
        if (home == null) throw new IllegalArgumentException("home is null");
        this.colonyId = colonyId;
        this.home = home;
        this.ants = new ArrayList<>();
    }

    public int getColonyId() {
        return colonyId;
    }

    public Point getHome() {
        return home;
    }

    public List<Ant> getAnts() {
        return ants;
    }

    /**
     * Spawns 1 queen, 2 guards and 2 workers around the nest if possible.
     * All colony ants use the nest as both home and current food storage.
     */
    public List<Ant> spawnStarterAnts(WorldGrid world, Random rng) {
        List<Ant> spawned = new ArrayList<>();

        QueenAnt queen = QueenAnt.spawn(world, rng, home, 500, home, colonyId);
        queen.setFoodStore(home);
        spawned.add(queen);

        Point north = home.add(Direction.NORTH);
        Point south = home.add(Direction.SOUTH);

        Point guardLeft = north.add(Direction.WEST);
        Point guardRight = north.add(Direction.EAST);
        Point workerLeft = south.add(Direction.WEST);
        Point workerRight = south.add(Direction.EAST);

        if (world.canMoveTo(guardLeft)) {
            GuardAnt g = GuardAnt.spawn(world, rng, guardLeft, 300, home, colonyId);
            g.setFoodStore(home);
            spawned.add(g);
        }

        if (world.canMoveTo(guardRight)) {
            GuardAnt g = GuardAnt.spawn(world, rng, guardRight, 300, home, colonyId);
            g.setFoodStore(home);
            spawned.add(g);
        }

        if (world.canMoveTo(workerLeft)) {
            WorkerAnt w = WorkerAnt.spawn(world, rng, workerLeft, 300, home, colonyId);
            w.setFoodStore(home);
            spawned.add(w);
        }

        if (world.canMoveTo(workerRight)) {
            WorkerAnt w = WorkerAnt.spawn(world, rng, workerRight, 300, home, colonyId);
            w.setFoodStore(home);
            spawned.add(w);
        }

        ants.addAll(spawned);
        return spawned;
    }
}