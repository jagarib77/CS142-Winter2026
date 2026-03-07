package editor;

import util.Point;

public class EditorState {
    private boolean editingEnabled;
    private BrushMode brushMode;      // TERRAIN, FOOD, COLONY
    private TerrainKind terrainKind;  // DIRT, ROCK, AIR, TUNNEL
    private FoodKind foodKind;        // FOOD, SUGAR, etc.
    private ColonyID colonyID;    // later if needed

    private int brushRadius;

    private int foodEnergy;
    private int foodCount;

    private Point hoveredTile;

    public TerrainKind getTerrainKind() { return terrainKind; }
    public void setTerrainKind(TerrainKind terrainKind) { this.terrainKind = terrainKind; }

    public FoodKind getFoodKind() { return foodKind; }
    public void setFoodKind(FoodKind foodKind) { this.foodKind = foodKind; }

    public ColonyID getColonyID() { return colonyID; }
    public void setColonyID(ColonyID colonyID) { this.colonyID = colonyID; }

    public boolean isEditingEnabled() { return editingEnabled; }
    public void setEditingEnabled(boolean editingEnabled) { this.editingEnabled = editingEnabled; }

    public BrushMode getBrushMode() { return brushMode; }
    public void setBrushMode(BrushMode brushMode) { this.brushMode = brushMode; }

    public int getBrushRadius() { return brushRadius; }
    public void setBrushRadius(int brushRadius) {
        this.brushRadius = Math.max(0, brushRadius);
    }

    public int getFoodEnergy() { return foodEnergy; }
    public void setFoodEnergy(int foodEnergy) { this.foodEnergy = Math.max(1, foodEnergy); }

    public int getFoodCount() { return foodCount; }
    public void setFoodCount(int foodCount) { this.foodCount = Math.max(1, foodCount); }

    public Point getHoveredTile() { return hoveredTile; }
    public void setHoveredTile(Point hoveredTile) { this.hoveredTile = hoveredTile; }
}