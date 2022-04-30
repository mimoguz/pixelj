package io.github.mimoguz.pixelj.models;

import io.github.mimoguz.pixelj.graphics.BinaryImage;

import java.util.List;

public class ExampleData {
    private static final int CANVAS_WIDTH = 20;
    private static final int CANVAS_HEIGHT = 20;

    private static final List<CharacterModel> characters = List.of(
            new CharacterModel(65, 10, BinaryImage.of(CANVAS_WIDTH, CANVAS_HEIGHT, true)),
            new CharacterModel(66, 11, BinaryImage.of(CANVAS_WIDTH, CANVAS_HEIGHT, true)),
            new CharacterModel(67, 12, BinaryImage.of(CANVAS_WIDTH, CANVAS_HEIGHT, true)),
            new CharacterModel(68, 13, BinaryImage.of(CANVAS_WIDTH, CANVAS_HEIGHT, true)),
            new CharacterModel(69, 14, BinaryImage.of(CANVAS_WIDTH, CANVAS_HEIGHT, true)),
            new CharacterModel(70, 15, BinaryImage.of(CANVAS_WIDTH, CANVAS_HEIGHT, true)),
            new CharacterModel(71, 16, BinaryImage.of(CANVAS_WIDTH, CANVAS_HEIGHT, true)),
            new CharacterModel(72, 17, BinaryImage.of(CANVAS_WIDTH, CANVAS_HEIGHT, true)),
            new CharacterModel(73, 18, BinaryImage.of(CANVAS_WIDTH, CANVAS_HEIGHT, true)),
            new CharacterModel(74, 19, BinaryImage.of(CANVAS_WIDTH, CANVAS_HEIGHT, true)),
            new CharacterModel(75, 20, BinaryImage.of(CANVAS_WIDTH, CANVAS_HEIGHT, true)),
            new CharacterModel(76, 10, BinaryImage.of(CANVAS_WIDTH, CANVAS_HEIGHT, true)),
            new CharacterModel(77, 11, BinaryImage.of(CANVAS_WIDTH, CANVAS_HEIGHT, true)),
            new CharacterModel(78, 12, BinaryImage.of(CANVAS_WIDTH, CANVAS_HEIGHT, true)),
            new CharacterModel(79, 13, BinaryImage.of(CANVAS_WIDTH, CANVAS_HEIGHT, true)),
            new CharacterModel(80, 14, BinaryImage.of(CANVAS_WIDTH, CANVAS_HEIGHT, true))
    );

    private static final List<KerningPairModel> kerningPairs = List.of(
            new KerningPairModel(characters.get(0), characters.get(1), 0),
            new KerningPairModel(characters.get(1), characters.get(2), 0),
            new KerningPairModel(characters.get(2), characters.get(3), 0),
            new KerningPairModel(characters.get(3), characters.get(4), 0),
            new KerningPairModel(characters.get(4), characters.get(5), 0),
            new KerningPairModel(characters.get(5), characters.get(6), 0)
    );

    private static final Metrics metrics = new Metrics(CANVAS_WIDTH, CANVAS_HEIGHT, 12, 4, 6, 10, 1, false);

    public static final ProjectModel project = new ProjectModel(
            "Example project",
            new CharacterListModel(characters),
            new KerningPairListModel(kerningPairs),
            metrics
    );

    private ExampleData() {
    }
}
