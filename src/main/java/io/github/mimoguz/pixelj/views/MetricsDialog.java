package io.github.mimoguz.pixelj.views;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;

import com.formdev.flatlaf.FlatClientProperties;

import io.github.mimoguz.pixelj.models.Metrics;
import io.github.mimoguz.pixelj.resources.Resources;

public class MetricsDialog extends JFrame {
    private static final long serialVersionUID = 1185992736696485089L;

    private static int getValue(JSpinner spinner) {
        return ((SpinnerNumberModel) spinner.getModel()).getNumber().intValue();
    }

    private final JButton applyButton;
    private final JSpinner ascender;
    private final JSpinner canvasHeight;
    private final JSpinner canvasWidth;
    private final JSpinner capHeight;
    private final JSpinner defaultWidth;
    private final JSpinner descender;
    private final JCheckBox isMonospaced;

    private final JSpinner xHeight;

    public MetricsDialog(final Metrics source) {
        super();

        final var res = Resources.get();

        final var root = new JPanel();
        root.setLayout(new BorderLayout(8, 8));

        final var content = new JPanel();
        content.setLayout(new GridBagLayout());

        final var cons = new GridBagConstraints();
        cons.insets = new Insets(4, 4, 4, 4);

        cons.gridx = 0;
        cons.gridy = 0;
        content.add(new JLabel(res.getString("metricsCanvasWidth")), cons);
        cons.gridx = 1;
        canvasWidth = new JSpinner(new SpinnerNumberModel(source.canvasWidth(), 1, 512, 1));
        content.add(canvasWidth, cons);
        canvasWidth.setEnabled(false);

        cons.gridx = 0;
        cons.gridy = 1;
        content.add(new JLabel(res.getString("metricsCanvasHeight")), cons);
        cons.gridx = 1;
        canvasHeight = new JSpinner(new SpinnerNumberModel(source.canvasWidth(), 1, 512, 1));
        content.add(canvasHeight, cons);
        canvasHeight.setEnabled(false);

        cons.gridx = 0;
        cons.gridy = 2;
        content.add(new JLabel(res.getString("metricsAscender")), cons);
        cons.gridx = 1;
        ascender = new JSpinner(new SpinnerNumberModel(source.canvasWidth(), 1, 512, 1));
        ascender.addChangeListener(this::onSpinnerChanged);
        content.add(ascender, cons);

        cons.gridx = 0;
        cons.gridy = 3;
        content.add(new JLabel(res.getString("metricsDescender")), cons);
        cons.gridx = 1;
        descender = new JSpinner(new SpinnerNumberModel(source.canvasWidth(), 1, 512, 1));
        content.add(descender, cons);

        cons.gridx = 0;
        cons.gridy = 4;
        content.add(new JLabel(res.getString("metricsCapHeight")), cons);
        cons.gridx = 1;
        capHeight = new JSpinner(new SpinnerNumberModel(source.canvasWidth(), 1, 512, 1));
        capHeight.addChangeListener(this::onSpinnerChanged);
        content.add(capHeight, cons);

        cons.gridx = 0;
        cons.gridy = 5;
        content.add(new JLabel(res.getString("metricsXHeight")), cons);
        cons.gridx = 1;
        xHeight = new JSpinner(new SpinnerNumberModel(source.canvasWidth(), 1, 512, 1));
        xHeight.addChangeListener(this::onSpinnerChanged);
        content.add(xHeight, cons);

        cons.gridx = 0;
        cons.gridy = 6;
        content.add(new JLabel(res.getString("metricsDefaultCharacterWidth")), cons);
        cons.gridx = 1;
        defaultWidth = new JSpinner(new SpinnerNumberModel(source.canvasWidth(), 1, 512, 1));
        defaultWidth.addChangeListener(this::onSpinnerChanged);
        content.add(defaultWidth, cons);

        cons.gridx = 0;
        cons.gridy = 7;
        content.add(new JLabel(res.getString("metricsIsMonospaced")), cons);
        cons.gridx = 1;
        isMonospaced = new JCheckBox();
        isMonospaced.setSelected(source.isMonospaced());
        content.add(isMonospaced, cons);

        root.add(content, BorderLayout.CENTER);

        applyButton = new JButton(res.getString("apply"));
    }

    private void onSpinnerChanged(ChangeEvent e) {
        final var valid = validateAscender() && validateDescender() && validateCapHeight()
                && validateXHeight() && validateDefaultWidth();
        applyButton.setEnabled(valid);
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
        final var validDefaultWidth = getValue(defaultWidth) <= getValue(canvasWidth);
        defaultWidth.putClientProperty(
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
}
