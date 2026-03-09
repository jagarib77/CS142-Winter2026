import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.Timer;

public class Main {
    public static void main(String[] args) {
        //test line to show commit

        SimulationModel model = new SimulationModel();
        model.initializeGrid(40, 60);
        
        SimulationGUI gui = new SimulationGUI(model);
        gui.display(); 
    }    
}