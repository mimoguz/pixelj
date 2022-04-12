package io.github.pixelj.models;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.*;
import java.util.function.Predicate;

/**
 * A list model that keeps its visible elements always filtered and sorted.
 * <p>
 * It's Intended to be used for CharacterModels and KerningPairModels, and uses a HashSet for backing collection
 * (so assumes no hash collisions).
 */
public class DisplayListModel<E extends Comparable<E>> implements ListModel<E> {
    private final ArrayList<E> display = new ArrayList<>();
    private final EventListenerList listeners = new EventListenerList();
    private final HashSet<E> source = new HashSet<>();
    private @NotNull Predicate<E> filter = t -> true;

    public DisplayListModel() {
    }

    public DisplayListModel(@NotNull Collection<E> elements) {
        final var collection = elements.stream().filter(Objects::nonNull).toList();
        source.addAll(collection);
        display.addAll(source.stream().filter(filter).sorted().toList());
    }

    public void add(@NotNull E element) {
        source.add(element);
        final var index = insertOrdered(element);
        if (index >= 0) fireIntervalAddedEvent(index, index);
    }

    public void addAll(@NotNull Collection<E> collection) {
        var index0 = -1;
        var index1 = -1;
        for (var element : collection) {
            if (element == null || source.contains(element)) continue;
            source.add(element);
            final var index = insertOrdered(element);
            index0 = index == -1 ? index0 : (index0 == -1 ? index : Math.min(index0, index));
            index1 = index == -1 ? index1 : (index1 == -1 ? index : Math.max(index1, index));
        }
        if (index0 >= 0) fireIntervalAddedEvent(index0, index1);
    }

    @Override
    public void addListDataListener(@NotNull ListDataListener l) {
        listeners.add(ListDataListener.class, l);
    }

    public void clear() {
        source.clear();
        if (!display.isEmpty()) {
            final var index1 = display.size() - 1;
            display.clear();
            fireIntervalRemovedEvent(0, index1);
        }
    }

    public int countWhere(@NotNull Predicate<E> predicate) {
        return (int) source.stream().filter(predicate).count();
    }

    public List<E> find(@NotNull Predicate<E> predicate) {
        return source.stream().filter(predicate).toList();
    }

    protected void fireContentsChangedEvent(int index0, int index1) {
        final var lst = listeners.getListeners(ListDataListener.class).clone();
        for (var index = lst.length - 1; index >= 0; index--) {
            lst[index].contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index0, index1));
        }
    }

    protected void fireIntervalAddedEvent(int index0, int index1) {
        final var lst = listeners.getListeners(ListDataListener.class);
        for (var index = lst.length - 1; index >= 0; index--) {
            lst[index].intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index0, index1));
        }
    }

    protected void fireIntervalRemovedEvent(int index0, int index1) {
        final var lst = listeners.getListeners(ListDataListener.class).clone();
        for (var index = lst.length - 1; index >= 0; index--) {
            lst[index].intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index0, index1));
        }
    }

    /**
     * @param index Index to visible list
     */
    @Override
    public E getElementAt(int index) {
        return null;
    }

    /**
     * @return Size of the visible list
     */
    @Override
    public int getSize() {
        return display.size();
    }

    /**
     * @return Size of the backing collection
     */
    public int getSourceSize() {
        return source.size();
    }

    public void remove(@NotNull E element) {
        source.remove(element);
        final var index = display.indexOf(element);
        if (display.remove(element)) {
            fireIntervalRemovedEvent(index, index);
        }
    }

    public void removeAll(@NotNull Collection<E> collection) {
        var index0 = -1;
        var index1 = -1;
        source.removeAll(collection);
        for (var element : collection) {
            if (element == null) continue;
            final var index = display.indexOf(element);
            index0 = index == -1 ? index0 : (index0 == -1 ? index : Math.min(index0, index));
            index1 = index == -1 ? index1 : (index1 == -1 ? index : Math.max(index1, index));
        }
        display.removeAll(collection);
        if (index0 >= 0) fireIntervalRemovedEvent(index0, index1);
    }

    /**
     * @param index Index to visible list
     */
    public void removeAt(int index) {
        final var element = display.get(index);
        source.remove(element);
        display.remove(index);
        fireIntervalRemovedEvent(index, index);
    }

    @Override
    public void removeListDataListener(@NotNull ListDataListener l) {
        listeners.remove(ListDataListener.class, l);
    }

    public void requestEvent(int index) {
        fireContentsChangedEvent(index, index);
    }

    public void setFilter(@NotNull Predicate<E> value) {
        filter = value;
        if (!display.isEmpty()) {
            final var index1 = display.size() - 1;
            display.clear();
            fireIntervalRemovedEvent(0, index1);
        }
        display.addAll(source.stream().filter(filter).sorted().toList());
        if (!display.isEmpty()) fireIntervalAddedEvent(0, display.size() - 1);
    }

    public boolean sourceContains(E element) {
        return source.contains(element);
    }

    private int findPlace(@NotNull E element) {
        return (int) display.stream().takeWhile(item -> item.compareTo(element) > 0).count();
    }

    private int insertOrdered(@NotNull E element) {
        if (!filter.test(element)) return -1;
        final var index = findPlace(element);
        display.add(index, element);
        return index;
    }
}
