package io.github.mimoguz.pixelj.models;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class FilteredListModelTests {
    @Test
    public void addMany() {
        final var model = new IntObjectMapModel<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredListModel<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        wrapper.addAll(List.of(1, 5));
        assertArrayEquals(new Integer[] { 4, 5, 6 }, wrapper.display.toArray());
    }

    @Test
    public void addOne() {
        final var model = new IntObjectMapModel<>(List.of(2, 4, 6, 8));
        model.add(3);
        assertArrayEquals(new Integer[] { 2, 3, 4, 6, 8 }, model.display.toArray());
    }

    @Test
    public void removeAt() {
        final var model = new IntObjectMapModel<>(List.of(2, 4, 6, 8));
        model.removeElementAt(2);
        assertArrayEquals(new Integer[] { 2, 4, 8 }, model.display.toArray());
    }

    @Test
    public void removeInterval() {
        final var model = new IntObjectMapModel<>(List.of(2, 4, 6, 8, 10));
        model.removeInterval(1, 3);
        assertArrayEquals(new Integer[] { 2, 10 }, model.display.toArray());
    }

    @Test
    public void removeMany() {
        final var model = new IntObjectMapModel<>(List.of(2, 4, 6, 8));
        model.removeAll(List.of(2, 6));
        assertArrayEquals(new Integer[] { 4, 8 }, model.display.toArray());
    }

    @Test
    public void removeOne() {
        final var model = new IntObjectMapModel<>(List.of(2, 4, 6, 8));
        model.remove(4);
        assertArrayEquals(new Integer[] { 2, 6, 8 }, model.display.toArray());
    }
}
