package pixelj.models;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class FilteredListTests {

    private static final IdInt ONE = new IdInt(1);
    private static final IdInt TWO = new IdInt(2);
    private static final IdInt THREE = new IdInt(3);
    private static final IdInt FOUR = new IdInt(4);
    private static final IdInt FIVE = new IdInt(5);
    private static final IdInt SIX = new IdInt(6);
    private static final IdInt SEVEN = new IdInt(7);
    private static final IdInt EIGHT = new IdInt(8);

    /** Add multiple elements at once. */
    @Test
    public void addMany() {
        final var model = new SortedList<>(List.of(TWO, FOUR, SIX, EIGHT));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i.between(THREE, SEVEN));
        wrapper.addAll(List.of(ONE, FIVE));
        assertArrayEquals(new IdInt[] {FOUR, FIVE, SIX}, wrapper.display.toArray());
    }

    /** Remove one element. */
    @Test
    public void removeElement() {
        final var model = new SortedList<>(List.of(TWO, FOUR, SIX, EIGHT));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i.between(THREE, SEVEN));
        model.remove(FOUR);
        assertArrayEquals(new IdInt[] {SIX}, wrapper.display.toArray());
    }

    /** Remove a visible element using its index. */
    @Test
    public void removeInside() {
        final var model = new SortedList<>(List.of(TWO, FOUR, SIX, EIGHT));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i.between(THREE, SEVEN));
        model.removeElementAt(1);
        assertArrayEquals(new IdInt[] {SIX}, wrapper.display.toArray());
    }

    /** Remove multiple elements at once. */
    @Test
    public void removeMany() {
        final var model = new SortedList<>(List.of(TWO, FOUR, SIX, EIGHT));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i.between(THREE, SEVEN));
        model.removeAll(List.of(ONE, FOUR, FIVE));
        assertArrayEquals(new IdInt[] {SIX}, wrapper.display.toArray());
    }

    /** Try removing a value that is not in the list. */
    @Test
    public void removeNonElement() {
        final var model = new SortedList<>(List.of(TWO, FOUR, SIX, EIGHT));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i.between(THREE, SEVEN));
        model.remove(FIVE);
        assertArrayEquals(new IdInt[] {FOUR, SIX}, wrapper.display.toArray());
    }

    /** Remove an element from the wrapped list that isn't visible in the wrapper. */
    @Test
    public void removeOutside() {
        final var model = new SortedList<>(List.of(TWO, FOUR, SIX, EIGHT));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i.between(THREE, SEVEN));
        model.removeElementAt(0);
        assertArrayEquals(new IdInt[] {FOUR, SIX}, wrapper.display.toArray());
    }

    /** Change filter. */
    @Test
    public void setFilter() {
        final var model = new SortedList<>(List.of(TWO, FOUR, SIX, EIGHT));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i.between(THREE, SEVEN));
        model.add(FIVE);
        assertArrayEquals(new IdInt[] {FOUR, FIVE, SIX}, wrapper.display.toArray());
        wrapper.setFilter(i -> i.between(FIVE, EIGHT));
        assertArrayEquals(new IdInt[] {SIX}, wrapper.display.toArray());
    }
}
