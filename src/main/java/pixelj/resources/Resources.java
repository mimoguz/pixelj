package pixelj.resources;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.ImageIcon;

import org.eclipse.collections.api.map.primitive.ImmutableIntObjectMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import pixelj.graphics.FontIcon;
import pixelj.models.BlockRecord;
import pixelj.models.ScalarRecord;

public final class Resources {

    private static final String BASE = "pixelj/resources/";
    private static Resources instance;

    /**
     * List of application icons for different sizes.
     */
    public final List<Image> applicationIcons;
    /**
     * Theme colors.
     */
    public final Colors colors;

    private final Collection<BlockRecord> blocks;
    private final ImmutableIntObjectMap<BlockRecord> blocksTable;
    private final ImmutableIntObjectMap<ScalarRecord> scalarsTable;
    private final ImmutableIntObjectMap<Collection<ScalarRecord>> scalarsInBlock;
    private final Font iconFont;
    private final Strings strings;

    private Resources(final boolean useDarkTheme) {
        final var blockList = new ArrayList<BlockRecord>();
        blockList.add(new BlockRecord(0, "All", 0, Integer.MAX_VALUE));
        blockList.addAll(loadBlocks());
        blocks = Collections.unmodifiableCollection(blockList);

        final var blockMap = new IntObjectHashMap<BlockRecord>(blockList.size());
        for (final var block : blockList) {
            blockMap.put(block.id(), block);
        }
        blocksTable = blockMap.toImmutable();

        final Collection<ScalarRecord> scalarRecords = loadScalarRecords();
        final var scalars = new IntObjectHashMap<ScalarRecord>(scalarRecords.size());
        for (final var scalar : scalarRecords) {
            scalars.put(scalar.codePoint(), scalar);
        }
        scalarsTable = scalars.toImmutable();

        final var scalarsByBlock = new IntObjectHashMap<Collection<ScalarRecord>>();
        scalarRecords.stream()
                .collect(Collectors.groupingBy(ScalarRecord::blockId))
                .forEach((key, value) -> scalarsByBlock.put(key, Collections.unmodifiableCollection(value)));
        scalarsInBlock = scalarsByBlock.toImmutable();

        iconFont = loadIconFont();
        strings = new Strings(loadResourceBundle());
        colors = useDarkTheme ? new DarkColors() : new LightColors();
        applicationIcons = loadApplicationIcons();

        registerUIFonts();
    }

    public String formatString(final String key, final Object... arguments) {
        return strings.format(key, arguments);
    }

    public BlockRecord getBlockData(final int blockId) {
        return blocksTable.get(blockId);
    }

    public Collection<BlockRecord> getBlocks() {
        return blocks;
    }

    public ScalarRecord getScalar(final int codePoint) {
        return scalarsTable.get(codePoint);
    }

    public Collection<ScalarRecord> getScalars(final int blockId) {
        return scalarsInBlock.get(blockId);
    }

    public FontIcon getIcon(final Icons icon) {
        return new FontIcon(icon.codePoint, null, null, iconFont);
    }

    public FontIcon getIcon(final Icons icon, final Color color, final Color disabledColor) {
        return new FontIcon(icon.codePoint, color, disabledColor, iconFont);
    }

    public Locale getLocale() {
        return strings.getLocale();
    }

    public String getString(final String key) {
        return strings.get(key);
    }

    public static Resources get() {
        if (instance == null) {
            initialize(true);
        }
        return instance;
    }

    public static void initialize(final boolean useDarkTheme) {
        instance = new Resources(useDarkTheme);
    }

    private static List<Image> loadApplicationIcons() {
        return Stream.of(16, 24, 48, 32, 64, 128, 256).map(size -> {
            try {
                return new ImageIcon(
                        Resources.class.getResource("applicationIcon/icon" + size + "px.png").getPath()
                ).getImage();
            } catch (final NullPointerException exception) {
                return null;
            }
        }).filter(Objects::nonNull).toList();
    }

    private static Collection<BlockRecord> loadBlocks() {
        return loadSerializedCollection("blocks.json", new TypeReference<>() {
            // Empty
        });
    }

    private static Collection<ScalarRecord> loadScalarRecords() {
        return loadSerializedCollection("scalars.json", new TypeReference<>() {
            // Empty
        });
    }

    private static Font loadIconFont() {
        try (final var stream = Resources.class.getResourceAsStream("fonts/pxf16.otf")) {
            return loadFont(stream, Font.PLAIN, 16);
        } catch (final IOException e) {
            throw new ResourceInitializationException(e);
        }
    }

    private static void registerUIFonts() {
        try (var stream = Resources.class.getResourceAsStream("fonts/NotoSans-Regular.ttf")) {
            loadFont(stream, Font.PLAIN, 13);
        } catch (IOException e) {
            throw new ResourceInitializationException(e);
        }
        try (var stream = Resources.class.getResourceAsStream("fonts/NotoSans-Bold.ttf")) {
            loadFont(stream, Font.BOLD, 13);
        } catch (IOException e) {
            throw new ResourceInitializationException(e);
        }
        try (var stream = Resources.class.getResourceAsStream("fonts/NotoSans-Italic.ttf")) {
            loadFont(stream, Font.ITALIC, 13);
        } catch (IOException e) {
            throw new ResourceInitializationException(e);
        }
        try (var stream = Resources.class.getResourceAsStream("fonts/NotoSans-BoldItalic.ttf")) {
            loadFont(stream, Font.BOLD | Font.ITALIC, 13);
        } catch (IOException e) {
            throw new ResourceInitializationException(e);
        }
    }

    private static Font loadFont(final InputStream stream, final int style, final int size) {
        try {
            final var font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(style, size);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (final IOException | FontFormatException e) {
            e.printStackTrace();
            throw new ResourceInitializationException(e);
        }
    }

    private static ResourceBundle loadResourceBundle() {
        final var bundleBase = "strings/strings";
        ResourceBundle bundle;
        try {
            bundle = ResourceBundle
                    .getBundle(BASE + bundleBase, Locale.getDefault(), Resources.class.getClassLoader());
        } catch (final MissingResourceException e1) {
            try {
                bundle = ResourceBundle
                        .getBundle(BASE + bundleBase, Locale.US, Resources.class.getClassLoader());
            } catch (final MissingResourceException e2) {
                throw new ResourceInitializationException(
                        "Can't find strings:\n" + e1.getMessage() + "\n\n--------------------------\n\n"
                                + e2.getMessage()
                );
            }
        }
        return bundle;
    }

    private static <T> Collection<T> loadSerializedCollection(
            final String resource,
            final TypeReference<Collection<T>> typeRef
    ) {
        final var objectMapper = new ObjectMapper();
        try (var source = Resources.class.getResourceAsStream(resource)) {
            return Collections.unmodifiableCollection(objectMapper.readValue(source, typeRef));
        } catch (final IOException e) {
            throw new ResourceInitializationException(e);
        }
    }

    public static class ResourceInitializationException extends RuntimeException {
        public ResourceInitializationException(final String message) {
            super(message);
        }

        public ResourceInitializationException(final Exception e) {
            super(e);
        }
    }
}
