import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class GreetingsTest {

    @Test
    void atLeastOneGreeting() {
        assertFalse(Greetings.all().isEmpty(),
            "The class should have at least one greeting!");
    }
}
