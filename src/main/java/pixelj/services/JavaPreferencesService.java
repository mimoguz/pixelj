package pixelj.services;

import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import pixelj.Main;

/** Save/set the application state using the Java Preferences API. */
public final class JavaPreferencesService implements StatePersistanceService {

    private static final String PREVIEW_TEXT_PROPERTY = "previewText";
    private static final String DARK_THEME_PROPERTY = "darkTheme";
    private static final String EXPORT_IMAGE_WIDTH_PROPERTY = "exportImageWidth";
    private static final String EXPORT_IMAGE_HEIGHT_PROPERTY = "exportImageHeight";

    @Override
    public void save(final AppState state) throws IOException {
        try {
            final var preferences = Preferences.userNodeForPackage(Main.class);
            preferences.put(PREVIEW_TEXT_PROPERTY, state.getPreviewText());
            preferences.putBoolean(DARK_THEME_PROPERTY, state.isDarkTheme());
            preferences.putInt(EXPORT_IMAGE_WIDTH_PROPERTY, state.getExportImageWidth());
            preferences.putInt(EXPORT_IMAGE_HEIGHT_PROPERTY, state.getExportImageHeight());
            preferences.flush();
        } catch (SecurityException | IllegalStateException | BackingStoreException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void set(final AppState state) throws IOException {
        try {
            final var preferences = Preferences.userNodeForPackage(Main.class);
            state.setPreviewText(preferences.get(PREVIEW_TEXT_PROPERTY, null));
            state.setDarkTheme(preferences.getBoolean(DARK_THEME_PROPERTY, false));
            state.setExportImageWidth(
                    preferences.getInt(EXPORT_IMAGE_WIDTH_PROPERTY, AppState.DEFAULT_EXPORT_IMAGE_SIZE)
            );
            state.setExportImageHeight(
                    preferences.getInt(EXPORT_IMAGE_HEIGHT_PROPERTY, AppState.DEFAULT_EXPORT_IMAGE_SIZE)
            );
        } catch (SecurityException | IllegalStateException e) {
            throw new IOException(e);
        }
    }
}
