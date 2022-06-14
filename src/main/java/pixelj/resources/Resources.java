package pixelj.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pixelj.graphics.FontIcon;
import pixelj.models.BlockData;
import pixelj.models.CharacterData;
import org.eclipse.collections.api.map.primitive.ImmutableIntObjectMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class Resources {
    private static final String BASE = "pixelj/resources/";
    private static Resources instance = null;
    public final Colors colors;
    private final Collection<BlockData> blockList;
    private final ImmutableIntObjectMap<BlockData> blockMap;
    private final ImmutableIntObjectMap<CharacterData> characterMap;
    private final ImmutableIntObjectMap<Collection<CharacterData>> charactersInBlock;
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
        this.colors = useDarkTheme ? new DarkColors() : new LightColors();
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

    private static Collection<BlockData> loadBlocks() {
        return loadCollection("blocks.json", new TypeReference<>() {
            // Empty
        });
    }

    private static Collection<CharacterData> loadCharacters() {
        return loadCollection("characterData.json", new TypeReference<>() {
            // Empty
        });
    }

    private static <T> Collection<T> loadCollection(
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

    public static class ResourceInitializationException extends RuntimeException {
        public ResourceInitializationException(final String message) {
            super(message);
        }
    }
}
