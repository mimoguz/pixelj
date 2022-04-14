package io.github.mimoguz.pixelj.models;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A list model that keeps its visible elements always filtered and sorted.
 * <p>
 * It's Intended to be used for CharacterModels and KerningPairModels, and uses
 * a HashSet for backing collection (so assumes no hash collisions).
 */
public class SortedFilteredListModel<T extends Comparable<T>> extends DefaultListModel<T> {
    protected final HashSet<T> source = new HashSet<>();
    private final @NotNull Class<T> classT;
    private transient @NotNull Predicate<T> filter = t -> true;

    public SortedFilteredListModel(@NotNull Class<T> classT) {
        this.classT = classT;
    }

    public SortedFilteredListModel(@NotNull Class<T> classT, @NotNull Collection<T> elements) {
        this.classT = classT;
        elements.removeIf(Objects::isNull);
        source.addAll(elements);
        refillElements();
    }

    /**
     * Adds the item to the source. Keeps the model sorted and filtered.
     *
     * @param index   Ignored
     * @param element The item to add.
     */
    @Override
    public void add(int index, @NotNull T element) {
        addElement(element);
    }

    /**
     * Adds the items to the source. Keeps the model sorted and filtered.
     *
     * @param collection The items to add.
     */
    @Override
    public void addAll(Collection<? extends T> collection) {
        source.addAll(collection);
        refillElements();
    }

    /**
     * Adds the items to the source. Keeps the model sorted and filtered.
     *
     * @param index      Ignored
     * @param collection The items to add.
     */
    @Override
    public void addAll(int index, Collection<? extends T> collection) {
        source.addAll(collection);
        refillElements();
    }

    /**
     * Adds the item to the source. Keeps the model sorted and filtered.
     */
    @Override
    public void addElement(@NotNull T element) {
        source.add(element);
        insertOrdered(element);
    }

    /**
     * Remove all elements.
     */
    @Override
    public void clear() {
        source.clear();
        super.clear();
    }

    public void clearFilter() {
        setFilter(model -> true);
    }

    /**
     * Fires a fake content change event. Use this to force connected JList to
     * refresh.
     */
    public void fireRefresh(int modelIndex) {
        fireContentsChanged(this, modelIndex, modelIndex);
    }

    /**
     * Fires a fake content change event. Use this to force connected JList to
     * refresh.
     */
    public void fireRefresh() {
        fireContentsChanged(this, 0, super.size());
    }

    /**
     * Adds item to the source. Keeps the model sorted and filtered.
     *
     * @param index   Ignored
     * @param element The item to add.
     */
    @Override
    public void insertElementAt(@NotNull T element, int index) {
        addElement(element);
    }

    /**
     * Removes an item from both source and the model.
     *
     * @param modelIndex The index to the filtered, sorted model data.
     */
    @Override
    public T remove(int modelIndex) {
        final var element = super.get(modelIndex);
        source.remove(element);
        return super.remove(modelIndex);
    }

    @Override
    public void removeAllElements() {
        clear();
    }

    /**
     * Removes an item from both source and the model.
     *
     * @param obj The index to the filtered, sorted model data.
     */
    @Override
    public boolean removeElement(Object obj) {
        if (obj.getClass().equals(classT)) {
            source.remove(classT.cast(obj));
            return super.removeElement(obj);
        }
        return false;
    }

    /**
     * Removes an item from both source and the model.
     *
     * @param modelIndex The index to the filtered, sorted model data.
     */
    @Override
    public void removeElementAt(int modelIndex) {
        final var item = super.get(modelIndex);
        source.remove(item);
        super.removeElementAt(modelIndex);
    }

    /**
     * Removes multiple elements.
     *
     * @param collection The elements to be removed
     */
    public void removeElements(Collection<T> collection) {
        source.removeAll(collection);
        refillElements();
    }

    /**
     * Removes a range from both source and the model.
     *
     * @param fromIndex The first index to the filtered, sorted model data.
     * @param toIndex   The last index to the filtered, sorted model data.
     */
    @Override
    public void removeRange(int fromIndex, int toIndex) {
        for (var index = fromIndex; index <= toIndex; index++) {
            source.remove(super.get(index));
        }
        super.removeRange(fromIndex, toIndex);
    }

    /**
     * Updates both the source and the model.
     *
     * @param index   The index to the filtered, sorted model data.
     * @param element The new item.
     * @return The old item.
     */
    @Override
    public T set(int index, T element) {
        final var oldElement = super.get(index);
        source.remove(oldElement);
        source.add(oldElement);
        return super.set(index, element);
    }

    /**
     * Updates both the source and the model. Breaks sorting and filtering.
     *
     * @param element The new item.
     * @param index   The index to the filtered, sorted model data.
     */
    @Override
    public void setElementAt(T element, int index) {
        set(index, element);
    }

    public void setFilter(Predicate<T> filter) {
        this.filter = filter;
        refillElements();
    }

    /**
     * Find the index of the first element that is bigger than the parameter.
     *
     * @param element Reference element
     * @return Fount index or -1
     */
    private int firstBigger(T element) {
        for (var index = 0; index < super.size(); index++) {
            if (super.get(index).compareTo(element) > 0) {
                return index;
            }
        }
        return -1;
    }

    /**
     * Insert element to the model while keeping sorting and filtering.
     */
    private void insertOrdered(@NotNull T element) {
        if (!filter.test(element)) {
            return;
        }
        final var index = firstBigger(element);
        if (index < 0) {
            return;
        }
        super.add(index, element);
    }

    /**
     * Clear and refill the model.
     */
    private void refillElements() {
        super.removeAllElements();
        super.addAll(source.stream().filter(filter).sorted().toList());
    }
}
