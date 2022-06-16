package pixelj.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import pixelj.graphics.BinaryImage;

public class CharacterItemTests {
    private final Glyph capitalA = new Glyph(65, 10, BinaryImage.of(16, 16));
    private final Glyph capitalA2 = new Glyph(65, 11, BinaryImage.of(16, 16));
    private final Glyph capitalB = new Glyph(66, 9, BinaryImage.of(16, 16));
    private final Glyph capitalC = new Glyph(67, 8, BinaryImage.of(16, 16));

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
