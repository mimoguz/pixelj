package pixelj.util;

public class ReadOnlyBoolean implements Detachable {
    private final Runnable cleaner;
    private final ChangeableBoolean delegate;

    public ReadOnlyBoolean(final ChangeableBoolean delegate, final Runnable cleaner) {
        this.delegate = delegate;
        this.cleaner = cleaner;
    }

    public ReadOnlyBoolean(final ChangeableBoolean delegate) {
        this(delegate, null);
    }

    public void addChangeListener(final ChangeableBoolean.Listener listener) {
        delegate.addChangeListener(listener);
    }

    public ReadOnlyBoolean and(ReadOnlyBoolean that) {
        return delegate.and(that.delegate);
    }

    public ReadOnlyBoolean and(final ChangeableBoolean that) {
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

    public ReadOnlyBoolean or(final ChangeableBoolean that) {
        return delegate.or(that);
    }

    public ReadOnlyBoolean or(final ReadOnlyBoolean that) {
        return delegate.or(that.delegate);
    }

    public void removeChangeListener(final ChangeableBoolean.Listener listener) {
        delegate.removeChangeListener(listener);
    }
}
