package editor;

import resources.Food;
import sim.AntSim;
import sim.WorldGrid;
import terrain.Air;
import terrain.Dirt;
import terrain.Rock;
import terrain.Tunnel;
import util.Point;

public class EditorController {
    private final AntSim sim;
    private final EditorState state;

    public EditorController(AntSim sim, EditorState state) {
        this.sim = sim;
        this.state = state;
    }

    public boolean toggleEditing(boolean simPaused) {
        if (!simPaused) return false;
        state.setEditingEnabled(!state.isEditingEnabled());
        return true;
    }

    public void applyBrushAt(Point center) {
        if (!state.isEditingEnabled()) return;

        for (Point p : BrushMath.pointsInCircle(center, state.getBrushRadius())) {
            applySingle(p);
        }
    }

    private void applySingle(Point p) {
        switch (state.getBrushMode()) {
            case TERRAIN -> applyTerrain(p);
            case FOOD -> applyFood(p);
            case COLONY -> applyColony(p);
        }
    }

    private void applyTerrain(Point p) {
        WorldGrid world = sim.getWorld();
        switch (state.getTerrainKind()) {
            case DIRT -> world.setTerrain(p, new Dirt());
            case ROCK -> world.setTerrain(p, new Rock());
            case AIR -> world.setTerrain(p, new Air());
            case TUNNEL -> world.setTerrain(p, new Tunnel());
        }
    }

    private void applyFood(Point p) {
        WorldGrid world = sim.getWorld();
        world.setObjectAt(p, new Food(state.getFoodEnergy(), state.getFoodCount()));
    }

    private void applyColony(Point p) {
        // placeholder for later
    }
}