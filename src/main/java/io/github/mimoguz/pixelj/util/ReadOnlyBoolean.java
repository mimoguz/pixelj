package io.github.mimoguz.pixelj.util;

public class ReadOnlyBoolean implements Detachable {
    private final Runnable cleaner;
    private final ChangeableBoolean delegate;

    public ReadOnlyBoolean(ChangeableBoolean delegate, Runnable cleaner) {
        this.delegate = delegate;
        this.cleaner = cleaner;
    }

    public ReadOnlyBoolean(ChangeableBoolean delegate) {
        this(delegate, null);
    }

    public void addChangeListener(ChangeableBoolean.Listener listener) {
        delegate.addChangeListener(listener);
    }

    public ReadOnlyBoolean and(ReadOnlyBoolean that) {
        return delegate.and(that.delegate);
    }

    public ReadOnlyBoolean and(ChangeableBoolean that) {
        return delegate.and(that);
    }

    @Override
    public void detach() {
        if (cleaner != null) {
            cleaner.run();
        }
    }

    public boolean getValue() {
        return delegate.getValue();
    }

    public ReadOnlyBoolean or(ChangeableBoolean that) {
        return delegate.or(that);
    }

    public ReadOnlyBoolean or(ReadOnlyBoolean that) {
        return delegate.or(that.delegate);
    }

    public void removeChangeListener(ChangeableBoolean.Listener listener) {
        delegate.removeChangeListener(listener);
    }
}
