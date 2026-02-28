// WorkerAnt.java

import java.util.Random;

public class WorkerAnt extends ColonyAnt {
    public WorkerAnt(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home){
        super(world, rng, pos, maxEnergy, home);
    }

    public static WorkerAnt spawn(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home){
        return new WorkerAnt(world, rng, pos, maxEnergy, home);
    }

    @Override
    public char getSymbol() {
        return 'W';
    }
}