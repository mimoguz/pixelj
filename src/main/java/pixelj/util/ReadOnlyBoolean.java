package pixelj.util;

/** Read-only wrapper for ChangeableBoolean. */
public final class ReadOnlyBoolean implements Detachable {
    private final Runnable cleaner;
    private final ChangeableBoolean delegate;

    public ReadOnlyBoolean(final ChangeableBoolean delegate, final Runnable cleaner) {
        this.delegate = delegate;
        this.cleaner = cleaner;
    }

    public ReadOnlyBoolean(final ChangeableBoolean delegate) {
        this(delegate, null);
    }

    public boolean getValue() {
        return delegate.getValue();
    }

    /**
     * @see ChangeableBoolean#addChangeListener(pixelj.util.ChangeableBoolean.Listener)
     *
     * @param listener
     */
    public void addChangeListener(final ChangeableBoolean.Listener listener) {
        delegate.addChangeListener(listener);
    }

    /**
     * @see ChangeableBoolean#removeChangeListener(pixelj.util.ChangeableBoolean.Listener)
     *
     * @param listener
     */
    public void removeChangeListener(final ChangeableBoolean.Listener listener) {
        delegate.removeChangeListener(listener);
    }

    /**
     * @see ChangeableBoolean#and(ChangeableBoolean)
     *
     * @param that
     * @return this && that
     */
    public ReadOnlyBoolean and(final ReadOnlyBoolean that) {
        return delegate.and(that.delegate);
    }

    /**
     * @see ChangeableBoolean#and(ChangeableBoolean)
     *
     * @param that
     * @return this && that
     */
    public ReadOnlyBoolean and(final ChangeableBoolean that) {
        return delegate.and(that);
    }

    /**
     * @see ChangeableBoolean#or(ChangeableBoolean)
     *
     * @param that
     * @return this || that
     */
    public ReadOnlyBoolean or(final ChangeableBoolean that) {
        return delegate.or(that);
    }

    /**
     * @see ChangeableBoolean#or(ChangeableBoolean)
     *
     * @param that
     * @return this || that
     */
    public ReadOnlyBoolean or(final ReadOnlyBoolean that) {
        return delegate.or(that.delegate);
    }

    @Override
    public void detach() {
        if (cleaner != null) {
            cleaner.run();
        }
    }
}
