package io.github.mimoguz.pixelj.views.shared;

import io.github.mimoguz.pixelj.models.Metrics;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.util.ChangeListener;
import io.github.mimoguz.pixelj.util.Changeable;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import java.awt.*;

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
    private final JSpinner xHeight = getSpinner(0);
    private boolean valid;

    public MetricsPanel(final Metrics init, final boolean canEditCanvasSize) {
        canvasHeight.addChangeListener(this::onSpinnerChanged);
        canvasWidth.addChangeListener(this::onSpinnerChanged);
        ascender.addChangeListener(this::onSpinnerChanged);
        descender.addChangeListener(this::onSpinnerChanged);
        capHeight.addChangeListener(this::onSpinnerChanged);
        xHeight.addChangeListener(this::onSpinnerChanged);
        defaultCharacterWidth.addChangeListener(this::onSpinnerChanged);

        final var res = Resources.get();

        setLayout(new GridBagLayout());
        final var cons = new GridBagConstraints();
        cons.insets = new Insets(0, 4, 4, 4);

        cons.gridx = 0;
        cons.gridy = 0;
        cons.weightx = 1.0;
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
        add(new JLabel(res.getString("metricsIsMonospaced")), cons);

        cons.gridx = 1;
        cons.gridy = 0;
        cons.weightx = 0.0;
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

    @Override
    public boolean isValid() {
        return valid;
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

        valid = validateAscender() & validateDescender() & validateCapHeight() & validateXHeight()
                & validateDefaultWidth();

        fireChangeEvent(this, valid);
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
        valid = validateAscender() & validateDescender() & validateCapHeight() & validateXHeight()
                & validateDefaultWidth();
        fireChangeEvent(this, valid);
    }

    private boolean validateAscender() {
        // ascender <= canvasHeight - descender
        final var validAscender = getValue(descender) <= getValue(canvasWidth) - getValue(ascender);
        ascender.putClientProperty(
                FlatClientProperties.OUTLINE,
                validAscender ? null : FlatClientProperties.OUTLINE_ERROR
        );
        return validAscender;
    }

    private boolean validateCapHeight() {
        // capHeight <= ascender
        final var validCapHeight = getValue(capHeight) <= getValue(ascender);
        capHeight.putClientProperty(
                FlatClientProperties.OUTLINE,
                validCapHeight ? null : FlatClientProperties.OUTLINE_ERROR
        );
        return validCapHeight;
    }

    private boolean validateDefaultWidth() {
        // defaultWidth <= canvasWidth
        final var validDefaultWidth = getValue(defaultCharacterWidth) <= getValue(canvasWidth);
        defaultCharacterWidth.putClientProperty(
                FlatClientProperties.OUTLINE,
                validDefaultWidth ? null : FlatClientProperties.OUTLINE_ERROR
        );
        return validDefaultWidth;
    }

    private boolean validateDescender() {
        // descender <= canvasHeight
        final var validDescender = getValue(descender) <= getValue(canvasWidth);
        descender.putClientProperty(
                FlatClientProperties.OUTLINE,
                validDescender ? null : FlatClientProperties.OUTLINE_ERROR
        );
        return validDescender;
    }

    private boolean validateXHeight() {
        // xHeight <= capHeight
        final var validXHeight = getValue(xHeight) <= getValue(capHeight);
        xHeight.putClientProperty(
                FlatClientProperties.OUTLINE,
                validXHeight ? null : FlatClientProperties.OUTLINE_ERROR
        );
        return validXHeight;
    }

    public interface InputChangeListener extends ChangeListener<MetricsPanel, Boolean> {
        // Empty
    }
}
