package io.github.mimoguz.pixelj.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import io.github.mimoguz.pixelj.graphics.BinaryImage;

public class KerningModelTests {
    private final CharacterModel capitalA = new CharacterModel(65, 10, BinaryImage.of(16, 16));
    private final CharacterModel capitalA2 = new CharacterModel(65, 11, BinaryImage.of(16, 16));
    private final CharacterModel capitalB = new CharacterModel(66, 9, BinaryImage.of(16, 16));
    private final CharacterModel capitalC = new CharacterModel(67, 8, BinaryImage.of(16, 16));

    private final KerningPairModel pairA2B = new KerningPairModel(capitalA2, capitalB, 0);
    private final KerningPairModel pairAB = new KerningPairModel(capitalA, capitalB, 0);
    private final KerningPairModel pairAC = new KerningPairModel(capitalA, capitalC, 0);
    private final KerningPairModel pairBA = new KerningPairModel(capitalB, capitalA, 0);
    private final KerningPairModel pairBB = new KerningPairModel(capitalB, capitalB, 0);

    @Test
    public void ABEqualsA2B() {
        assertEquals(pairAB, pairA2B);
    }

    @Test
    public void ABIsLessThanBA() {
        assertEquals(pairAB.compareTo(pairBA), -1);
    }

    @Test
    public void BBIsGreaterThanAC() {
        assertEquals(pairBB.compareTo(pairAC), 1);
    }
}
