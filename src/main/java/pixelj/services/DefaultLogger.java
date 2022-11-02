package pixelj.services;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultLogger extends Logger {

    private static final DefaultLogger INSTANCE = new DefaultLogger();

    private DefaultLogger() {
        super("pixelj", null);
    }

    public void logInfo(String msg) {
        this.log(Level.INFO, msg);
    }

    public void logWarning(String msg) {
        this.log(Level.WARNING, msg);
    }

    public void logError(String msg) {
        this.log(Level.SEVERE, msg);
    }

    public static DefaultLogger get() {
        return INSTANCE;
    }
}
