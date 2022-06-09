package io.github.mimoguz.pixelj.models;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class FilteredListTests {
    @Test
    public void addMany() {
        final var model = new SortedList<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        wrapper.addAll(List.of(1, 5));
        assertArrayEquals(new Integer[] { 4, 5, 6 }, wrapper.display.toArray());
    }

    @Test
    public void removeElement() {
        final var model = new SortedList<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        model.remove(4);
        assertArrayEquals(new Integer[] { 6 }, wrapper.display.toArray());
    }

    @Test
    public void removeInside() {
        final var model = new SortedList<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        model.removeElementAt(1);
        assertArrayEquals(new Integer[] { 6 }, wrapper.display.toArray());
    }

    @Test
    public void removeMany() {
        final var model = new SortedList<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        model.removeAll(List.of(1, 4, 5));
        System.out.println(wrapper.display.stream().map(Object::toString).collect(Collectors.joining(", ")));
        assertArrayEquals(new Integer[] { 6 }, wrapper.display.toArray());
    }

    @Test
    public void removeNonElement() {
        final var model = new SortedList<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        model.remove(5);
        assertArrayEquals(new Integer[] { 4, 6 }, wrapper.display.toArray());
    }

    @Test
    public void removeOutside() {
        final var model = new SortedList<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        model.removeElementAt(0);
        assertArrayEquals(new Integer[] { 4, 6 }, wrapper.display.toArray());
    }

    @Test
    public void setFilter() {
        final var model = new SortedList<>(List.of(2, 4, 6, 8));
        final var wrapper = new FilteredList<>(model);
        wrapper.setFilter(i -> i > 3 && i < 7);
        model.add(5);
        assertArrayEquals(new Integer[] { 4, 5, 6 }, wrapper.display.toArray());
        wrapper.setFilter(i -> i > 5 && i < 8);
        assertArrayEquals(new Integer[] { 6 }, wrapper.display.toArray());
    }
}
