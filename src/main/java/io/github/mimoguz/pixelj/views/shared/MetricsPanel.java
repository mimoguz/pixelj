package io.github.mimoguz.pixelj.views.shared;

import io.github.mimoguz.pixelj.models.Metrics;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.util.ChangeListener;
import io.github.mimoguz.pixelj.util.Changeable;
import io.github.mimoguz.pixelj.util.ChangeableBoolean;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.util.function.BooleanSupplier;

public class MetricsPanel extends JPanel implements Changeable<MetricsPanel, Boolean, MetricsPanel.InputChangeListener> {
    private final JSpinner ascender = getSpinner();
    private final JSpinner canvasHeight = getSpinner();
    private final JSpinner canvasWidth = getSpinner();
    private final JSpinner capHeight = getSpinner();
    private final JSpinner defaultCharacterWidth = getSpinner();
    private final JSpinner descender = getSpinner();
    private final JCheckBox isMonospaced = new JCheckBox();
    private final JSpinner lineSpacing = getSpinner();
    private final EventListenerList listeners = new EventListenerList();
    private final JSpinner spaceSize = getSpinner(0);
    private final JSpinner spacing = getSpinner(0);
    private final ChangeableBoolean validAscender = new ChangeableBoolean();
    private final ChangeableBoolean validCapHeight = new ChangeableBoolean();
    private final ChangeableBoolean validDefaultCharacterWidth = new ChangeableBoolean();
    private final ChangeableBoolean validDescender = new ChangeableBoolean();
    private final ChangeableBoolean validXHeight = new ChangeableBoolean();
    private final ReadOnlyBoolean valid = new ReadOnlyBoolean(validAscender).and(validDescender)
            .and(validCapHeight)
            .and(validXHeight)
            .and(validDefaultCharacterWidth);
    private final JSpinner xHeight = getSpinner(0);
    ;

    public MetricsPanel(final Metrics init, final boolean canEditCanvasSize) {
        canvasHeight.addChangeListener(this::onSpinnerChanged);
        canvasWidth.addChangeListener(this::onSpinnerChanged);
        ascender.addChangeListener(this::onSpinnerChanged);
        descender.addChangeListener(this::onSpinnerChanged);
        capHeight.addChangeListener(this::onSpinnerChanged);
        xHeight.addChangeListener(this::onSpinnerChanged);
        defaultCharacterWidth.addChangeListener(this::onSpinnerChanged);

        valid.addChangeListener((sender, value) -> fireChangeEvent(this, value));

        final var res = Resources.get();

        setLayout(new GridBagLayout());
        setBorder(Borders.EMPTY);
        final var cons = new GridBagConstraints();

        cons.gridx = 0;
        cons.gridy = 0;
        cons.weightx = 1.0;
        cons.insets = new Insets(0, 0, Dimensions.SMALL_PADDING, Dimensions.MEDIUM_PADDING);
        cons.anchor = GridBagConstraints.LINE_START;
        add(new JLabel(res.getString("metricsCanvasWidth")), cons);
        cons.gridy = GridBagConstraints.RELATIVE;
        add(new JLabel(res.getString("metricsCanvasHeight")), cons);
        add(new JLabel(res.getString("metricsAscender")), cons);
        add(new JLabel(res.getString("metricsDescender")), cons);
        add(new JLabel(res.getString("metricsCapHeight")), cons);
        add(new JLabel(res.getString("metricsXHeight")), cons);
        add(new JLabel(res.getString("metricsDefaultCharacterWidth")), cons);
        add(new JLabel(res.getString("metricsCharacterSpacing")), cons);
        add(new JLabel(res.getString("metricsSpaceSize")), cons);
        add(new JLabel(res.getString("metricsLineSpacing")), cons);
        final var isMonospacedLabel = new JLabel(res.getString("metricsIsMonospaced"));
        isMonospacedLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        add(isMonospacedLabel, cons);

        cons.gridx = 1;
        cons.gridy = 0;
        cons.weightx = 0.0;
        cons.insets = new Insets(0, 0, Dimensions.SMALL_PADDING, 0);
        cons.fill = GridBagConstraints.NONE;
        cons.anchor = GridBagConstraints.LINE_END;
        add(canvasWidth, cons);
        cons.gridy = GridBagConstraints.RELATIVE;
        add(canvasHeight, cons);
        add(ascender, cons);
        add(descender, cons);
        add(capHeight, cons);
        add(xHeight, cons);
        add(defaultCharacterWidth, cons);
        add(spacing, cons);
        add(spaceSize, cons);
        add(lineSpacing, cons);
        add(isMonospaced, cons);

        setMetrics(init, canEditCanvasSize);
    }

    @Override
    public Class<InputChangeListener> getListenerClass() {
        return InputChangeListener.class;
    }

    @Override
    public EventListenerList getListenerList() {
        return listeners;
    }

    public Metrics getMetrics() {
        return Metrics.Builder.getDefault()
                .setCanvasWidth(getValue(canvasWidth))
                .setCanvasHeight(getValue(canvasHeight))
                .setAscender(getValue(ascender))
                .setDescender(getValue(descender))
                .setCapHeight(getValue(capHeight))
                .setXHeight(getValue(xHeight))
                .setDefaultCharacterWidth(getValue(defaultCharacterWidth))
                .setSpacing(getValue(spacing))
                .setSpaceSize(getValue(spaceSize))
                .setLineSpacing(getValue(lineSpacing))
                .setMonospaced(isMonospaced.isSelected())
                .build();
    }

    public boolean isMetricsValid() {
        return valid.getValue();
    }

    public void setMetrics(final Metrics metrics, boolean canEditCanvasSize) {
        canvasWidth.setValue(metrics.canvasWidth());
        canvasHeight.setValue(metrics.canvasHeight());
        ascender.setValue(metrics.ascender());
        descender.setValue(metrics.descender());
        capHeight.setValue(metrics.capHeight());
        xHeight.setValue(metrics.xHeight());
        defaultCharacterWidth.setValue(metrics.defaultCharacterWidth());
        spacing.setValue(metrics.spacing());
        spaceSize.setValue(metrics.spaceSize());
        lineSpacing.setValue(metrics.lineSpacing());
        isMonospaced.setSelected(metrics.isMonospaced());

        canvasHeight.setEnabled(canEditCanvasSize);
        canvasWidth.setEnabled(canEditCanvasSize);
    }

    public ReadOnlyBoolean validProperty() {
        return valid;
    }

    private static JSpinner getSpinner() {
        return getSpinner(1);
    }

    private static JSpinner getSpinner(final int minimum) {
        final var spinner = new JSpinner(new SpinnerNumberModel(minimum, minimum, 512, 1));
        Components.setFixedSize(spinner, Dimensions.SPINNER_SIZE);
        return spinner;
    }

    private static int getValue(final JSpinner spinner) {
        return ((SpinnerNumberModel) spinner.getModel()).getNumber().intValue();
    }

    @SuppressWarnings({"java:S2178"})
    private void onSpinnerChanged(final ChangeEvent e) {
        // Do not short-circuit
        validateAscender();
        validateDescender();
        validateCapHeight();
        validateXHeight();
        validateDefaultWidth();
    }

    private void validate(BooleanSupplier supplier, ChangeableBoolean target, JSpinner control) {
        final var isValid = supplier.getAsBoolean();
        if (isValid != target.getValue()) {
            target.setValue(isValid);
            control.putClientProperty(
                    FlatClientProperties.OUTLINE,
                    isValid ? null : FlatClientProperties.OUTLINE_ERROR
            );
        }
    }

    private void validateAscender() {
        // ascender <= canvasHeight - descender
        validate(() -> getValue(descender) <= getValue(canvasWidth) - getValue(ascender), validAscender, ascender);
    }

    private void validateCapHeight() {
        // ascender <= canvasHeight - descender
        validate(() -> getValue(capHeight) <= getValue(ascender), validCapHeight, capHeight);
    }

    private void validateDefaultWidth() {
        // defaultWidth <= canvasWidth
        validate(
                () -> getValue(defaultCharacterWidth) <= getValue(canvasWidth),
                validDefaultCharacterWidth,
                defaultCharacterWidth
        );
    }

    private void validateDescender() {
        // descender <= canvasHeight
        validate(() -> getValue(descender) <= getValue(canvasWidth), validDescender, descender);
    }

    private void validateXHeight() {
        // xHeight <= capHeight
        validate(() -> getValue(xHeight) <= getValue(capHeight), validXHeight, xHeight);
    }

    public interface InputChangeListener extends ChangeListener<MetricsPanel, Boolean> {
        // Empty
    }
}
