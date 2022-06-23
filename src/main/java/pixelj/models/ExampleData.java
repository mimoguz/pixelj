package pixelj.models;

import pixelj.graphics.BinaryImage;

import java.util.List;

public class ExampleData {
    private ExampleData() {
    }

    public static Project createProject() {
        final var metrics = Metrics.getDefault();

        final var glyphs = List.of(
                new Glyph(
                        65,
                        metrics.defaultWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new Glyph(
                        66,
                        metrics.defaultWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new Glyph(
                        67,
                        metrics.defaultWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new Glyph(
                        68,
                        metrics.defaultWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new Glyph(
                        69,
                        metrics.defaultWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new Glyph(
                        70,
                        metrics.defaultWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new Glyph(
                        71,
                        metrics.defaultWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new Glyph(
                        72,
                        metrics.defaultWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new Glyph(
                        73,
                        metrics.defaultWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new Glyph(
                        74,
                        metrics.defaultWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new Glyph(
                        75,
                        metrics.defaultWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new Glyph(
                        76,
                        metrics.defaultWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new Glyph(
                        77,
                        metrics.defaultWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new Glyph(
                        78,
                        metrics.defaultWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new Glyph(
                        79,
                        metrics.defaultWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new Glyph(
                        80,
                        metrics.defaultWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
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
                metrics,
                null
        );
    }
}
