package pixelj.services;

import static pixelj.services.Queries.PIXELJ;

import java.io.File;
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

import pixelj.util.FileSystemUtilities;
import pixelj.util.FileSystemUtilities.OS;
import pixelj.util.FileSystemUtilities.OSPair;

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
        final var previewText = state.getPreviewText() == null ? "" : state.getPreviewText();
        properties.setProperty(PREVIEW_TEXT, previewText);
        properties.setProperty(DARK_THEME, Boolean.toString(state.isDarkTheme()));
        properties.setProperty(EXPORT_IMAGE_WIDTH, Integer.toString(state.getExportImageWidth()));
        properties.setProperty(EXPORT_IMAGE_HEIGHT, Integer.toString(state.getExportImageHeight()));
        saveRecentItems(properties, state.getRecentItems());
        saveXML(properties);
    }

    @Override
    public void set(final AppState state) throws IOException {
        final var properties = new Properties();
        final var osPair = getPath();
        try (var inStream = Files.newInputStream(osPair.value(), StandardOpenOption.READ)) {
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

    /** Read and parse JSON. */
    private static Collection<RecentItem> readRecentItems(final Properties properties) {
        try {
            final var source = properties.getProperty(RECENT_ITEMS, "[]");
            return parseRecentItems(source);
        } catch (JsonProcessingException e) {
            return List.of();
        }
    }

    /** Deserialize to JSON and write. */
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
        return Boolean.parseBoolean(properties.getProperty(key)) || defaultValue;
    }

    private static OSPair<Path> getPath() throws IOException {
        final var osPair = FileSystemUtilities.getConfigDirectory();
        return new OSPair<>(
                osPair.os(),
                Paths.get(osPair.value().toAbsolutePath().toString(), PIXELJ + ".properties")
        );
    }

    /**
     * This is only public because I want to test it.
     *
     * @param source Serialized JSON
     * @return Collection of deserialized recent items.
     * @throws JsonProcessingException
     */
    public static Collection<RecentItem> parseRecentItems(final String source)
            throws JsonProcessingException {
        final var objectMapper = new ObjectMapper();
        return Collections.unmodifiableCollection(
            objectMapper.readValue(source, new TypeReference<Collection<RecentItem>>() { })
        );
    }

    private static void saveXML(final Properties properties) throws IOException {
        final var osPair = getPath();
        if (osPair.os() == OS.LINUX) {
            // .local/share may not exist
            final var dir = new File(osPair.value().getParent().toAbsolutePath().toString());
            if (!dir.exists()) {
                try {
                    dir.mkdirs();
                } catch (SecurityException e) {
                    throw new IOException(e);
                }
            }
        }
        try (var outStream = Files.newOutputStream(osPair.value())) {
            properties.storeToXML(outStream, "Pixelj application state");
        }
    }
}
