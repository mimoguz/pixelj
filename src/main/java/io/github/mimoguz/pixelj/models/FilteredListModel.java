package io.github.mimoguz.pixelj.models;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class FilteredListModel<E extends Comparable<E>> extends IntObjectMapModel<E> {
    private static record Interval(int index0, int index1) {
    }

    private final IntObjectMapModel<E> delegate;

    private Predicate<E> filter = t -> true;

    public FilteredListModel(final IntObjectMapModel<E> delegate) {
        this.delegate = delegate;
        pushAll(delegate.display);

        // Addition to/removal from this model will be handled by this listener.
        // Related overrides should just change the delegate.
        delegate.addListDataListener(new ListDataListener() {
            @Override
            public void contentsChanged(final ListDataEvent e) {
                clear();
                pushAll(delegate.display);
                fireContentsChangedEvent(0, display.size());
            }

            @Override
            public void intervalAdded(final ListDataEvent e) {
                if (e.getIndex0() == e.getIndex1()) {
                    if (push(delegate.getElementAt(e.getIndex0()))) {
                        fireIntervalAddedEvent(e.getIndex0(), e.getIndex0());
                    }
                } else {
                    final var interval = pushAll(delegate.display.subList(e.getIndex0(), e.getIndex1() + 1));
                    if (interval.index0 >= 0) {
                        fireIntervalAddedEvent(interval.index0, interval.index1);
                    }
                }
            }

            @Override
            public void intervalRemoved(final ListDataEvent e) {
                if (e.getIndex0() == e.getIndex1()) {
                    final var element = delegate.getElementAt(e.getIndex0());
                    final var hash = element.hashCode();
                    if (source.containsKey(hash)) {
                        source.remove(hash);
                        final var index = display.indexOf(element);
                        display.remove(index);
                        fireIntervalRemovedEvent(index, index);
                    }
                } else {
                    clear();
                    pushAll(delegate.display.stream().filter(filter).toList());
                    fireContentsChangedEvent(0, display.size());
                }
            }
        });
    }

    @Override
    public boolean add(final E element) {
        return delegate.add(element);
    }

    @Override
    public void addAll(final Collection<E> collection) {
        delegate.addAll(collection);
    }

    @Override
    public int countWhere(final Predicate<E> predicate) {
        return delegate.countWhere(predicate);
    }

    @Override
    public List<E> find(final Predicate<E> predicate) {
        return delegate.find(predicate);
    }

    @Override
    public E findFirst(final Predicate<E> predicate) {
        return delegate.findFirst(predicate);
    }

    @Override
    public E findHash(final int hashCode) {
        return delegate.findHash(hashCode);
    }

    @Override
    public int getSourceSize() {
        return delegate.getSourceSize();
    }

    @Override
    public void removeElementAt(final int index) {
        delegate.remove(display.get(index));
    }

    @Override
    public void removeInterval(final int from, final int to) {
        delegate.removeAll(display.subList(from, to + 1));
    }

    public void setFilter(final Predicate<E> filter) {
        this.filter = filter;
        clear();
        pushAll(delegate.display);
        fireContentsChangedEvent(0, display.size());
    }

    @Override
    public boolean sourceContains(final E element) {
        return delegate.sourceContains(element);
    }

    private boolean push(final E element) {
        if (filter.test(element) && !source.containsKey(element.hashCode())) {
            source.put(element.hashCode(), element);
            insertOrdered(element);
            return true;
        }
        return false;
    }

    private Interval pushAll(final Collection<E> collection) {
        var index0 = -1;
        var index1 = -1;
        for (final var element : collection) {
            if (!filter.test(element) || source.containsKey(element.hashCode())) {
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
        return new Interval(index0, index1);
    }
}
