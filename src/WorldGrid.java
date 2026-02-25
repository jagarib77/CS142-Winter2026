// WorldGrid.java

public class WorldGrid {
    private final WorldObject[][] objects;
    private final Terrain[][] terrain;
    // [type][row][column]
    private final Pheromones[][][] pheromones;

    private final int width;
    private final int height;

    public WorldGrid(int width, int height){
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Invalid grid size");
        }

        this.width = width;
        this.height = height;
        terrain = new Terrain[height][width];
        objects = new WorldObject[height][width];
        pheromones = new Pheromones[PheromoneType.values().length][width][height];

        // Default: fill with Dirt
        for (int y=0; y<height; ++y) {
            for (int x=0; x<width; ++x) {
                terrain[y][x] = new Dirt();
            }
        }
        // objects is already null filled
        // pheromones is already null filled
    }

    public WorldObject getObjectAt(Point pos){
        if (!inBounds(pos)) return null;
        return objects[pos.y][pos.x];
    }

    public boolean setObjectAt(Point pos, WorldObject obj){
        if (!inBounds(pos)) return false;
        objects[pos.y][pos.x] = obj;
        return true;
    }

    public WorldObject takeObject(Point pos){
        if (!inBounds(pos)) return null;
        if (!getObjectAt(pos).isCarryable() || getObjectAt(pos) == null) return null;
        WorldObject obj = getObjectAt(pos);
        setObjectAt(pos, null);
        return obj;
    }

    // movement rules
    public boolean canMoveTo(Point p) {
        if (!inBounds(p)) return false;

        Terrain t = terrain[p.y][p.x];

        // tunnel is always walkable
        if (t instanceof Tunnel) return true;

        // air is only walkable if supported from below
        if (t instanceof Air) {
            Point below = p.add(Direction.SOUTH);
            if (!inBounds(below)) return false;

            Terrain belowTerrain = terrain[below.y][below.x];
            return (belowTerrain != null) && (belowTerrain.isSolid());
        }

        return false;
    }

    public boolean inBounds(Point p){
        return  p != null &&
                p.x >= 0  && p.x < width &&
                p.y >= 0  && p.y < height;
    }

}
