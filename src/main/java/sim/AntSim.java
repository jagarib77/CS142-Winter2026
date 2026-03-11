package sim;// sim.AntSim.java
// Simulation driver that owns the world grid, RNG and list of ants.
// Advances the simulation in discrete ticks and performs global updates.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler and Kyle Hamasaki

import gui.AntSimGUI;
import pheromones.PheromoneType;
import resources.Colony;
import resources.Sugar;
import terrain.*;
import util.Direction;
import util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * Main simulation model for the ant colony project.
 * Owns world state and the list of ants, and advances the simulation each tick.
 */
public class AntSim {
    private final WorldGrid world;
    private final Random rng;
    private final List<Ant> antColonys;
    private final List<ColonyState> colonies;

    private int nextColonyId = 1;
    /**
     * Creates a simulation with the given grid size and a default Random generator.
     *
     * @param width world width in tiles
     * @param height world height in tiles
     */
    public AntSim(int width, int height) {
        this(width, height, new Random());
    }

    /**
     * Creates a simulation with the given grid size and Random generator.
     * Sets up terrain and initial ants.
     *
     * @param width world width in tiles
     * @param height world height in tiles
     * @param rng random generator used for world setup and behaviors (non-null)
     */
    public AntSim(int width, int height, Random rng) {
        if (rng == null) throw new IllegalArgumentException("rng");

        this.world = new WorldGrid(width, height);
        this.rng = rng;
        this.antColonys = new ArrayList<>();
        this.colonies = new ArrayList<>();

        setupWorld();
    }

    /**
     * Prints a short introduction to the console.
     * Written by Kyle.
     */
    public static void printIntro() {
        System.out.println("Welcome to the Ant Colony Simulator!");
        System.out.println("This program creates a world with ant colonies and simulates ants'");
        System.out.println("behaviors like foraging, fighting, and reproducing.");
    }

    public WorldGrid getWorld() { return world; }

    public List<Ant> getAnts() { return antColonys; }

    public List<ColonyState> getColonies() { return colonies; }

    /**
     * Places a colony object on the map and spawns its starter ants.
     * Returns false if placement is invalid or blocked.
     */
    public boolean placeColony(Point p) {
        if (p == null || !world.inBounds(p)) return false;
        if (world.getObjectAt(p) != null) return false;

        int colonyId = nextColonyId++; // starts at 1 then post increment
        Colony colonyObject = new Colony(colonyId, p);
        world.setObjectAt(p, colonyObject);

        ColonyState colonyState = new ColonyState(colonyId, p);
        colonies.add(colonyState);
        antColonys.addAll(colonyState.spawnStarterAnts(world, rng));

        return true;
    }

    /**
     * Advances the simulation by one tick.
     * Intended responsibilities:
     * - update ant behaviors (movement, actions, combat, etc.)
     * - spawn new ants from Queens
     * - remove dead ants
     * - update pheromones (spread and decay)
     */
    public void step() {
        ListIterator<Ant> it = antColonys.listIterator();

        while (it.hasNext()) {
            Ant ant = it.next();
            if (ant == null || !ant.isAlive()) {
                it.remove();
                continue;
            }

            ColonyAnt colonyAnt = (ColonyAnt) ant;
            Point home = colonyAnt.getHome();

            // QUEEN
            if (ant instanceof QueenAnt q) {
                if (rng.nextInt(50) == 0) {
                    Ant spawned = q.spawnAnt();
                    if (spawned != null) {
                        it.add(spawned);
                    }
                }

                // keep queen near home
                if (q.getPoint().getDistanceBetween(home) > 5) {
                    Direction dir = q.getPoint().moveToPoint(home);
                    if (!q.move(dir)) {
                        q.move(Direction.randDir(rng));
                    }
                }
                continue;
            }

            // IF CARRYING FOOD, RETURN HOME
            if (ant.getHeldItem() instanceof Sugar) {
                if (ant.getPoint().equals(home)) {
                    ant.dropItem();
                } else {
                    Direction dir = ant.getPoint().moveToPoint(home);
                    if (!ant.move(dir)) {
                        ant.move(Direction.randDir(rng));
                    }
                    world.getPheromones().add(PheromoneType.FOOD, ant.getPoint(), 2);
                }
                continue;
            }

            // PICK UP FOOD IF STANDING ON IT
            if (ant.pickupObject()) {
                if (ant.getHeldItem() instanceof Sugar) {
                    world.getPheromones().add(PheromoneType.FOOD, ant.getPoint(), 10);
                    continue;
                }
            }

            // GUARD BEHAVIOR
            if (ant instanceof GuardAnt guard) {
                if (guard.getPoint().equals(home)) {
                    Direction[] dirs = Direction.allDirections();
                    boolean moved = false;

                    for (int i=0; i<dirs.length; ++i) {
                        Direction d = dirs[rng.nextInt(dirs.length)];
                        if (guard.move(d)) {
                            moved = true;
                            break;
                        }
                    }
                    if (moved) continue;
                }

                // return if too far from colony
                if (guard.getPoint().getDistanceBetween(home) > 8) {
                    Direction dir = guard.getPoint().moveToPoint(home);
                    if (!guard.move(dir)) {
                        guard.move(Direction.randDir(rng));
                    }
                    continue;
                }

                // otherwise patrol by smell
                Direction dir = guard.smell(world.getPheromones());
                if (!guard.move(dir)) {
                    guard.move(Direction.randDir(rng));
                }
                continue;
            }

            // WORKER ANT BEHAVIOR
            Direction dir = ant.smell(world.getPheromones());
            if (!ant.move(dir)) {
                ant.move(Direction.randDir(rng));
            }
        }

        world.spreadPheromones(.05);
        world.decayPheromones(.95);
    }

    /**
     * Initializes world terrain layout (nest area, surface air area and obstacles).
     * This method is called once during construction.
     */
    private void setupWorld() {
        int width = world.getWidth();
        int height = world.getHeight();

        for (int y=0; y<=height; ++y) {
            for (int x=0; x<=width; ++x) {
                world.setTerrain(new Point(x, y), new Tunnel());
            }
        }
    }

    /**
     * Program entry point. Creates the simulation and launches the GUI.
     * @param args unused
     */
    public static void main(String[] args) {
        printIntro();
        AntSim sim = new AntSim(49, 49);
        new AntSimGUI(sim);
    }
}
