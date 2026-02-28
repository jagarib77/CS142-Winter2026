// WorldGrid.java

public class WorldGrid {
    private final WorldObject[][] objects;
    private final Terrain[][] terrain;
    private final Pheromones pheromones;

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
        pheromones = new Pheromones(width, height);

        // Default: fill with Dirt
        for (int y=0; y<height; ++y) {
            for (int x=0; x<width; ++x) {
                terrain[y][x] = new Dirt();
            }
        }
        // objects is already null filled
        // pheromones is already null filled
    }

    public int getWidth(){ return width; }
    public int getHeight(){ return height; }

    //TODO: set terrain at Point to Terrain
    public void setTerrain(Point pos, Terrain type){

    }

    //TODO: return the kind of Terrain at pos
    public Terrain getTerrainAt(Point pos){
        return null;
    }

    // returns true if dirt was dug
    public boolean dig(Point p){
        //TODO: check if Terrain at p is dirt, remove the dirt, then add dirt
        // into ant inventory. Need to make sure ant has room for dirt
        return false;
    }

    public void decayPheromones(double amount) {
        pheromones.decay(amount); // 1% loss each tick
    }

    public void spreadPheromones(double amount) {
        pheromones.decay(amount); // 1% loss each tick
    }

    public WorldObject getObjectAt(Point pos){
        if (!inBounds(pos)) return null;
        return objects[pos.y][pos.x];
    }

    public void setObjectAt(Point pos, WorldObject obj){
        if (!inBounds(pos)) return;
        objects[pos.y][pos.x] = obj;
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