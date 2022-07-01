package pixelj.models;

import pixelj.graphics.BinaryImage;

import java.util.List;

public class ExampleData {
    private ExampleData() {
    }

    public static Project createProject() {
        final var settings = DocumentSettings.getDefault();

        final var glyphs = List.of(
                new Glyph(
                        65,
                        settings.defaultWidth(),
                        BinaryImage.of(settings.canvasWidth(), settings.canvasHeight(), true)
                ),
                new Glyph(
                        66,
                        settings.defaultWidth(),
                        BinaryImage.of(settings.canvasWidth(), settings.canvasHeight(), true)
                ),
                new Glyph(
                        67,
                        settings.defaultWidth(),
                        BinaryImage.of(settings.canvasWidth(), settings.canvasHeight(), true)
                ),
                new Glyph(
                        68,
                        settings.defaultWidth(),
                        BinaryImage.of(settings.canvasWidth(), settings.canvasHeight(), true)
                ),
                new Glyph(
                        69,
                        settings.defaultWidth(),
                        BinaryImage.of(settings.canvasWidth(), settings.canvasHeight(), true)
                ),
                new Glyph(
                        70,
                        settings.defaultWidth(),
                        BinaryImage.of(settings.canvasWidth(), settings.canvasHeight(), true)
                ),
                new Glyph(
                        71,
                        settings.defaultWidth(),
                        BinaryImage.of(settings.canvasWidth(), settings.canvasHeight(), true)
                ),
                new Glyph(
                        72,
                        settings.defaultWidth(),
                        BinaryImage.of(settings.canvasWidth(), settings.canvasHeight(), true)
                ),
                new Glyph(
                        73,
                        settings.defaultWidth(),
                        BinaryImage.of(settings.canvasWidth(), settings.canvasHeight(), true)
                ),
                new Glyph(
                        74,
                        settings.defaultWidth(),
                        BinaryImage.of(settings.canvasWidth(), settings.canvasHeight(), true)
                ),
                new Glyph(
                        75,
                        settings.defaultWidth(),
                        BinaryImage.of(settings.canvasWidth(), settings.canvasHeight(), true)
                ),
                new Glyph(
                        76,
                        settings.defaultWidth(),
                        BinaryImage.of(settings.canvasWidth(), settings.canvasHeight(), true)
                ),
                new Glyph(
                        77,
                        settings.defaultWidth(),
                        BinaryImage.of(settings.canvasWidth(), settings.canvasHeight(), true)
                ),
                new Glyph(
                        78,
                        settings.defaultWidth(),
                        BinaryImage.of(settings.canvasWidth(), settings.canvasHeight(), true)
                ),
                new Glyph(
                        79,
                        settings.defaultWidth(),
                        BinaryImage.of(settings.canvasWidth(), settings.canvasHeight(), true)
                ),
                new Glyph(
                        80,
                        settings.defaultWidth(),
                        BinaryImage.of(settings.canvasWidth(), settings.canvasHeight(), true)
                )
        );

        final var kerningPairs = List.of(
                new KerningPair(glyphs.get(0), glyphs.get(1), 0),
                new KerningPair(glyphs.get(1), glyphs.get(2), 0),
                new KerningPair(glyphs.get(2), glyphs.get(3), 0),
                new KerningPair(glyphs.get(3), glyphs.get(4), 0),
                new KerningPair(glyphs.get(4), glyphs.get(5), 0),
                new KerningPair(glyphs.get(5), glyphs.get(6), 0)
        );

        return new Project(
                "Example project",
                new SortedList<>(glyphs),
                new SortedList<>(kerningPairs),
                settings,
                null
        );
    }
}
