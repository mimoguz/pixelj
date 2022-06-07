package io.github.mimoguz.pixelj.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.ListModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.eclipse.collections.impl.map.mutable.primitive.IntObjectHashMap;

/**
 * A list model that keeps its elements always sorted.
 * <p>
 * It's Intended to be used for CharacterModels and KerningPairModels, and uses
 * a IntObjectHashMap for backing collection (so assumes no hash collisions).
 */
public class IntObjectMapModel<E extends Comparable<E>> implements ListModel<E> {
    protected final ArrayList<E> display = new ArrayList<>();
    protected final EventListenerList listeners = new EventListenerList();
    protected final IntObjectHashMap<E> source = new IntObjectHashMap<>();

    public IntObjectMapModel() {
    }

    public IntObjectMapModel(final Collection<E> elements) {
        for (final var elem : elements) {
            if (elem == null) {
                continue;
            }
            source.put(elem.hashCode(), elem);
        }
        display.addAll(source.toList());
    }

    public boolean add(final E element) {
        if (element == null || source.contains(element)) {
            return false;
        }
        source.put(element.hashCode(), element);
        final var index = insertOrdered(element);
        fireIntervalAddedEvent(index, index);
        return true;
    }

    public void addAll(final Collection<E> collection) {
        var index0 = -1;
        var index1 = -1;
        for (final var element : collection) {
            if (element == null || source.contains(element)) {
                continue;
            }
            source.put(element.hashCode(), element);
            final var index = insertOrdered(element);
            index0 = Math.min(index0, index);
            index1 = Math.max(index1, index);
        }
        if (index0 >= 0) {
            fireIntervalAddedEvent(index0, index1);
        }
    }

    @Override
    public void addListDataListener(final ListDataListener listener) {
        if (listener == null) {
            return;
        }
        listeners.add(ListDataListener.class, listener);
    }

    public void clear() {
        if (source.isEmpty()) {
            return;
        }
        final var index1 = source.size() - 1;
        source.clear();
        display.clear();
        fireIntervalRemovedEvent(0, index1);
    }

    public int countWhere(final Predicate<E> predicate) {
        return (int) source.stream().filter(predicate).count();
    }

    public List<E> find(final Predicate<E> predicate) {
        return source.stream().filter(predicate).toList();
    }

    public E findFirst(final Predicate<E> predicate) {
        return source.stream().filter(predicate).findFirst().orElse(null);
    }

    /**
     * @return E or null
     */
    public E findHash(final int hashCode) {
        return source.get(hashCode);
    }

    /**
     * @param index Index to visible list
     */
    @Override
    public E getElementAt(final int index) {
        return display.get(index);
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

    /**
     * @return If the source collection have had an element with same hash code with
     *         the parameter, returns that element. Otherwise returns null.
     */
    public E remove(final E element) {
        if (element == null) {
            return null;
        }
        final var existing = source.remove(element.hashCode());
        final var index = display.indexOf(existing);
        if (display.remove(existing)) {
            fireIntervalRemovedEvent(index, index);
        }
        return existing;
    }

    public void removeAll(final Collection<E> collection) {
        var index0 = -1;
        var index1 = -1;
        for (final var element : collection) {
            if (element == null) {
                continue;
            }
            source.remove(element.hashCode());
            final var index = display.indexOf(element);
            index0 = Math.min(index0, index);
            index1 = Math.max(index1, index);
        }
        display.removeAll(collection);
        if (index0 >= 0) {
            fireIntervalRemovedEvent(index0, index1);
        }
    }

    /**
     * @param index Index to visible list
     */
    public void removeElementAt(final int index) {
        final var element = display.get(index);
        source.remove(element.hashCode());
        display.remove(index);
        fireIntervalRemovedEvent(index, index);
    }

    /**
     * @param from Index to visible list, inclusive
     * @param to   Index to visible list, inclusive
     */
    public void removeInterval(final int from, final int to) {
        if (to < from) {
            return;
        }
        final var elements = display.subList(from, to + 1);
        elements.forEach(e -> source.remove(e.hashCode()));
        display.removeAll(elements);
        fireIntervalRemovedEvent(from, to - 1);
    }

    @Override
    public void removeListDataListener(final ListDataListener l) {
        listeners.remove(ListDataListener.class, l);
    }

    public void requestEvent(final int index) {
        fireContentsChangedEvent(index, index);
    }

    public boolean sourceContains(final E element) {
        return source.contains(element);
    }

    private int findPlace(final E element) {
        return (int) display.stream().takeWhile(item -> item.compareTo(element) < 0).count();
    }

    protected void fireContentsChangedEvent(final int index0, final int index1) {
        final var lst = listeners.getListeners(ListDataListener.class).clone();
        for (var index = lst.length - 1; index >= 0; index--) {
            lst[index]
                    .contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index0, index1));
        }
    }

    protected void fireIntervalAddedEvent(final int index0, final int index1) {
        final var lst = listeners.getListeners(ListDataListener.class);
        for (var index = lst.length - 1; index >= 0; index--) {
            lst[index].intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index0, index1));
        }
    }

    protected void fireIntervalRemovedEvent(final int index0, final int index1) {
        final var lst = listeners.getListeners(ListDataListener.class).clone();
        for (var index = lst.length - 1; index >= 0; index--) {
            lst[index]
                    .intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index0, index1));
        }
    }

    protected int insertOrdered(final E element) {
        final var index = findPlace(element);
        display.add(index, element);
        return index;
    }
}
