package pixelj.services;

import java.io.IOException;

/** Save/set the application state. */
public interface StatePersistanceService {

    /**
     * Save current state.
     *
     * @param state Current application state
     * @throws IOException
     */
    void save(AppState state) throws IOException;

    /**
     * Load the last saved app state.
     *
     * @param state  State to set to the last saved values.
     * @throws IOException
     */
    void set(AppState state) throws IOException;
}
