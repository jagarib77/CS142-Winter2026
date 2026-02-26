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

    // TODO: add logic for ants to expand the nest 
    public void digTunnel(){
        
    }

    //TODO: add logic for hunting for food and bringing it back to the foodStore
    public void findFood(){

    }
}
