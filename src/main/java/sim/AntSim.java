package sim;// sim.AntSim.java
// Simulation driver that owns the world grid, RNG and list of ants.
// Advances the simulation in discrete ticks and performs global updates.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler, Kyle Hamasaki and Dmytro Shyliuk

import gui.AntSimGUI;
import pheromones.PheromoneType;
import pheromones.Pheromones;
import resources.Colony;
import resources.Sugar;
import resources.WorldObject;
import terrain.*;
import util.Direction;
import util.Point;

import java.io.File;
import java.util.*;

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

    public static final Scanner console = new Scanner(System.in);
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

    public AntSim(Random rng, WorldGrid world, List<Ant> antColonys,
                  List<ColonyState> colonies, int nextColonyId) {
        if (rng == null) throw new IllegalArgumentException("rng");

        this.world = world;
        this.rng = rng;
        this.antColonys = antColonys;
        this.colonies = colonies;
        this.nextColonyId = nextColonyId;
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

            // The Ant moves or digs to the Direction from the pheromones.
            //Kyle and Dmytro
            // I think this version is more about finding food/doing something based
            // on smell, and then if can't move reattempts again in random direction
            // The dig function is incorporated in the worker ants general behaivor.
            // so previous refrences to the ant in this class includes the dig behaivor.
            if (ant instanceof WorkerAnt worker) {
                Direction dir = worker.smell(world.getPheromones());
                if (!worker.move(dir)) {
                    // If the Ant cannot move, or dig and then move, a new Direction is chosen.
                    dir = Direction.randDir(rng);
                    // The Ant tries again to move, or dig and then move in the same step
                    // to the new Direction.
                    worker.move(dir);
                }
            }
            // Adds food pheromone when picks up food (not ideal situation)
            // but when it leaves path due to holding food it makes ants follow it
            // if phermone is weak.
            // Just kind of observation I had when food is far from colony and their was
            // little food before (at the begining) right next to the colony, they
            // will just sort of circle around in that area and not go down to the food on the other edge.
            // Maybe wrong. Maybe it also due to food smell being high. Some reason
            // it behaves this way. Guard ants are also behaving kind of weird
            // or maybe it just by design (did not look into that code so yeah)
            // EMIT PHEROMONE
            world.getPheromones().add(ant.createPheromone(), ant.getPoint(), 5);

        }

        resolveAntAttacks();

        world.addSmellAtFood(100);
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
        printIntro();
        startProgram();
    }

    /**
     * Prints an introduction about what the program is about.
     */
    public static void printIntro() {
        System.out.println("Welcome to the Ant Simulation!");
        System.out.println("This program simulates ant colonies digging tunnels, foraging for");
        System.out.println("food, and fighting other colonies.");
        System.out.println();
    }

    /**
     * Asks the user whether they want to load a file or begin the program.
     */
    public static void startProgram() {
        System.out.println("Do you want to load a file?");
        System.out.print("Type \"y\" or \"n\": ");
        String userInput = console.next();
        System.out.println();

        if (userInput.equalsIgnoreCase("y")) {
            loadFile();
        } else if (userInput.equalsIgnoreCase("n")) {
            AntSim sim = new AntSim(49, 49);
            new AntSimGUI(sim);
        } else {
            System.out.println("Invalid input");
            System.out.println();
            startProgram();
        }
    }

    /**
     * Prompts the user for a file and uses its content to create the simulation. The user can
     * also go back to the beginning of the program by typing "Exit".
     */
    public static void loadFile() {
        System.out.print("Input file's name, or input \"Exit\" to exit: ");
        String userInput = console.next();
        System.out.println();

        if (userInput.equalsIgnoreCase("EXIT")) {
            // If the user types "Exit", then the user will be asked if they want to load a file
            // again.
            startProgram();
        } else {
            try {
                File file = new File(userInput);
                Scanner input = new Scanner(file);

                int width = input.nextInt();
                int height = input.nextInt();
                input.nextLine();

                WorldObject[][] objects = new WorldObject[height][width];
                Terrain[][] terrains = new Terrain[height][width];
                Pheromones pheromones = new Pheromones(width, height);

                // Loads the WorldObjects
                for (int row = 0; row < height; row++) {
                    String line = input.nextLine();
                    Scanner lineScanner = new Scanner(line);
                    for (int col = 0; col < width; col++) {
                        String symbol = lineScanner.next();
                        if (symbol.equals("S")) {
                            int energyValue = lineScanner.nextInt();
                            int foodCount = lineScanner.nextInt();
                            objects[row][col] = new Sugar(energyValue, foodCount);
                        } else if (symbol.equals("C")) {
                            int colonyId = lineScanner.nextInt();
                            Point home = new Point(lineScanner.nextInt(), lineScanner.nextInt());
                            objects[row][col] = new Colony(colonyId, home);
                        }
                    }
                }

                // Loads the Terrain
                for (int row = 0; row < height; row++) {
                    String line = input.nextLine();
                    Scanner lineScanner = new Scanner(line);
                    for (int col = 0; col < width; col++) {
                        String symbol = lineScanner.next();
                        if (symbol.equals("_")) {
                            terrains[row][col] = new Air();
                        } else if (symbol.equals("#")) {
                            terrains[row][col] = new Dirt();
                        } else if (symbol.equals("R")) {
                            terrains[row][col] = new Rock();
                        } else if (symbol.equals(".")) {
                            terrains[row][col] = new Tunnel();
                        }
                    }
                }

                // Loads the Pheromones
                for (PheromoneType type : PheromoneType.values()) {
                    for (int row = 0; row < height; row++) {
                        String line = input.nextLine();
                        Scanner lineScanner = new Scanner(line);
                        for (int col = 0; col < width; col++) {
                            pheromones.set(type, new Point(col, row), lineScanner.nextDouble());
                        }
                    }
                }

                // The WorldGrid
                WorldGrid world = new WorldGrid(width, height, objects, terrains, pheromones);


                List<Ant> antColonies = new ArrayList<>();
                int numOfAnts = input.nextInt();
                input.nextLine();

                // Loads the Ants
                for (int i = 0; i < numOfAnts; i++) {
                    String line = input.nextLine();
                    Scanner lineScanner = new Scanner(line);

                    String antSymbol = lineScanner.next();
                    int currentEnergy = lineScanner.nextInt();
                    int maxEnergy = lineScanner.nextInt();
                    Point pos = new Point(lineScanner.nextInt(), lineScanner.nextInt());
                    boolean alive = lineScanner.nextBoolean();

                    String itemSymbol = lineScanner.next();
                    WorldObject heldItem;
                    if (itemSymbol.equals("S")) {
                        int energyValue = lineScanner.nextInt();
                        int foodCount = lineScanner.nextInt();
                        heldItem = new Sugar(energyValue, foodCount);
                    } else {
                        heldItem = null;
                    }

                    int memoryLength = lineScanner.nextInt();
                    List<Point> lastLocations = new ArrayList<>();
                    for (int j = 0; j < memoryLength; j++) {
                        lastLocations.add(new Point(lineScanner.nextInt(), lineScanner.nextInt()));
                    }

                    Point home = new Point(lineScanner.nextInt(), lineScanner.nextInt());
                    Point foodStore = new Point(lineScanner.nextInt(), lineScanner.nextInt());
                    int colonyId = lineScanner.nextInt();

                    ColonyAnt ant;
                    Random rng = new Random();
                    if (antSymbol.equals("G")) {
                        ant = new GuardAnt(world, rng, pos, maxEnergy, currentEnergy, heldItem, alive,
                                lastLocations, home, colonyId, foodStore);
                    } else if (antSymbol.equals("Q")) {
                        ant = new QueenAnt(world, rng, pos, maxEnergy, currentEnergy, heldItem, alive,
                                lastLocations, home, colonyId, foodStore);
                    } else if (antSymbol.equals("W")) {
                        ant = new WorkerAnt(world, rng, pos, maxEnergy, currentEnergy, heldItem, alive,
                                lastLocations, home, colonyId, foodStore);
                    } else {
                        throw new IllegalArgumentException();
                    }

                    antColonies.add(ant);
                }

                int nextColonyId = input.nextInt() + 1;
                input.nextLine();

                // Loads the colonies
                List<ColonyState> colonies = new ArrayList<>();
                for (int j = 1; j < nextColonyId; j++) {
                    String line = input.nextLine();
                    Scanner lineScanner = new Scanner(line);

                    int currentColonyId = lineScanner.nextInt();
                    int foodCount = lineScanner.nextInt();
                    Point home = new Point(lineScanner.nextInt(), lineScanner.nextInt());

                    colonies.add(new ColonyState(currentColonyId, home, foodCount));
                }

                AntSim sim = new AntSim(new Random(), world, antColonies, colonies, nextColonyId);
                new AntSimGUI(sim);
            } catch (Exception e) {
                // If the program cannot load the file, then it prompts the user for another file.
                System.out.println("Cannot read file, try again");
                System.out.println();
                loadFile();
            }
        }
    }
}
