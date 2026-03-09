
public class Main {
    public static void main(String[] args) {
        //test line to show commit

        SimulationModel model = new SimulationModel();
        model.initializeGrid(30, 50);
        
        SimulationGUI gui = new SimulationGUI(model);
        gui.display(); 
    } 
}