package pixelj.models;

import pixelj.graphics.BinaryImage;

import java.util.List;

public class ExampleData {
    private ExampleData() {
    }

    public static Project createProject() {
        final var metrics = Metrics.getDefault();

        final var characters = List.of(
                new CharacterItem(
                        65,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterItem(
                        66,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterItem(
                        67,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterItem(
                        68,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterItem(
                        69,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterItem(
                        70,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterItem(
                        71,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterItem(
                        72,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterItem(
                        73,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterItem(
                        74,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterItem(
                        75,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterItem(
                        76,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterItem(
                        77,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterItem(
                        78,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterItem(
                        79,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterItem(
                        80,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                )
        );

        final var kerningPairs = List.of(
                new KerningPair(characters.get(0), characters.get(1), 0),
                new KerningPair(characters.get(1), characters.get(2), 0),
                new KerningPair(characters.get(2), characters.get(3), 0),
                new KerningPair(characters.get(3), characters.get(4), 0),
                new KerningPair(characters.get(4), characters.get(5), 0),
                new KerningPair(characters.get(5), characters.get(6), 0)
        );

        return new Project(
                "Example project",
                new SortedList<>(characters),
                new SortedList<>(kerningPairs),
                metrics
        );
    }
}
