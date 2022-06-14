package io.github.mimoguz.pixelj.util;

public class ReadOnlyInt {
    private final ChangeableInt delegate;

    public ReadOnlyInt(ChangeableInt delegate) {
        this.delegate = delegate;
    }

    public ReadOnlyInt add(ReadOnlyInt that) {
        return delegate.add(that.delegate);
    }

    public ReadOnlyInt add(ChangeableInt that) {
        return delegate.add(that);
    }

    public void addChangeListener(ChangeableInt.Listener listener) {
        delegate.addChangeListener(listener);
    }

    public ReadOnlyInt divide(ReadOnlyInt that) {
        return delegate.divide(that.delegate);
    }

    public ReadOnlyInt divide(ChangeableInt that) {
        return delegate.divide(that);
    }

    public int getValue() {
        return delegate.getValue();
    }

    public ReadOnlyInt multiply(ReadOnlyInt that) {
        return delegate.multiply(that.delegate);
    }

    public ReadOnlyInt multiply(ChangeableInt that) {
        return delegate.multiply(that);
    }

    public void removeChangeListener(ChangeableInt.Listener listener) {
        delegate.removeChangeListener(listener);
    }

    public ReadOnlyInt subtract(ReadOnlyInt that) {
        return delegate.subtract(that.delegate);
    }

    public ReadOnlyInt subtract(ChangeableInt that) {
        return delegate.subtract(that);
    }
}
