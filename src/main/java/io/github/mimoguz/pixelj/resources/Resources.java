package io.github.mimoguz.pixelj.resources;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.eclipse.collections.api.map.primitive.ImmutableIntObjectMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

import io.github.mimoguz.pixelj.graphics.FontIcon;
import io.github.mimoguz.pixelj.models.BlockModel;

public class Resources {
    public static class ResourceInitializationException extends RuntimeException {
        private static final long serialVersionUID = -3116843305868695218L;

        public ResourceInitializationException(final String message) {
            super(message);
        }
    }

    private static final String BASE = "io/github/mimoguz/pixelj/resources/";
    private static Resources instance = null;

    public static Resources get() {
        if (instance == null) {
            initialize(true);
        }
        return instance;
    }

    public static void initialize(final boolean useDarkTheme) {
        instance = new Resources(useDarkTheme);
    }

    public final Collection<BlockModel> blockList;
    public final ImmutableIntObjectMap<BlockModel> blockMap;
    public final Colors colors;

    private final Font iconFont;
    private final Strings strings;

    private Resources(final boolean useDarkTheme) {
        final var blocks = new ArrayList<BlockModel>();
        blocks.add(new BlockModel(0, "All", 0, Integer.MAX_VALUE));
        blocks.addAll(loadBlocks());
        blockList = Collections.unmodifiableCollection(blocks);

        final var blockHashMap = new IntObjectHashMap<BlockModel>();
        blocks.stream().forEach(block -> blockHashMap.put(block.id(), block));
        blockMap = blockHashMap.toImmutable();

        iconFont = loadFont();
        strings = new Strings(loadResourceBundle());
        this.colors = useDarkTheme ? new DarkColors() : new LightColors();
    }

    public String formatString(final String key, final Object... arguments) {
        return strings.format(key, arguments);
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

    private Collection<BlockModel> loadBlocks() {
        final var objectMapper = new ObjectMapper();
        try {
            final var blocks = objectMapper.readValue(
                    getClass().getResourceAsStream("blocks.json"),
                    new TypeReference<List<BlockModel>>() {
                    }
            );
            return Collections.unmodifiableCollection(blocks);
        } catch (final IOException e) {
            throw new ResourceInitializationException("Can't read blocks.json:\n" + e.getMessage());
        }
    }

    private Font loadFont() {
        try (final var stream = getClass().getResourceAsStream("pxf16.otf")) {
            if (stream == null) {
                throw new ResourceInitializationException("The resource pxf16.otf is not found.");
            }
            return loadFont(stream);
        } catch (final IOException e) {
            throw new ResourceInitializationException("Can't read the font file:\n" + e.getMessage());
        }
    }

    private Font loadFont(final InputStream stream) {
        try {
            final var font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(Font.PLAIN, 16);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (final IOException | FontFormatException e) {
            throw new ResourceInitializationException("Can't read the font file: \n" + e.getMessage());
        }
    }

    private ResourceBundle loadResourceBundle() {
        final var bundleBase = "strings/strings";
        ResourceBundle bundle;
        try {
            bundle = ResourceBundle
                    .getBundle(BASE + bundleBase, Locale.getDefault(), getClass().getClassLoader());
        } catch (final MissingResourceException e1) {
            try {
                bundle = ResourceBundle.getBundle(BASE + bundleBase, Locale.US, getClass().getClassLoader());
            } catch (final MissingResourceException e2) {
                throw new ResourceInitializationException(
                        "Can't find strings:\n" + e1.getMessage() + "\n\n--------------------------\n\n"
                                + e2.getMessage()
                );
            }
        }
        return bundle;
    }
}
