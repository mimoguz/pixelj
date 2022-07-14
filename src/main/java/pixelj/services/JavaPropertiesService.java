package pixelj.services;

import static pixelj.services.Queries.PIXELJ;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/** Save/set the application state using the Java Properties API. */
public final class JavaPropertiesService implements StatePersistanceService {

    private static final String PREVIEW_TEXT = "previewText";
    private static final String DARK_THEME = "darkTheme";
    private static final String EXPORT_IMAGE_WIDTH = "exportImageWidth";
    private static final String EXPORT_IMAGE_HEIGHT = "exportImageHeight";
    private static final String RECENT_ITEMS = "recentProjects";

    @Override
    public void save(final AppState state) throws IOException {
        final var properties = new Properties();
        properties.setProperty(PREVIEW_TEXT, state.getPreviewText());
        properties.setProperty(DARK_THEME, Boolean.toString(state.isDarkTheme()));
        properties.setProperty(EXPORT_IMAGE_WIDTH, Integer.toString(state.getExportImageWidth()));
        properties.setProperty(EXPORT_IMAGE_HEIGHT, Integer.toString(state.getExportImageHeight()));
        saveRecentItems(properties, state.getRecentItems());
        try (var outStream = Files.newOutputStream(getPath(), StandardOpenOption.CREATE_NEW)) {
            properties.storeToXML(outStream, "Pixelj application state");
        }
    }

    @Override
    public void set(final AppState state) throws IOException {
        final var properties = new Properties();
        try (var inStream = Files.newInputStream(getPath(), StandardOpenOption.READ)) {
            properties.loadFromXML(inStream);
        }
        state.setPreviewText(properties.getProperty(PREVIEW_TEXT, null));
        state.setDarkTheme(getBoolean(properties, DARK_THEME, false));
        state.setExportImageWidth(
                getInt(properties, EXPORT_IMAGE_WIDTH, AppState.DEFAULT_EXPORT_IMAGE_SIZE)
        );
        state.setExportImageHeight(
                getInt(properties, EXPORT_IMAGE_HEIGHT, AppState.DEFAULT_EXPORT_IMAGE_SIZE)
        );
        state.setRecentItems(readRecentItems(properties));
    }

    private static Collection<RecentItem> readRecentItems(final Properties properties) {
        try {
            final var source = properties.getProperty(RECENT_ITEMS, "[]");
            return parseRecentItems(source);
        } catch (JsonProcessingException e) {
            return List.of();
        }
    }

    private static void saveRecentItems(final Properties properties, final List<RecentItem> recentItems) {
        final var objectMapper = new ObjectMapper();
        try {
            final var json = objectMapper.writeValueAsString(recentItems);
            properties.setProperty(RECENT_ITEMS, json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private static int getInt(final Properties properties, final String key, final int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key, Integer.toString(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static boolean getBoolean(
            final Properties properties,
            final String key,
            final Boolean defaultValue
    ) {
        return Boolean.parseBoolean(properties.getProperty(key, Boolean.toString(defaultValue)))
                || defaultValue;
    }

    private static Path getPath() {
        return Paths.get(System.getProperty("user.home"), PIXELJ + ".properties");
    }

    private static Collection<RecentItem> parseRecentItems(final String source)
            throws JsonProcessingException {
        final var objectMapper = new ObjectMapper();
        return Collections.unmodifiableCollection(
            objectMapper.readValue(source, new TypeReference<Collection<RecentItem>>() { })
        );
    }
}
