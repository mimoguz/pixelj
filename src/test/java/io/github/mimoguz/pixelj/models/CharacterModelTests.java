package io.github.mimoguz.pixelj.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.github.mimoguz.pixelj.graphics.BinaryImage;

public class CharacterModelTests {
    private final CharacterModel capitalA = new CharacterModel(65, 10, BinaryImage.of(16, 16));
    private final CharacterModel capitalA2 = new CharacterModel(65, 11, BinaryImage.of(16, 16));
    private final CharacterModel capitalB = new CharacterModel(66, 9, BinaryImage.of(16, 16));
    private final CharacterModel capitalC = new CharacterModel(67, 8, BinaryImage.of(16, 16));

    @Test
    public void capitalAEqualsCapitalA() {
        assertEquals(capitalA, capitalA);
    }

    @Test
    public void capitalAEqualsCapitalA2() {
        assertEquals(capitalA, capitalA2);
    }

    @Test
    public void capitalAIsLessThanCapitalB() {
        assertEquals(capitalA.compareTo(capitalB), -1);
    }

    @Test
    public void capitalCIsGreaterThanCapitalB() {
        assertEquals(capitalC.compareTo(capitalB), 1);
    }
}
