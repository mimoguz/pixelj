package io.github.mimoguz.pixelj.models;

import java.util.List;

import io.github.mimoguz.pixelj.graphics.BinaryImage;

public class ExampleData {
    private ExampleData() {
    }

    public static ProjectModel createProject() {
        final var metrics = Metrics.Builder.getDefault().build();

        final var characters = List.of(
                new CharacterModel(
                        65,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterModel(
                        66,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterModel(
                        67,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterModel(
                        68,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterModel(
                        69,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterModel(
                        70,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterModel(
                        71,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterModel(
                        72,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterModel(
                        73,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterModel(
                        74,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterModel(
                        75,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterModel(
                        76,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterModel(
                        77,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterModel(
                        78,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterModel(
                        79,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                ),
                new CharacterModel(
                        80,
                        metrics.defaultCharacterWidth(),
                        BinaryImage.of(metrics.canvasWidth(), metrics.canvasHeight(), true)
                )
        );

        final var kerningPairs = List.of(
                new KerningPairModel(characters.get(0), characters.get(1), 0),
                new KerningPairModel(characters.get(1), characters.get(2), 0),
                new KerningPairModel(characters.get(2), characters.get(3), 0),
                new KerningPairModel(characters.get(3), characters.get(4), 0),
                new KerningPairModel(characters.get(4), characters.get(5), 0),
                new KerningPairModel(characters.get(5), characters.get(6), 0)
        );

        return new ProjectModel(
                "Example project",
                new HashListModel<>(characters),
                new HashListModel<>(kerningPairs),
                metrics
        );
    }
}
