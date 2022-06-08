package io.github.mimoguz.pixelj.models;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

public class FilteredListModelTests {
    @Test
    public void addMany() {
        final var model = new HashListModel<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredListModel<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        wrapper.addAll(List.of(1, 5));
        assertArrayEquals(new Integer[] { 4, 5, 6 }, wrapper.display.toArray());
    }

    @Test
    public void removeInside() {
        final var model = new HashListModel<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredListModel<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        model.removeElementAt(1);
//        assertArrayEquals(new Integer[] { 2, 6, 8 }, model.display.toArray());
        assertArrayEquals(new Integer[] { 6 }, wrapper.display.toArray());
    }

    @Test
    public void removeOutside() {
        final var model = new HashListModel<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredListModel<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        model.removeElementAt(0);
        assertArrayEquals(new Integer[] { 4, 6 }, wrapper.display.toArray());
    }

    @Test
    public void setFilter() {
        final var model = new HashListModel<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredListModel<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        model.add(5);
        assertArrayEquals(new Integer[] { 4, 5, 6 }, wrapper.display.toArray());
        wrapper.setFilter(i -> i > 5 && i < 7);
        assertArrayEquals(new Integer[] { 6 }, wrapper.display.toArray());
    }
}
