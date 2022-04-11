package io.github.pixelj.models;

import io.github.pixelj.util.Changeable;

import javax.swing.event.EventListenerList;

/**
 * Base class for models with a single integer value that can be observed
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
