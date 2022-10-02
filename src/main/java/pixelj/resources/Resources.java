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
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;

import pixelj.graphics.FontIcon;
import pixelj.models.Block;
import pixelj.models.Scalar;
import pixelj.services.AppState;

public final class Resources {

    private static final String BASE = "pixelj/resources/";
    private static Resources instance;

    /** List of application icons for different sizes. */
    public final List<Image> applicationIcons;
    /** Theme colors. */
    public final Colors colors;

    private final Collection<Block> blocks;
    private final ImmutableIntObjectMap<Block> blocksTable;
    private final ImmutableIntObjectMap<Scalar> scalarsTable;
    private final ImmutableIntObjectMap<Collection<Scalar>> scalarsInBlock;
    private final Font iconFont;
    private final Strings strings;

    private Resources(final AppState.ColorTheme colorTheme, final AppState.IconTheme iconTheme) {
        final var blockList = new ArrayList<Block>();
        blockList.add(new Block(0, "All", 0, Integer.MAX_VALUE));
        blockList.addAll(loadBlocks());
        blocks = Collections.unmodifiableCollection(blockList);

        final var blockMap = new IntObjectHashMap<Block>(blockList.size());
        for (final var block : blockList) {
            blockMap.put(block.id(), block);
        }
        blocksTable = blockMap.toImmutable();

        final Collection<Scalar> scalarRecords = loadScalarRecords();
        final var scalars = new IntObjectHashMap<Scalar>(scalarRecords.size());
        for (final var scalar : scalarRecords) {
            scalars.put(scalar.codePoint(), scalar);
        }
        scalarsTable = scalars.toImmutable();

        final var scalarsByBlock = new IntObjectHashMap<Collection<Scalar>>();
        scalarRecords.stream()
            .collect(Collectors.groupingBy(Scalar::blockId))
            .forEach((key, value) -> scalarsByBlock.put(key, Collections.unmodifiableCollection(value)));
        scalarsInBlock = scalarsByBlock.toImmutable();

        iconFont = iconTheme == AppState.IconTheme.LINE ? registerPxf16Line() : registerPxf16();
        colors = colorTheme == AppState.ColorTheme.DARK ? new DarkColors() : new LightColors();
        strings = new Strings(loadResourceBundle());
        applicationIcons = loadApplicationIcons();

        registerUIFonts();
    }

    /**
     * @param key       Resource key
     * @param arguments Format arguments
     * @return Formatted string resource
     */
    public String formatString(final String key, final Object... arguments) {
        return strings.format(key, arguments);
    }

    public Block getBlockData(final int blockId) {
        return blocksTable.get(blockId);
    }

    public Collection<Block> getBlocks() {
        return blocks;
    }

    public Scalar getScalar(final int codePoint) {
        return scalarsTable.get(codePoint);
    }

    /**
     * @param codePoint
     * @return If the code point is a valid scalar
     */
    public boolean hasScalar(final int codePoint) {
        return scalarsTable.containsKey(codePoint);
    }

    public Collection<Scalar> getScalars(final int blockId) {
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

    /**
     * @return Resources instance
     */
    public static Resources get() {
        if (instance == null) {
            initialize(AppState.ColorTheme.LIGHT, AppState.IconTheme.LINE);
        }
        return instance;
    }

    /**
     * Initialize instance.
     * 
     * @param colorTheme
     * @param iconTheme
     */
    public static void initialize(final AppState.ColorTheme colorTheme, final AppState.IconTheme iconTheme) {
        instance = new Resources(colorTheme, iconTheme);
    }

    private static List<Image> loadApplicationIcons() {
        return Stream.of(16, 24, 32, 64, 128, 256).map(size -> {
            try {
                return new ImageIcon(
                    Resources.class.getResource("applicationIcon/icon" + size + "px.png").getPath()
                ).getImage();
            } catch (NullPointerException e) {
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).toList();
    }

    private static Collection<Block> loadBlocks() {
        return loadSerializedCollection("blocks.sml", new TypeReference<>() {
            // Empty
        });
    }

    private static Collection<Scalar> loadScalarRecords() {
        return loadSerializedCollection("scalars.sml", new TypeReference<>() {
            // Empty
        });
    }

    private static Font registerPxf16() {
        return registerIconFont("fonts/pxf16.otf");
    }
    
     private static Font registerPxf16Line() {
        return registerIconFont("fonts/pxf16_line.ttf");
    }
    
    private static Font registerIconFont(final String path) {
        try (var stream = Resources.class.getResourceAsStream(path)) {
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
        final var smileMapper = new SmileMapper();
        try (var source = Resources.class.getResourceAsStream(resource)) {
            return Collections.unmodifiableCollection(smileMapper.readValue(source, typeRef));
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
