package pixelj.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import pixelj.graphics.BinaryImage;

public class KerningPairTests {
    private final CharacterItem capitalA = new CharacterItem(65, 10, BinaryImage.of(16, 16));
    private final CharacterItem capitalA2 = new CharacterItem(65, 11, BinaryImage.of(16, 16));
    private final CharacterItem capitalB = new CharacterItem(66, 9, BinaryImage.of(16, 16));
    private final CharacterItem capitalC = new CharacterItem(67, 8, BinaryImage.of(16, 16));

    private final KerningPair pairA2B = new KerningPair(capitalA2, capitalB, 0);
    private final KerningPair pairAB = new KerningPair(capitalA, capitalB, 0);
    private final KerningPair pairAC = new KerningPair(capitalA, capitalC, 0);
    private final KerningPair pairBA = new KerningPair(capitalB, capitalA, 0);
    private final KerningPair pairBB = new KerningPair(capitalB, capitalB, 0);

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
