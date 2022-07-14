package pixelj.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import pixelj.graphics.BinaryImage;

public class KerningPairTests {
    private final Glyph capitalA = new Glyph(65, 10, BinaryImage.of(16, 16));
    private final Glyph capitalA2 = new Glyph(65, 11, BinaryImage.of(16, 16));
    private final Glyph capitalB = new Glyph(66, 9, BinaryImage.of(16, 16));
    private final Glyph capitalC = new Glyph(67, 8, BinaryImage.of(16, 16));

    private final KerningPair pairA2B = new KerningPair(capitalA2, capitalB, 0);
    private final KerningPair pairAB = new KerningPair(capitalA, capitalB, 0);
    private final KerningPair pairAC = new KerningPair(capitalA, capitalC, 0);
    private final KerningPair pairBA = new KerningPair(capitalB, capitalA, 0);
    private final KerningPair pairBB = new KerningPair(capitalB, capitalB, 0);

    /** Value equality. */
    @Test
    public void isABEqualA2B() {
        assertEquals(pairAB, pairA2B);
    }

    /** Ordering. */
    @Test
    public void isABLessThanBA() {
        assertEquals(pairAB.compareTo(pairBA), -1);
    }

    /** Ordering. */
    @Test
    public void isBBGreaterThanAC() {
        assertEquals(pairBB.compareTo(pairAC), 1);
    }
}
