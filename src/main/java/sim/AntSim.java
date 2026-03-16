package sim;// sim.AntSim.java
// Simulation driver that owns the world grid, RNG and list of ants.
// Advances the simulation in discrete ticks and performs global updates.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler and Kyle Hamasaki

import gui.AntSimGUI;
import pheromones.PheromoneType;
import pheromones.Pheromones;
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

            // IF HUNGRY, EAT IF POSSIBLE
            if (ant.isHungry()) {
                ant.eat();
            }

            ColonyAnt colonyAnt = (ColonyAnt) ant;
            Point home = colonyAnt.getHome();

            if (ant instanceof QueenAnt q) {
                // Runs if the Queen is hungry
                if (q.isHungry()) {
                    if (q.getPoint().equals(home)) {
                        // If the Queen is at the Home, then she will retrieve food if possible.
                        if (colonies.get(q.getColonyId() - 1).retrieveFood()) {
                            q.setHeldItem(new Sugar());
                        }
                    } else {
                        // If she is not at Home, then she will move towards home if possible,
                        // else, she will move in a random Direction.
                        if (!q.move(q.getPoint().moveToPoint(home))) {
                            q.move(Direction.randDir(rng));
                        }
                        continue;
                    }
                }

                if (rng.nextInt(50) == 0) {
                    Ant spawned = q.spawnAnt();
                    if (spawned != null) {
                        it.add(spawned);
                    }
                }

                Direction dir;
                if (q.getPoint().getDistanceBetween(home) > 5) {
                    // keep queen near home
                    dir = q.getPoint().moveToPoint(home);
                } else {
                    dir = Direction.randDir(rng);
                }

                if (!q.move(dir)) {
                    q.move(Direction.randDir(rng));
                }

                continue;
            }

            // IF CARRYING FOOD, RETURN HOME
            if (ant.getHeldItem() instanceof Sugar) {
                if (ant.getPoint().equals(home)) {
                    ant.destroyHeld();
                    // Adds the food to the food storage at Home.
                    colonies.get(colonyAnt.getColonyId() - 1).addFood();
                } else {
                    Direction dir = ant.getPoint().moveToPoint(home);
                    if (!ant.move(dir)) {
                        ant.move(Direction.randDir(rng));
                    }
                    double dist = ant.getPoint().getDistanceBetween(home);
                    double amount = Math.max(4.0, Math.min(20.0, 40.0/Math.max(dist, 1.0)));

                    world.getPheromones().add(PheromoneType.FOOD, ant.getPoint(), amount);
                }
                continue;
            }

            // PICK UP FOOD IF STANDING ON IT
            if (ant.pickupObject()) {
                if (ant.getHeldItem() instanceof Sugar) {
                    world.getPheromones().add(PheromoneType.FOOD, ant.getPoint(), 25);
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
            // I worked on the digging behavior, but it seems that the Ants constantly are blocked
            // by the Dirt. Maybe we should let the WorkerAnts dig even while holding Sugar?
            // - Kyle
            Direction dir = ant.smell(world.getPheromones());
            // The Ant moves or digs to the Direction from the pheromones.
            if (!ant.move(dir) && !world.dig((WorkerAnt) ant, ant.getPoint().add(dir))) {
                // If the Ant cannot move or dig, a new Direction is chosen.
                dir = Direction.randDir(rng);
                // The Ant moves or digs to the new Direction.
                if (!ant.move(dir)) {
                    world.dig((WorkerAnt) ant, ant.getPoint().add(dir));
                }
            }

            // EMIT PHEROMONE
            world.getPheromones().add(ant.createPheromone(), ant.getPoint(), 5);
        }

        resolveAntAttacks();

        world.spreadPheromones(.05);
        world.decayPheromones(.99);
        world.getPheromones().capPheromones();

        // clear space around colony
        Pheromones pher = world.getPheromones();
        int radius = 4;
        for(ColonyState c:colonies){
            Point home = c.getHome();

            for (int y=home.y-radius; y<=home.y+radius; ++y) {
                for (int x=home.x-radius; x<=home.x+radius; ++x) {
                    Point p = new Point(x, y);
                    if (!world.inBounds(p)) continue;

                    double dist = p.getDistanceBetween(home);
                    if (dist >= radius) continue;

                    double current = pher.get(PheromoneType.FOOD, p);
                    double newValue;

                    if (dist <= 1) {
                        newValue = 0; // completely clear near nest
                    } else {
                        // linear decay removal from distance 1 -> 4
                        double removeFactor = (radius-dist)/(radius-1);
                        newValue = current*(1.0-removeFactor);
                    }

                    pher.set(PheromoneType.FOOD, p, newValue);
                }
            }
        }
    }

    private void resolveAntAttacks() {
        for (int i=0; i<antColonys.size(); ++i) {
            Ant a = antColonys.get(i);
            if (a==null || !a.isAlive()) continue;
            if (!(a instanceof ColonyAnt colonyAnt)) continue;

            for (int j=i+1; j<antColonys.size(); ++j) {
                Ant b = antColonys.get(j);
                if (b==null || !b.isAlive()) continue;
                if (!(b instanceof ColonyAnt colonyB)) continue;

                if (!a.getPoint().equals(b.getPoint())) continue;
                if (colonyAnt.getColonyId() == colonyB.getColonyId()) continue;

                switch (a) {
                    case GuardAnt guard -> {
                        if (guard.attack(b)) {
                            world.getPheromones().add(PheromoneType.DANGER, a.getPoint(), 5);
                        }
                    }

                    case WorkerAnt worker -> {
                        if (worker.attack(b)) {
                            world.getPheromones().add(PheromoneType.DANGER, a.getPoint(), 5);
                        }
                    }

                    case QueenAnt queen -> {
                        if (queen.attack(b)) {
                            world.getPheromones().add(PheromoneType.DANGER, a.getPoint(), 5);
                        }
                    }

                    default -> {}
                }
            }
        }
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
        AntSim sim = new AntSim(49, 49);
        new AntSimGUI(sim);
    }
}
