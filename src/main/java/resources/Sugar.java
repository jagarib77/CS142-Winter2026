package resources;// resources.Sugar.java
// A resource that provides energy when eaten.
// Group Project: sim.Ant Colony Simulator
// Authors: Harrison Butler

/**
 * resources.Sugar resource. Carryable and edible, restoring a fixed amount of energy.
 */
public class Sugar extends Resource {
    private int energyValue;
    private int foodCount;

    public Sugar(int energyValue, int foodCount){
        this.energyValue = energyValue;
        this.foodCount = foodCount;
    }

    public Sugar(){
        energyValue = 50;
        foodCount = 1;
    }

    public void takeFood() {
        if (foodCount<=0) return;
        --foodCount;
    }

    public boolean isEmptied(){
        return foodCount<=0;
    }

    public boolean isDepleted() {
        return foodCount<=0;
    }

    public int getFoodCount() {
        return foodCount;
    }

    @Override
    public boolean isCarryable() { return true; }

    @Override
    public boolean isEdible() { return true; }

    @Override
    public char getSymbol() { return 'S'; }

    @Override
    public int energyValue() { return energyValue; }
}