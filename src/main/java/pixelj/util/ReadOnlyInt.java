package pixelj.util;

/** Read-only wrapper for ChangeableInt. */
public final class ReadOnlyInt implements Detachable {
    private final Runnable cleaner;
    private final ChangeableInt delegate;

    public ReadOnlyInt(final ChangeableInt delegate, final Runnable cleaner) {
        this.delegate = delegate;
        this.cleaner = cleaner;
    }

    public ReadOnlyInt(final ChangeableInt delegate) {
        this(delegate, null);
    }

    public int getValue() {
        return delegate.getValue();
    }

    /**
     * @see ChangeableInt#add(ChangeableInt)
     *
     * @param that
     * @return this + that
     */
    public ReadOnlyInt add(final ReadOnlyInt that) {
        return delegate.add(that.delegate);
    }

    /**
     * @see ChangeableInt#add(ChangeableInt)
     *
     * @param that
     * @return this + that
     */
    public ReadOnlyInt add(final ChangeableInt that) {
        return delegate.add(that);
    }

    /**
     * @see ChangeableInt#divide(ChangeableInt)
     *
     * @param that
     * @return this / that
     */
    public ReadOnlyInt divide(final ReadOnlyInt that) {
        return delegate.divide(that.delegate);
    }

    /**
     * @see ChangeableInt#divide(ChangeableInt)
     *
     * @param that
     * @return this / that
     */
    public ReadOnlyInt divide(final ChangeableInt that) {
        return delegate.divide(that);
    }

    /**
     * @see ChangeableInt#ge(ChangeableInt)
     *
     * @param that
     * @return this >= that
     */
    public ReadOnlyBoolean ge(final ChangeableInt that) {
        return delegate.ge(that);
    }

    /**
     * @see ChangeableInt#ge(ChangeableInt)
     *
     * @param that
     * @return this >= that
     */
    public ReadOnlyBoolean ge(final ReadOnlyInt that) {
        return delegate.ge(that.delegate);
    }

    /**
     * @see ChangeableInt#gt(ChangeableInt)
     *
     * @param that
     * @return this > that
     */
    public ReadOnlyBoolean gt(final ChangeableInt that) {
        return delegate.gt(that);
    }

    /**
     * @see ChangeableInt#gt(ChangeableInt)
     *
     * @param that
     * @return this > that
     */
    public ReadOnlyBoolean gt(final ReadOnlyInt that) {
        return delegate.gt(that.delegate);
    }

    /**
     * @see ChangeableInt#le(ChangeableInt)
     *
     * @param that
     * @return this <= that
     */
    public ReadOnlyBoolean le(final ChangeableInt that) {
        return delegate.le(that);
    }

    /**
     * @see ChangeableInt#le(ChangeableInt)
     *
     * @param that
     * @return this <= that
     */
    public ReadOnlyBoolean le(final ReadOnlyInt that) {
        return delegate.le(that.delegate);
    }

    /**
     * @see ChangeableInt#lt(ChangeableInt)
     *
     * @param that
     * @return this < that
     */
    public ReadOnlyBoolean lt(final ChangeableInt that) {
        return delegate.lt(that);
    }

    /**
     * @see ChangeableInt#lt(ChangeableInt)
     *
     * @param that
     * @return this < that
     */
    public ReadOnlyBoolean lt(final ReadOnlyInt that) {
        return delegate.lt(that.delegate);
    }

    /**
     * @see ChangeableInt#multiply(ChangeableInt)
     *
     * @param that
     * @return this * that
     */
    public ReadOnlyInt multiply(final ReadOnlyInt that) {
        return delegate.multiply(that.delegate);
    }

    /**
     * @see ChangeableInt#multiply(ChangeableInt)
     *
     * @param that
     * @return this * that
     */
    public ReadOnlyInt multiply(final ChangeableInt that) {
        return delegate.multiply(that);
    }

    /**
     * @see ChangeableInt#subtract(ChangeableInt)
     *
     * @param that
     * @return this - that
     */
    public ReadOnlyInt subtract(final ReadOnlyInt that) {
        return delegate.subtract(that.delegate);
    }

    /**
     * @see ChangeableInt#subtract(ChangeableInt)
     *
     * @param that
     * @return this - that
     */
    public ReadOnlyInt subtract(final ChangeableInt that) {
        return delegate.subtract(that);
    }

    /**
     * @see ChangeableInt#addChangeListener(pixelj.util.ChangeableInt.Listener)
     *
     * @param listener
     */
    public void addChangeListener(final ChangeableInt.Listener listener) {
        delegate.addChangeListener(listener);
    }

    /**
     * @see ChangeableInt#removeChangeListener(pixelj.util.ChangeableInt.Listener)
     *
     * @param listener
     */
    public void removeChangeListener(final ChangeableInt.Listener listener) {
        delegate.removeChangeListener(listener);
    }

    @Override
    public void detach() {
        if (cleaner != null) {
            cleaner.run();
        }
    }
}
