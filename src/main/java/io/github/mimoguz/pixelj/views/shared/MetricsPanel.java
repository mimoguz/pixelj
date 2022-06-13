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

import static javax.swing.GroupLayout.DEFAULT_SIZE;
import static javax.swing.GroupLayout.PREFERRED_SIZE;

public class MetricsPanel extends JPanel implements Changeable<MetricsPanel, Boolean, MetricsPanel.MetricsChangeListener> {
    private final JSpinner ascender;
    private final JSpinner canvasHeight;
    private final JSpinner canvasWidth;
    private final JSpinner capHeight;
    private final JSpinner defaultCharacterWidth;
    private final JSpinner descender;
    private final JCheckBox isMonospaced;
    private final JSpinner lineSpacing;
    private final EventListenerList listeners = new EventListenerList();
    private final JSpinner spaceSize;
    private final JSpinner spacing;
    private final JSpinner xHeight;
    private boolean valid;

    public MetricsPanel(final Metrics init) {
        setLayout(new BorderLayout());

        final var res = Resources.get();

        canvasHeight = getSpinner();
        canvasWidth = getSpinner();
        ascender = getSpinner();
        ascender.addChangeListener(this::onSpinnerChanged);
        descender = getSpinner();
        descender.addChangeListener(this::onSpinnerChanged);
        capHeight = getSpinner();
        capHeight.addChangeListener(this::onSpinnerChanged);
        xHeight = getSpinner();
        xHeight.addChangeListener(this::onSpinnerChanged);
        defaultCharacterWidth = getSpinner();
        defaultCharacterWidth.addChangeListener(this::onSpinnerChanged);
        spacing = getSpinner(0);
        spaceSize = getSpinner(0);
        lineSpacing = getSpinner(0);
        isMonospaced = new JCheckBox();

        final var canvasWidthLabel = new JLabel(res.getString("metricsCanvasWidth"));
        final var canvasHeightLabel = new JLabel(res.getString("metricsCanvasHeight"));
        final var ascenderLabel = new JLabel(res.getString("metricsAscender"));
        final var descenderLabel = new JLabel(res.getString("metricsDescender"));
        final var capHeightLabel = new JLabel(res.getString("metricsCapHeight"));
        final var xHeightLabel = new JLabel(res.getString("metricsXHeight"));
        final var characterWidthLabel = new JLabel(res.getString("metricsDefaultCharacterWidth"));
        final var spacingLabel = new JLabel(res.getString("metricsCharacterSpacing"));
        final var spaceSizeLabel = new JLabel(res.getString("metricsSpaceSize"));
        final var lineSpacingLabel = new JLabel(res.getString("metricsLineSpacing"));
        final var isMonospacedLabel = new JLabel(res.getString("metricsIsMonospaced"));

        final var inputPanel = new JPanel();
        final var layout = new GroupLayout(inputPanel);
        final var sw = Dimensions.SPINNER_SIZE.width;
        final var sh = Dimensions.SPINNER_SIZE.height;
        inputPanel.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(canvasWidthLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(canvasHeightLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(ascenderLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(descenderLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(capHeightLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(xHeightLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(characterWidthLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(spacingLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(spaceSizeLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(lineSpacingLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(isMonospacedLabel, PREFERRED_SIZE, DEFAULT_SIZE, PREFERRED_SIZE)
                        )
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(canvasHeight, sw, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(canvasWidth, sw, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(ascender, sw, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(descender, sw, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(capHeight, sw, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(xHeight, sw, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(defaultCharacterWidth, sw, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(spacing, sw, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(spaceSize, sw, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(lineSpacing, sw, DEFAULT_SIZE, PREFERRED_SIZE)
                                        .addComponent(isMonospaced, sw, DEFAULT_SIZE, PREFERRED_SIZE)
                        )
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                                .addComponent(capHeightLabel, sh, DEFAULT_SIZE, PREFERRED_SIZE)
                                .addComponent(canvasHeight, sh, DEFAULT_SIZE, PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                                .addComponent(canvasWidthLabel, sh, DEFAULT_SIZE, PREFERRED_SIZE)
                                .addComponent(canvasWidth, sh, DEFAULT_SIZE, PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                                .addComponent(ascenderLabel, sh, DEFAULT_SIZE, PREFERRED_SIZE)
                                .addComponent(ascender, sh, DEFAULT_SIZE, PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                                .addComponent(descenderLabel, sh, DEFAULT_SIZE, PREFERRED_SIZE)
                                .addComponent(descender, sh, DEFAULT_SIZE, PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                                .addComponent(capHeightLabel, sh, DEFAULT_SIZE, PREFERRED_SIZE)
                                .addComponent(canvasHeight, sh, DEFAULT_SIZE, PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                                .addComponent(xHeightLabel, sh, DEFAULT_SIZE, PREFERRED_SIZE)
                                .addComponent(xHeight, sh, DEFAULT_SIZE, PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                                .addComponent(characterWidthLabel, sh, DEFAULT_SIZE, PREFERRED_SIZE)
                                .addComponent(defaultCharacterWidth, sh, DEFAULT_SIZE, PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                                .addComponent(spacingLabel, sh, DEFAULT_SIZE, PREFERRED_SIZE)
                                .addComponent(spacing, sh, DEFAULT_SIZE, PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                                .addComponent(spaceSizeLabel, sh, DEFAULT_SIZE, PREFERRED_SIZE)
                                .addComponent(spaceSize, sh, DEFAULT_SIZE, PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                                .addComponent(lineSpacingLabel, sh, DEFAULT_SIZE, PREFERRED_SIZE)
                                .addComponent(lineSpacing, sh, DEFAULT_SIZE, PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER, false)
                                .addComponent(isMonospacedLabel, sh, DEFAULT_SIZE, PREFERRED_SIZE)
                                .addComponent(isMonospaced, sh, DEFAULT_SIZE, PREFERRED_SIZE))
        );

        final var scroll = new JScrollPane(inputPanel);
        scroll.setBorder(Borders.EMPTY);
        add(scroll, BorderLayout.CENTER);

        set(init);
    }

    public void get() {
        Metrics.Builder.getDefault()
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
    public Class<MetricsChangeListener> getListenerClass() {
        return MetricsChangeListener.class;
    }

    @Override
    public EventListenerList getListenerList() {
        return listeners;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    public void set(final Metrics metrics) {
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
        valid = validateAscender() & validateDescender() & validateCapHeight() & validateXHeight()
                & validateDefaultWidth();
        fireChangeEvent(this, valid);
    }

    private static JSpinner getSpinner() {
        return getSpinner(1);
    }

    private static JSpinner getSpinner(final int minimum) {
        return new JSpinner(new SpinnerNumberModel(minimum, minimum, 512, 1));
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

    public interface MetricsChangeListener extends ChangeListener<MetricsPanel, Boolean> {
        // Empty
    }
}
