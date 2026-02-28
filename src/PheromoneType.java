// PheromoneType.java

public enum PheromoneType {
    DANGER(0),
    WALKING_TRAIL(1),
    FOOD(2);

    public final int type;

    PheromoneType(int type){
        this.type = type;
    }
}