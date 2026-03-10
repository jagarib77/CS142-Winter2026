package resources;

import sim.*;
import util.Direction;
import util.Point;

import java.util.List;
import java.util.Random;

public class Colony extends WorldObject {
    private int foodCount = 20;
    private List<Ant> ants;
    private final Point home;
    private final WorldGrid world;
    private final Random rng;

    public Colony(Point home, WorldGrid world, Random rng){
        this.home = home;
        this.world = world;
        this.rng = rng;
        setupAnts();
    }

    private void setupAnts(){
        assert ants != null;
        ants.add(QueenAnt.spawn(world, rng, home, 500, home));

        // guards
        Point homeNorth = home.add(Direction.NORTH);
        ants.add(GuardAnt.spawn(world, rng, homeNorth.add(Direction.WEST), 300, home));
        ants.add(GuardAnt.spawn(world, rng, homeNorth.add(Direction.EAST), 300, home));

        // workers
        Point homeSouth = home.add(Direction.SOUTH);
        ants.add(WorkerAnt.spawn(world, rng, homeSouth.add(Direction.WEST), 300, home));
        ants.add(WorkerAnt.spawn(world, rng, homeSouth.add(Direction.EAST), 300, home));
    }

    @Override
    public char getSymbol(){ return 'C'; }
}