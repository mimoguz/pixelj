package io.github.mimoguz.pixelj.models;

import javax.swing.event.EventListenerList;

import io.github.mimoguz.pixelj.util.Changeable;

/**
 * CharacterItem and KerningPair both have a single integer value that can be
 * observed. This is the base class for them.
 */
public class MutableIntValueModel
        implements
        Changeable<MutableIntValueModel, IntValueChangeEvent, IntValueChangeListener> {
    private final EventListenerList listeners = new EventListenerList();

    @Override
    public Class<IntValueChangeListener> getListenerClass() {
        return IntValueChangeListener.class;
    }

    @Override
    public EventListenerList getListenerList() {
        return listeners;
    }
}
