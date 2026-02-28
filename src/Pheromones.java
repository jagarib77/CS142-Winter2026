// Pheromones.java

public class Pheromones {
    private final int width;
    private final int height;

    // [type][row][column]
    // the double held in the location is the amount of the pheromone
    private final double[][][] grid;

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

    // 1-2% loss per tick
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

    // 1-2% loss per tick
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