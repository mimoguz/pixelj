package io.github.mimoguz.pixelj.models;

import java.util.List;
import java.util.function.Predicate;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class FilteredListModel<E extends Comparable<E>> extends IntObjectMapModel<E> {
    private final IntObjectMapModel<E> delegate;
    private Predicate<E> filter = t -> true;

    public FilteredListModel(IntObjectMapModel<E> delegate) {
        this.delegate = delegate;
        addAll(delegate.display.stream().filter(filter).toList());
        delegate.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                if (e.getIndex0() == e.getIndex1()) {
                    final var item = delegate.getElementAt(e.getIndex0());
                    if (filter.test(item)) {
                        add(item);
                    }
                } else {
                    final var items = delegate.display.subList(e.getIndex0(), e.getIndex1())
                            .stream()
                            .filter(filter)
                            .toList();
                    addAll(items);
                }
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                final var items = delegate.display.subList(e.getIndex0(), e.getIndex1())
                        .stream()
                        .filter(filter)
                        .count();
                if (items > 0) {
                    clear();
                    addAll(delegate.display.stream().filter(filter).toList());
                }
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                clear();
                addAll(delegate.display.stream().filter(filter).toList());
            }
        });
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
        final var element = display.get(index);
        delegate.remove(element);
    }

    @Override
    public void removeInterval(final int from, final int until) {
        final var elements = display.subList(from, until);
        delegate.removeAll(elements);
    }

    @Override
    public boolean sourceContains(final E element) {
        return delegate.sourceContains(element);
    }
}
