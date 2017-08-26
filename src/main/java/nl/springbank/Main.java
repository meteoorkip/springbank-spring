package nl.springbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Main application of Spring. This file starts all other files.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */

@SpringBootApplication
public class Main {

    /**
     * Main class. Starts the application.
     *
     * @param args Arguments passed to Spring.
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
