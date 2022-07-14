package pixelj.models;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class FilteredListTests {

    /** Add multiple elements at once. */
    @Test
    public void addMany() {
        final var model = new SortedList<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        wrapper.addAll(List.of(1, 5));
        assertArrayEquals(new Integer[] {4, 5, 6}, wrapper.display.toArray());
    }

    /** Remove one element. */
    @Test
    public void removeElement() {
        final var model = new SortedList<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        model.remove(4);
        assertArrayEquals(new Integer[] {6}, wrapper.display.toArray());
    }

    /** Remove a visible element using its index. */
    @Test
    public void removeInside() {
        final var model = new SortedList<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        model.removeElementAt(1);
        assertArrayEquals(new Integer[] {6}, wrapper.display.toArray());
    }

    /** Remove multiple elements at once. */
    @Test
    public void removeMany() {
        final var model = new SortedList<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        model.removeAll(List.of(1, 4, 5));
        assertArrayEquals(new Integer[] {6}, wrapper.display.toArray());
    }

    /** Try removing a value that is not in the list. */
    @Test
    public void removeNonElement() {
        final var model = new SortedList<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        model.remove(5);
        assertArrayEquals(new Integer[] {4, 6}, wrapper.display.toArray());
    }

    /** Remove an element from the wrapped list that isn't visible in the wrapper. */
    @Test
    public void removeOutside() {
        final var model = new SortedList<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        model.removeElementAt(0);
        assertArrayEquals(new Integer[] {4, 6}, wrapper.display.toArray());
    }

    /** Change filter. */
    @Test
    public void setFilter() {
        final var model = new SortedList<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        model.add(5);
        assertArrayEquals(new Integer[] {4, 5, 6}, wrapper.display.toArray());
        wrapper.setFilter(i -> i > 5 && i < 8);
        assertArrayEquals(new Integer[] {6}, wrapper.display.toArray());
    }
}
