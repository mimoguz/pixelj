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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import pixelj.util.FileSystemUtilities;
import pixelj.util.FileSystemUtilities.OS;
import pixelj.util.FileSystemUtilities.OSPair;
import pixelj.views.projectwindow.LayoutStrategy;

/** Save/set the application state using the Java Properties API. */
public final class JavaPropertiesService implements StatePersistanceService {

    private static final String PREVIEW_TEXT = "previewText";
    private static final String COLOR_THEME = "theme";
    private static final String ICON_THEME = "icons";
    private static final String EXPORT_IMAGE_WIDTH = "exportImageWidth";
    private static final String EXPORT_IMAGE_HEIGHT = "exportImageHeight";
    private static final String LAYOUT_STRATEGY = "layoutStrategy";
    private static final String RECENT_ITEMS = "recentProjects";

    @Override
    public void save(final AppState state) throws IOException {
        final var properties = new Properties();
        final var previewText = state.getPreviewText() == null ? "" : state.getPreviewText();
        properties.setProperty(PREVIEW_TEXT, previewText);
        properties.setProperty(COLOR_THEME, Integer.toString(state.getColorTheme().ordinal()));
        properties.setProperty(ICON_THEME, Integer.toString(state.getIconTheme().ordinal()));
        properties.setProperty(EXPORT_IMAGE_WIDTH, Integer.toString(state.getExportImageWidth()));
        properties.setProperty(EXPORT_IMAGE_HEIGHT, Integer.toString(state.getExportImageHeight()));
        properties.setProperty(LAYOUT_STRATEGY, Integer.toString(state.getLayoutStrategy().ordinal()));
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
        state.setExportImageWidth(
            getInt(properties, EXPORT_IMAGE_WIDTH, AppState.DEFAULT_EXPORT_IMAGE_SIZE)
        );
        state.setExportImageHeight(
            getInt(properties, EXPORT_IMAGE_HEIGHT, AppState.DEFAULT_EXPORT_IMAGE_SIZE)
        );
        state.setRecentItems(readRecentItems(properties));

        try {
            state.setLayoutStrategy(LayoutStrategy.values()[getInt(properties, LAYOUT_STRATEGY, 0)]);
        } catch (IndexOutOfBoundsException e) {
            state.setLayoutStrategy(LayoutStrategy.values()[0]);
        }

        try {
            state.setColorTheme(AppState.ColorTheme.values()[getInt(properties, COLOR_THEME, 0)]);
        } catch (IndexOutOfBoundsException e) {
            // Leave default
        }
        try {
            state.setIconTheme(AppState.IconTheme.values()[getInt(properties, ICON_THEME, 0)]);
        } catch (IndexOutOfBoundsException e) {
            // Leave default
        }
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
        final var objectMapper = getObjectMapper();
        return Collections.unmodifiableCollection(
            objectMapper.readValue(source, new TypeReference<Collection<RecentItem>>() { })
        );
    }
    
    public static ObjectMapper getObjectMapper() {
        final var objectMapper = new ObjectMapper();
        objectMapper
            .registerModule(new JavaTimeModule())
            .enable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
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
        final var objectMapper = getObjectMapper();
        try {
            final var json = objectMapper.writeValueAsString(recentItems);
            properties.setProperty(RECENT_ITEMS, json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private static int getInt(final Properties properties, final String key, final int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static OSPair<Path> getPath() throws IOException {
        final var osPair = FileSystemUtilities.getConfigDirectory();
        return new OSPair<>(
                osPair.os(),
                Paths.get(osPair.value().toAbsolutePath().toString(), PIXELJ + ".properties")
        );
    }

    private static void saveXML(final Properties properties) throws IOException {
        final var osPair = getPath();
        if (osPair.os() == OS.LINUX) {
            // .local/share may not exist
            final var dir = osPair.value().getParent().toFile();
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
