package pixelj.models;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class SortedListTests {

    private static final IdInt ONE = new IdInt(1);
    private static final IdInt TWO = new IdInt(2);
    private static final IdInt THREE = new IdInt(3);
    private static final IdInt FOUR = new IdInt(4);
    private static final IdInt SIX = new IdInt(6);
    private static final IdInt EIGHT = new IdInt(8);
    private static final IdInt TEN = new IdInt(10);

    /** Add multiple elements at once. */
    @Test
    public void addMany() {
        final var model = new SortedList<>(List.of(TWO, FOUR, SIX, EIGHT));
        model.addAll(List.of(ONE, THREE));
        assertArrayEquals(new IdInt[] {ONE, TWO, THREE, FOUR, SIX, EIGHT}, model.display.toArray());
    }

    /** Add one element. */
    @Test
    public void addOne() {
        final var model = new SortedList<>(List.of(TWO, FOUR, SIX, EIGHT));
        model.add(THREE);
        assertArrayEquals(new IdInt[] {TWO, THREE, FOUR, SIX, EIGHT}, model.display.toArray());
    }

    /** Remove by index. */
    @Test
    public void removeAt() {
        final var model = new SortedList<>(List.of(TWO, FOUR, SIX, EIGHT));
        model.removeElementAt(2);
        assertArrayEquals(new IdInt[] {TWO, FOUR, EIGHT}, model.display.toArray());
    }

    /** Remove multiple elements using an interval. */
    @Test
    public void removeInterval() {
        final var model = new SortedList<>(List.of(TWO, FOUR, SIX, EIGHT, TEN));
        model.removeInterval(1, 3);
        assertArrayEquals(new IdInt[] {TWO, TEN}, model.display.toArray());
    }

    /** Remove multiple elements. */
    @Test
    public void removeMany() {
        final var model = new SortedList<>(List.of(TWO, FOUR, SIX, EIGHT));
        model.removeAll(List.of(TWO, SIX));
        assertArrayEquals(new IdInt[] {FOUR, EIGHT}, model.display.toArray());
    }

    /** Remove one element. */
    @Test
    public void removeOne() {
        final var model = new SortedList<>(List.of(TWO, FOUR, SIX, EIGHT));
        model.remove(FOUR);
        assertArrayEquals(new IdInt[] {TWO, SIX, EIGHT}, model.display.toArray());
    }
}
