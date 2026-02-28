// GuardAnt.java

import java.util.Random;

public class GuardAnt extends ColonyAnt {
    public GuardAnt(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home){
        super(world, rng, pos, maxEnergy, home);
    }

    public static GuardAnt spawn(WorldGrid world, Random rng, Point pos, int maxEnergy, Point home){
        return new GuardAnt(world, rng, pos, maxEnergy, home);
    }

    @Override
    public char getSymbol() {
        return 'G';
    }

    public boolean attack(){
        //TODO: implement attack logic
        return true;
    }
}