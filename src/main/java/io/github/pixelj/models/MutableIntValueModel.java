package io.github.pixelj.models;

import io.github.pixelj.util.Changeable;

import javax.swing.event.EventListenerList;

/**
 * CharacterModel and KerningPairModel both have a single integer value that can be observed.
 * This is the base class for them.
 */
public class MutableIntValueModel implements Changeable<MutableIntValueModel, IntValueChangeEvent, IntValueChangeListener> {
    private final EventListenerList listeners = new EventListenerList();

    @Override
    public EventListenerList getListenerList() {
        return listeners;
    }

    @Override
    public Class<IntValueChangeListener> getListenerClass() {
        return IntValueChangeListener.class;
    }
}
