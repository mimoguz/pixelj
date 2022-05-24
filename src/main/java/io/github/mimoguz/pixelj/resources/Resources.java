package io.github.mimoguz.pixelj.resources;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.eclipse.collections.api.map.primitive.ImmutableIntObjectMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

import io.github.mimoguz.pixelj.graphics.FontIcon;
import io.github.mimoguz.pixelj.models.BlockData;
import io.github.mimoguz.pixelj.models.CharacterData;

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

    public final Collection<BlockData> blockList;
    public final ImmutableIntObjectMap<BlockData> blockMap;
    public final ImmutableIntObjectMap<CharacterData> characterMap;
    public final ImmutableIntObjectMap<Collection<CharacterData>> charactersInBlock;
    public final Colors colors;

    private final Font iconFont;
    private final Strings strings;

    private Resources(final boolean useDarkTheme) {
        final var blocks = new ArrayList<BlockData>();
        blocks.add(new BlockData(0, "All", 0, Integer.MAX_VALUE));
        blocks.addAll(loadBlocks());
        blockList = Collections.unmodifiableCollection(blocks);

        final var blockHashMap = new IntObjectHashMap<BlockData>(blocks.size());
        for (var block : blocks) {
            blockHashMap.put(block.id(), block);
        }
        blockMap = blockHashMap.toImmutable();

        final Collection<CharacterData> characterList = loadCharacters();
        final var characters = new IntObjectHashMap<CharacterData>(characterList.size());
        for (var character : characterList) {
            characters.put(character.codePoint(), character);
        }
        characterMap = characters.toImmutable();

        final var blockChars = new IntObjectHashMap<Collection<CharacterData>>();
        characterList.stream()
                .collect(Collectors.groupingBy(CharacterData::blockId))
                .forEach((key, value) -> blockChars.put(key, Collections.unmodifiableCollection(value)));
        charactersInBlock = blockChars.toImmutable();

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

    private static <T> Collection<T> loadCollection(String resource, TypeReference<Collection<T>> typeRef) {
        final var objectMapper = new ObjectMapper();
        try {
            return Collections.unmodifiableCollection(
                    objectMapper.readValue(Resources.class.getResourceAsStream(resource), typeRef)
            );
        } catch (final IOException e) {
            throw new ResourceInitializationException("Can't read " + resource + "\n" + e.getMessage());
        }
    }

    private static Collection<BlockData> loadBlocks() {
        return loadCollection("blocks.json", new TypeReference<Collection<BlockData>>() {
        });
    }

    private static Collection<CharacterData> loadCharacters() {
        return loadCollection("characterData.json", new TypeReference<Collection<CharacterData>>() {
        });
    }

    private static Font loadFont() {
        try (final var stream = Resources.class.getResourceAsStream("pxf16.otf")) {
            if (stream == null) {
                throw new ResourceInitializationException("The resource pxf16.otf is not found.");
            }
            return loadFont(stream);
        } catch (final IOException e) {
            throw new ResourceInitializationException("Can't read the font file:\n" + e.getMessage());
        }
    }

    private static Font loadFont(final InputStream stream) {
        try {
            final var font = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(Font.PLAIN, 16);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (final IOException | FontFormatException e) {
            throw new ResourceInitializationException("Can't read the font file: \n" + e.getMessage());
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
}
