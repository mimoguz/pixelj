package io.github.mimoguz.pixelj.util;

public class ChangeableBooleanValue extends ChangeableValue<Boolean> {
    public ChangeableBooleanValue() {
        super(BooleanValueChangeListener.class);
    }

    public ChangeableBooleanValue and(ChangeableBooleanValue that) {
        final var result = new ChangeableBooleanValue();
        final BooleanValueChangeListener listenerThis = (sender, a) -> result.setValue(a && that.getValue());
        final BooleanValueChangeListener listenerThat = (sender, b) -> result.setValue(b && getValue());
        this.addChangeListener(listenerThis);
        that.addChangeListener(listenerThat);
        return result;
    }

    public ChangeableBooleanValue not() {
        final var result = new ChangeableBooleanValue();
        final BooleanValueChangeListener listener = (sender, a) -> result.setValue(!a);
        addChangeListener(listener);
        return result;
    }

    public ChangeableBooleanValue or(ChangeableBooleanValue that) {
        final var result = new ChangeableBooleanValue();
        final BooleanValueChangeListener listenerThis = (sender, a) -> result.setValue(a || that.getValue());
        final BooleanValueChangeListener listenerThat = (sender, b) -> result.setValue(b || getValue());
        this.addChangeListener(listenerThis);
        that.addChangeListener(listenerThat);
        return result;
    }

    public interface BooleanValueChangeListener extends ValueChangeListener<Boolean> {
        // Empty
    }
}