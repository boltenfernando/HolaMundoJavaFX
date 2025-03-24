package util;

import java.io.IOException;
import java.util.logging.*;

public class LoggerUtil {
    private static final Logger logger = Logger.getLogger("HolaMundoJavaFX");

    static {
        try {
            // Configurar un FileHandler para guardar los logs en un archivo
            Handler fileHandler = new FileHandler("app.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

            // Configurar un ConsoleHandler para ver logs en la consola
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);

            // Establecer nivel de log
            logger.setLevel(Level.ALL);
            fileHandler.setLevel(Level.ALL);
            consoleHandler.setLevel(Level.ALL);
        } catch (IOException e) {
            System.err.println("No se pudo configurar el logging: " + e.getMessage());
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}
