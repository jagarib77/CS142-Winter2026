package ZombieSim;

import java.util.Scanner;

public class MainApp {

    static void main(String[] args) {
        run();
    }
    private static void run(){
        new SimGUI();
        int size = getSize();
//        int human = getHuman();
//        SimModel model = new SimModel(size, human, ...) implement this
    }

    private static int getSize(){
        Scanner input = new Scanner(System.in);
        System.out.println("Map Size: ");
        return input.nextInt();
    }

}
