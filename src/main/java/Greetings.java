import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Holds a list of greetings supplied by the class. */
public final class Greetings {

    private static final List<String> MESSAGES = new ArrayList<>();
    static {
        MESSAGES.add("Hello from the teacher!");
        // students add exactly one line each:
        // MESSAGES.add("Hola — Alice P.");
        MESSAGES.add("Hello-DarionP.");

        MESSAGES.add("Human vs. Zombie Survival Simulation is an infection and survival simulation in grid-based. ");
        MESSAGES.add("The simulation takes place in a two-dimensional world divided into cells with integer coordinates, similar to the Critter model. Each cell may contain an entity. ");

        MESSAGES.add("Humans move randomly in the grid and exist in different states, healthy or infected. ");
        MESSAGES.add("When an infected human encounters a healthy human, there is a probability that the infection will spread. ");
        MESSAGES.add("If an infection persists for a certain amount of time, the human may transform into a zombie.");

        MESSAGES.add("Zombies actively move through the grid and attempt to infect or attack nearby humans. ");
        MESSAGES.add("Some specialized humans, such as doctors, are able to heal infected individuals, reduce the spread of infection. ");
        MESSAGES.add("Different types of humans and zombies exhibit different behaviors, allowing the simulation to demonstrate how variation among agents affects overall survival. ");
        
        MESSAGES.add("The simulation runs as an animated graphical user interface that updates over time ");
        MESSAGES.add("Users can control key parameters such as population size. ");
        MESSAGES.add("The simulation ends when either no zombies remain or when all humans have become infected or transformed. ");
        
    }

    /** Returns an unmodifiable view of the greetings list. */
    public static List<String> all() {
        return Collections.unmodifiableList(MESSAGES);
    }
}
