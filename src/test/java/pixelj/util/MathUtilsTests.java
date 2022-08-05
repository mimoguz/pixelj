package pixelj.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import static pixelj.util.MathUtils.even;
import static pixelj.util.MathUtils.odd;

public class MathUtilsTests {

    /** Even is even, odd is odd. */
    @Test
    public void correct() {
        assertTrue(even(103298));
        assertTrue(odd(103297));
    }

    /** Even isn't odd, odd isn't even. */
    @Test
    public void incorrect() {
        assertFalse(odd(103298));
        assertFalse(even(103297));
    }
}
