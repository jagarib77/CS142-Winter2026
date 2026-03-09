package editor;

import resources.Food;
import resources.Sugar;
import sim.AntSim;
import sim.WorldGrid;
import terrain.*;
import util.Point;

// enums
import static editor.TerrainKind.*;
import static editor.FoodKind.*;

public class EditorController {
    private final AntSim sim;
    private final EditorState state;

    public EditorController(AntSim sim, EditorState state) {
        this.sim = sim;
        this.state = state;
    }

    public boolean toggleEditing() {
        state.setEditingEnabled(!state.isEditingEnabled()); // toggles editing status
        return true;
    }

    public void applyBrushAt(Point center) {
        if (!state.isEditingEnabled()) return;

        for (Point p : BrushMath.pointsInCircle(sim, center, state.getBrushRadius())) {
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
        world.setObjectAt(p, new Sugar(state.getFoodEnergy(), state.getFoodCount()));
    }

    private void applyColony(Point p) {
        // placeholder for later
    }
}