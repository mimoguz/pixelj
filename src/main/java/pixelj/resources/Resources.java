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
import pixelj.models.BlockData;
import pixelj.models.CharacterData;

public class Resources {
    public static class ResourceInitializationException extends RuntimeException {
        public ResourceInitializationException(final String message) {
            super(message);
        }
    }

    private static final String BASE = "pixelj/resources/";

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

    private static Collection<BlockData> loadBlocks() {
        return loadSerializedCollection("blocks.json", new TypeReference<>() {
            // Empty
        });
    }

    private static Collection<CharacterData> loadCharacters() {
        return loadSerializedCollection("characterData.json", new TypeReference<>() {
            // Empty
        });
    }

    private static Font loadFont() {
        try (final var stream = Resources.class.getResourceAsStream("pxf16.otf")) {
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

    private static <T> Collection<T> loadSerializedCollection(
            final String resource,
            final TypeReference<Collection<T>> typeRef
    ) {
        final var objectMapper = new ObjectMapper();
        try (var source = Resources.class.getResourceAsStream(resource)) {
            return Collections.unmodifiableCollection(objectMapper.readValue(source, typeRef));
        } catch (final IOException e) {
            throw new ResourceInitializationException("Can't read " + resource + "\n" + e.getMessage());
        }
    }

    public final List<Image> applicationIcons;

    private final Collection<BlockData> blockList;

    private final ImmutableIntObjectMap<BlockData> blockMap;

    private final ImmutableIntObjectMap<CharacterData> characterMap;

    private final ImmutableIntObjectMap<Collection<CharacterData>> charactersInBlock;

    public final Colors colors;

    private final Font iconFont;

    private final Strings strings;

    private Resources(final boolean useDarkTheme) {
        final var blocks = new ArrayList<BlockData>();
        blocks.add(new BlockData(0, "All", 0, Integer.MAX_VALUE));
        blocks.addAll(loadBlocks());
        blockList = Collections.unmodifiableCollection(blocks);

        final var blockHashMap = new IntObjectHashMap<BlockData>(blocks.size());
        for (final var block : blocks) {
            blockHashMap.put(block.id(), block);
        }
        blockMap = blockHashMap.toImmutable();

        final Collection<CharacterData> characterList = loadCharacters();
        final var characters = new IntObjectHashMap<CharacterData>(characterList.size());
        for (final var character : characterList) {
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
        colors = useDarkTheme ? new DarkColors() : new LightColors();
        applicationIcons = loadApplicationIcons();
    }

    public String formatString(final String key, final Object... arguments) {
        return strings.format(key, arguments);
    }

    public BlockData getBlockData(final int blockId) {
        return blockMap.get(blockId);
    }

    public Collection<BlockData> getBlocks() {
        return blockList;
    }

    public CharacterData getCharacterData(final int codePoint) {
        return characterMap.get(codePoint);
    }

    public Collection<CharacterData> getCharacters(final int blockId) {
        return charactersInBlock.get(blockId);
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
}
