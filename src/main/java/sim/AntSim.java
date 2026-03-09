package sim;// sim.AntSim.java
// Simulation driver that owns the world grid, RNG and list of ants.
// Advances the simulation in discrete ticks and performs global updates.
// Group Project: Ant Colony Simulator
// Authors: Harrison Butler and Kyle Hamasaki

import gui.AntSimGUI;
import pheromones.PheromoneType;
import pheromones.Pheromones;
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
    private final List<Ant> ants;

    // The rate in which sugar spawns (spawn/cycle).
    private final double sugarSpawnRate = 1;
    // Keeps track of when to spawn sugar. When it is >= 1, spawns sugar.
    private double sugarSpawnProgress = 0;
    // The probability sugar will spawn at a Point
    private final double sugarSpawnProbability = 0.1;

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
        this.ants = new ArrayList<>();

        setupWorld();
        setupAnts();
        setupPheromones();
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

    public List<Ant> getAnts() { return ants; }

    /**
     * Advances the simulation by one tick.
     * Intended responsibilities:
     * - update ant behaviors (movement, actions, combat, etc.)
     * - spawn new ants from Queens
     * - remove dead ants
     * - update pheromones (spread and decay)
     */
    public void step() {
        // TODO: Write GuardAnt's behavior and setting the Point for the food storage. The movement
        //  system I made is imperfect, so feel free to modify anything.
        ListIterator<Ant> it = ants.listIterator(); // safe iterator add
        while (it.hasNext()) {
            Ant ant = it.next();

            // Removes the Ant from the list if it is not alive.
            if (ant == null || !ant.isAlive()) {
                it.remove();
                continue;
            }

            // The Ant's current location
            Point currentPoint = ant.getPoint();
            // The location of the food storage
            Point foodStorePoint = ((ColonyAnt) ant).getFoodStore();

            // The ant eats food (if it is holding Sugar) when it is hungry, and goes to the food
            // storage for food if it exists. Does not release any pheromone.
            if (ant.isHungry()) {
                if (!ant.eat() && !ant.isScavenging() && foodStorePoint != null) {
                    // If the Ant cannot eat and is not scavenging, then it will move to the food
                    // storage. If there is an obstacle, then it will move randomly.
                    if (!ant.move(currentPoint.moveToPoint(foodStorePoint))) {
                        ant.move(Direction.randDir(rng));
                    }

                    // If the Ant is at the food storage, then it will pick up the food. If there
                    // is no food, then the Ant will scavenge. Scavenging means that the Ant will
                    // wander the world to find food instead of going to the food storage.
                    if (currentPoint.equals(foodStorePoint)) {
                        if (!ant.pickupObject()) {
                            ant.setScavenging(true);
                        }
                    }
                    continue;
                }
            }

            // If the Ant has a Sugar, is not hungry, and the food storage exists, then it will go
            // to the food storage and drop the sugar there. Does not release any pheromones.
            if (ant.getHeldItem() instanceof Sugar && foodStorePoint != null) {
                // The Ant will go to the food storage. If there is an obstacle, it will move in a
                // random Direction.
                if (!ant.move(((ColonyAnt) ant).depositFood())) {
                    ant.move(Direction.randDir(rng));
                }

                // TODO: I think that there is something wrong with this code because the Ant
                //  becomes frozen at the food storage
                // If the Ant is at the food storage, then it will drop the Sugar.
                if (currentPoint.equals(foodStorePoint)) {
                    ant.dropItem();
                }
                continue;
            }

            if (ant instanceof QueenAnt q) {
                // random chance for each queen to spawn an ant
                if (rng.nextInt(50) == 0) {
                    it.add(q.spawnAnt()); // safe add
                }

                // If the Queen is too far away from the home Point, then it will move towards
                // the home Point.
                if (q.getHome().getDistanceBetween(ant.getPoint()) > 3) {
                    if (q.move(q.getPoint().moveToPoint(q.getHome()))) {
                        continue;
                    }
                }
            }

            // If the Ant is a WorkerAnt, is not carrying anything, has enough energy, and is
            // adjacent to Dirt, then the Ant will dig out the Dirt to make it a Tunnel and
            // release WALKING_TRAIL pheromones.
            if (ant instanceof WorkerAnt worker && ant.getHeldItem() == null &&
                    ant.getEnergy() >= 15) {
                // The adjacent Dirt Terrain around the Ant.
                List<Point> adjacentDirt = new ArrayList<>();

                for (Direction dir : Direction.allDirections()) {
                    // An adjacent point to the Ant.
                    Point adjacent = worker.getPoint().add(dir);
                    if (world.inBounds(adjacent) && world.getTerrainAt(adjacent) instanceof Dirt) {
                        adjacentDirt.add(adjacent);
                    }
                }

                // The Ant will dig out an adjacent Dirt terrain if it exists. If not, then it will
                // move based on pheromones (as shown in the later code).
                if (adjacentDirt.size() > 0) {
                    world.dig(worker, adjacentDirt.get(rng.nextInt(adjacentDirt.size())));
                    worker.pickupObject();
                    world.getPheromones().add(worker.createPheromone(), worker.getPoint(), 1);
                    continue;
                }
            }

            // The Direction the ant will move based on the pheromones
            Direction direction = ant.smell(world.getPheromones());
            // Makes the Ant move in the Direction based on the pheromone. If the Ant cannot move
            // said Direction, then it will move in a random Direction.
            if (!ant.move(direction)) {
                ant.move(Direction.randDir(rng));
            }

            if (ant.pickupObject()) {
                if (ant.getHeldItem() instanceof Sugar && ((ColonyAnt) ant).getFoodStore() != null
                        && !((ColonyAnt) ant).getFoodStore().equals(ant.getPoint())) {
                    // If the Ant picks up Sugar, and is not at the food storage, then
                    // it will release FOOD pheromone.
                    world.getPheromones().add(PheromoneType.FOOD, ant.getPoint(), 5);
                }
            } else if (ant instanceof WorkerAnt) {
                // If the Ant is a WorkerAnt, and did not pick up Sugar, then it will create a
                // WALKING_TRAIL pheromone.
                world.getPheromones().add(ant.createPheromone(), ant.getPoint(), 1.5);
            }
        }

        // Spawns sugar. Commented this out in case you do not want to spawn sugar.
        /*
        while (sugarSpawnProgress >= 1) {
            spawnSugar();
            sugarSpawnProgress -= 1;
        }
        sugarSpawnProgress += sugarSpawnRate;
        */

        // I didn't plan out these functions so when I wrote them they use different maths.
        world.spreadPheromones(.1); // value is the % amount that spreads out, 1% spread
        world.decayPheromones(.99); // value is the % remaining after decay, 1% loss
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

        /* commented this out as i plan to rework the basic idea of how the sim will work
        // Carve a centered square room of Tunnel tiles.
        // 3x3 if size is odd, 4x4 if size is even.
        int roomSize = (width%2 == 0) ? 4 : 3;
        int half = roomSize/2;

        // For odd (3): cx = w/2, start = cx-1, end = cx+1
        // For even (4): cx = w/2, start = cx-2, end = cx+1
        int startX = (width/2) - half;
        int startY = (height/2) - half;
        int endX = startX + roomSize-1;
        int endY = startY + roomSize-1;

        for (int y=startY; y<=endY; ++y) {
            for (int x=startX; x<=endX; ++x) {
                world.setTerrain(new Point(x, y), new Tunnel());
            }
        }

        // tunnel from starting nest to surface, either 1 or 2 wide depending on size of world
        startX = (width-half)/2;
        startY = 0;

        endX = startX + half-1;
        endY = height/2;

        for (int y=startY; y<=endY; ++y) {
            for (int x=startX; x<=endX; ++x) {
                world.setTerrain(new Point(x, y), new Tunnel());
            }
        }

        // carve a rectangular room of air for the surface world
        for (int y=0; y<=5; ++y) {
            for (int x=0; x<width; ++x) {
                world.setTerrain(new Point(x, y), new Air());
            }
        }

        // Add a couple rocks as obstacles
        for (int r=0; r<25; ++r){
            Point rand = new Point(rng.nextInt(width), rng.nextInt(height));
            if (world.getTerrainAt(rand) instanceof Dirt){
                world.setTerrain(rand, new Rock());
            } else { --r; } // try again
        }

        // Sets up the sugar in the world.
        setupSugar();
        */
    }

    /**
     * Spawns the initial ants for the simulation (at minimum, a queen).
     * This method is called once during construction.
     */
    private void setupAnts() {
        Point home = new Point(world.getWidth()/2, world.getHeight()/2);
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

    /**
     * sets up Pheromones as the world is generated
     * reuses the code from setupWorld but changes Pheromones instead of Terrain
     */
    public void setupPheromones(){
        int width = world.getWidth();
        int height = world.getHeight();
        Pheromones pheromones = world.getPheromones();

        // fill in starting tunnel with walking trail pheromones
        // 3x3 if size is odd, 4x4 if size is even.
        int roomSize = (width%2 == 0) ? 4 : 3;
        int half = roomSize/2;

        // For odd (3): cx = w/2, start = cx-1, end = cx+1
        // For even (4): cx = w/2, start = cx-2, end = cx+1
        int startX = (width/2) - half;
        int startY = (height/2) - half;
        int endX = startX + roomSize-1;
        int endY = startY + roomSize-1;

        for (int y=startY; y<=endY; ++y) {
            for (int x=startX; x<=endX; ++x) {
                pheromones.add(PheromoneType.WALKING_TRAIL, new Point(x, y), 1);
            }
        }

        // tunnel from starting nest to surface, either 1 or 2 wide depending on size of world
        startX = (width-half)/2;
        startY = 0;

        endX = startX + half-1;
        endY = height/2;

        for (int y=startY; y<=endY; ++y) {
            for (int x=startX; x<=endX; ++x) {
                pheromones.add(PheromoneType.WALKING_TRAIL, new Point(x, y), 1);
            }
        }
    }

    // Initializes the sugar in the world. Every Dirt terrain has a chance of having sugar.
    public void setupSugar() {
        for (int h = 0; h < world.getHeight(); h++) {
            for (int w = 0; w <= world.getWidth(); w++) {
                Point currentPoint = new Point(w, h);
                // seeing what the game looks like when sugar can be anywhere
                if (true /*world.getTerrainAt(currentPoint) instanceof Dirt*/) {
                    if (Math.random() <= sugarSpawnProbability) {
                        world.setObjectAt(currentPoint, new Sugar());
                    }
                }
            }
        }
    }

    // Spawns sugar in the world. Commented this out in case you do not want to spawn sugar.
    // By Kyle
    /*
    public void spawnSugar() {
        // X position where the bunch of sugar spawns
        int xPos = rng.nextInt(world.getWidth());
        // Y position where the bunch of sugar spawns
        int yPos = rng.nextInt(world.getHeight());

        // Spawns sugar in a 3x3 area (assuming the xPos and yPos are not at the border).
        // Sugar can spawn on Dirt, Tunnel, and Sky, but not Rock.
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Point sugarSpawnPoint = new Point(xPos + i, yPos + j);

                if (world.inBounds(sugarSpawnPoint)) {
                    if (!(world.getTerrainAt(sugarSpawnPoint) instanceof Rock)) {
                        // The probability of sugar spawning
                        if (Math.random() < 0.1) {
                            world.setObjectAt(sugarSpawnPoint, new Sugar());
                        }
                    }
                }
            }
        }

    }
    */

    /**
     * Program entry point. Creates the simulation and launches the GUI.
     * @param args unused
     */
    public static void main(String[] args) {
        printIntro();
        // 93 is based on the default window size, not sure why 100 is too big
        AntSim sim = new AntSim(93, 50);
        new AntSimGUI(sim);
    }
}
