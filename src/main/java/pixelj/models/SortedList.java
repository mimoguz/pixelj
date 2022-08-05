package pixelj.models;

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
 * It's Intended to be used for CharacterModels and KerningPairModels, and uses
 * a IntObjectHashMap for backing collection (so assumes no hash collisions).
 *
 * @param <E> Element type. Should have a unique hash.
 */
public class SortedList<E extends Comparable<E>> implements ListModel<E> {
    protected final ArrayList<E> display = new ArrayList<>();
    protected final EventListenerList listeners = new EventListenerList();
    protected final IntObjectHashMap<E> source = new IntObjectHashMap<>();

    public SortedList() {
    }

    public SortedList(final Collection<E> elements) {
        for (final var elem : elements) {
            if (elem == null) {
                continue;
            }
            source.put(elem.hashCode(), elem);
        }
        display.addAll(source.toList());
    }

    /**
     * @return A list of all elements. This will create a new list on each call.
     */
    public List<E> getElements() {
        return display;
    }

    /**
     * @param element
     * @return True if the element was added (if it wasn't already in the list), false otherwise.
     */
    public boolean add(final E element) {
        if (element == null || source.contains(element)) {
            return false;
        }
        source.put(element.hashCode(), element);
        final var index = insertOrdered(element);
        fireIntervalAddedEvent(index, index);
        return true;
    }

    /**
     * @param collection A collection of elements to be added to the list.
     */
    public void addAll(final Collection<E> collection) {
        var index0 = -1;
        var index1 = -1;
        for (final var element : collection) {
            if (element == null || source.contains(element)) {
                continue;
            }
            source.put(element.hashCode(), element);
            final var index = insertOrdered(element);
            if (index0 == -1 || index < index0) {
                index0 = index;
            }
            if (index1 == -1 || index > index1) {
                index1 = index;
            } else if (index > index0) {
                index1 += 1;
            }
        }
        if (index0 >= 0) {
            fireIntervalAddedEvent(index0, index1);
        }
    }

    /**
     * Remove all element.
     */
    public void clear() {
        if (source.isEmpty()) {
            return;
        }
        final var index1 = source.size() - 1;
        source.clear();
        display.clear();
        fireIntervalRemovedEvent(0, index1);
    }

    /**
     * @param predicate
     * @return The number of elements which the predicate tests true.
     */
    public int countWhere(final Predicate<E> predicate) {
        return (int) source.stream().filter(predicate).count();
    }

    /**
     * @param predicate
     * @return The list of elements which the predicate tests true.
     */
    public List<E> find(final Predicate<E> predicate) {
        return source.stream().filter(predicate).toList();
    }

    /**
     * @param predicate
     * @return The first which the predicate tests true. Null if there were no matches.
     */
    public E findFirst(final Predicate<E> predicate) {
        return source.stream().filter(predicate).findFirst().orElse(null);
    }

    /**
     * Get by hash code.
     *
     * @param hashCode
     * @return E or null
     */
    public E findHash(final int hashCode) {
        return source.get(hashCode);
    }

    /**
     * @param element Element to be searched
     * @return True if the element is in the list, false otherwise.
     */
    public boolean sourceContains(final E element) {
        return source.contains(element);
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
     * @param element The element to be removed
     * @return If the source collection have had an element with same hash code with
     *         the parameter, removes that element from the list and returns it. Otherwise, returns null.
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

    /**
     * @param collection The elements to be removed
     */
    public void removeAll(final Collection<E> collection) {
        var index0 = -1;
        var index1 = -1;
        for (final var element : collection) {
            if (element == null || !source.containsKey(element.hashCode())) {
                continue;
            }
            source.remove(element.hashCode());
            final var index = display.indexOf(element);
            if (index0 == -1 || index < index0) {
                index0 = index;
            }
            if (index1 == -1 || index > index1) {
                index1 = index;
            } else if (index > index0) {
                index1 += 1;
            }
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

    /**
     * Request a notification without actually modifying the list.
     *
     * @param index Index to visible list
     */
    public void requestEvent(final int index) {
        fireContentsChangedEvent(index, index);
    }

    /**
     * @see javax.swing.ListModel#addListDataListener(javax.swing.event.ListDataListener)
     */
    @Override
    public void addListDataListener(final ListDataListener listener) {
        if (listener == null) {
            return;
        }
        listeners.add(ListDataListener.class, listener);
    }

    /**
     * @see javax.swing.ListModel#removeListDataListener(javax.swing.event.ListDataListener)
     */
    @Override
    public void removeListDataListener(final ListDataListener l) {
        listeners.remove(ListDataListener.class, l);
    }

    /**
     * @param index Index to visible list
     */
    @Override
    public E getElementAt(final int index) {
        return display.get(index);
    }

    /**
     * @param index0 From index, inclusive.
     * @param index1 To index, inclusive.
     */
    protected void fireContentsChangedEvent(final int index0, final int index1) {
        final var lst = listeners.getListeners(ListDataListener.class).clone();
        for (var index = lst.length - 1; index >= 0; index--) {
            lst[index].contentsChanged(
                new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index0, index1)
            );
        }
    }

    /**
     * @param index0 From index, inclusive.
     * @param index1 To index, inclusive.
     */
    protected void fireIntervalAddedEvent(final int index0, final int index1) {
        final var lst = listeners.getListeners(ListDataListener.class);
        for (var index = lst.length - 1; index >= 0; index--) {
            lst[index].intervalAdded(new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index0, index1));
        }
    }

    /**
     * @param index0 From index, inclusive.
     * @param index1 To index, inclusive.
     */
    protected void fireIntervalRemovedEvent(final int index0, final int index1) {
        final var lst = listeners.getListeners(ListDataListener.class).clone();
        for (var idx = lst.length - 1; idx >= 0; idx--) {
            lst[idx].intervalRemoved(new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index0, index1));
        }
    }

    /**
     * @param element The element to be inserted.
     * @return The index which the element was inserted.
     */
    protected int insertOrdered(final E element) {
        final var index = findPlace(element);
        display.add(index, element);
        return index;
    }

    private int findPlace(final E element) {
        return (int) display.stream().takeWhile(item -> item.compareTo(element) < 0).count();
    }
}
