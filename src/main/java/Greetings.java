package main.java;

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
        MESSAGES.add("Hello - Harrison B.");
    }

    /** Returns an unmodifiable view of the greetings list. */
    public static List<String> all() {
        return Collections.unmodifiableList(MESSAGES);
    }
}
