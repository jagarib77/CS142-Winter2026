package pheromones;// pheromones.Pheromones.java
// Stores and updates pheromone concentrations across the grid for multiple pheromone types.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

/**
 * Stores pheromone concentrations as a 3D array indexed by type, x and y.
 * Supports reading, adding, setting, decay and spreading of pheromones each tick.
 */
public class Pheromones {
    private final int width;
    private final int height;

    // [type][row][column]
    // the double held in the location is the amount of the pheromone
    private final double[][][] grid;

    /**
     * Creates the pheromone grid for a given world size.
     *
     * @param width world width in tiles
     * @param height world height in tiles
     */
    public Pheromones(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new double[PheromoneType.values().length][width][height];
    }

    public double get(PheromoneType type, Point p){
        return grid[type.type][p.x][p.y];
    }

    public void add(PheromoneType type, Point p, double amount){
        grid[type.type][p.x][p.y] += amount;
    }

    public void set(PheromoneType type, Point p, double value){
        grid[type.type][p.x][p.y] = value;
    }

    /**
     * Applies multiplicative decay to all pheromone values.
     *
     * @param decayRate multiplier applied to each cell (example: 0.99 for 1% loss)
     */
    public void decay(double decayRate){
        for (int t=0; t<PheromoneType.values().length; ++t) {
            for (int x=0; x<width; ++x) {
                for (int y=0; y<height; ++y) {
                    grid[t][x][y] *= decayRate;
                }
            }
        }
    }

    public boolean inBounds(Point p){
        return  p != null &&
                p.x >= 0  && p.x < width &&
                p.y >= 0  && p.y < height;
    }

    /**
     * Spreads a fraction of each cell's pheromone value to its cardinal neighbors.
     * The total pheromone is conserved per type during spreading.
     *
     * @param spreadRate fraction moved out of each cell (example: 0.01 for 1% spread)
     */
    public void spread(double spreadRate) {
        for (int t=0; t<grid.length; ++t) {
            double[][] next = new double[width][height];

            for (int x=0; x<width; ++x) {
                for (int y=0; y<height; ++y) {
                    double amount = grid[t][x][y];
                    if (amount == 0) continue;

                    Point here = new Point(x, y);
                    // valid is the number of spots the pheromones can go to
                    int valid = 0;
                    for (Direction d : Direction.allDirections()) {
                        Point there = here.add(d);
                        if (inBounds(there)) ++valid;
                    }
                    // do nothing if gas cant go anywhere
                    if (valid == 0) {
                        next[x][y] += amount;
                        continue;
                    }

                    double totalOut = amount*spreadRate;
                    double remain = amount-totalOut;
                    double portion = totalOut/valid;
                    next[x][y] += remain;

                    for (Direction d : Direction.allDirections()) {
                        Point there = here.add(d);
                        if (inBounds(there)) {
                            next[there.x][there.y] += portion;
                        }
                    }
                }
            }
            grid[t] = next;
        }
    }
}