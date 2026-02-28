package ZombieSim;
import ZombieSim.Entities.*;

import java.awt.*;
import static ZombieSim.Direction.*;

public class MiniSim {
    static void main(String[] args) {
        SimModel model = new SimModel(5, 5, 1, 0, 0);
        model.printMap();
    }
}
