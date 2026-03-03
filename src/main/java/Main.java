import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.Timer;

public class Main {
    public static void main(String[] args) {
        Greetings.all().forEach(System.out::println);
        //test line to show commit

        SimulationModel model = new SimulationModel();
        model.initializeGrid(20, 20);
        
        SimulationGUI gui = new SimulationGUI(model);
        gui.display(); 
    }    
}