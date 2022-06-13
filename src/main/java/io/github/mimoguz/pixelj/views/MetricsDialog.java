package io.github.mimoguz.pixelj.views;

import io.github.mimoguz.pixelj.actions.Actions;
import io.github.mimoguz.pixelj.actions.ApplicationAction;
import io.github.mimoguz.pixelj.models.Metrics;
import io.github.mimoguz.pixelj.resources.Icons;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.shared.Borders;
import io.github.mimoguz.pixelj.views.shared.Components;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MetricsDialog extends JDialog {
    private final JButton applyButton;
    private final JSpinner ascender;
    private final JSpinner canvasHeight;
    private final JSpinner canvasWidth;
    private final JSpinner capHeight;
    private final JSpinner defaultCharacterWidth;
    private final JSpinner descender;
    private final JCheckBox isMonospaced;
    private final JSpinner lineSpacing;
    private final JSpinner spaceSize;
    private final JSpinner spacing;
    private final JSpinner xHeight;
    private Metrics result;

    public MetricsDialog(final Frame owner) {
        super(owner, Resources.get().getString("metricsDialogTitle"), Dialog.ModalityType.APPLICATION_MODAL);

        final var logger = Logger.getLogger(this.getClass().getName());
        logger.addHandler(new ConsoleHandler());

        final var res = Resources.get();

        final var content = new JPanel();
        content.setLayout(new BorderLayout(8, 8));

        final var inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());

        final var cons = new GridBagConstraints();
        cons.insets = new Insets(4, 4, 4, 4);
        cons.anchor = GridBagConstraints.WEST;
        cons.weighty = 0.0;
        cons.weightx = 0.0;

        cons.gridx = 0;
        cons.gridy = 0;
        inputPanel.add(new JLabel(res.getString("metricsCanvasWidth")), cons);
        cons.gridx = 1;
        canvasWidth = getSpinner();
        inputPanel.add(canvasWidth, cons);
        canvasWidth.setEnabled(false);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        inputPanel.add(new JLabel(res.getString("metricsCanvasHeight")), cons);
        cons.gridx = 1;
        canvasHeight = getSpinner();
        inputPanel.add(canvasHeight, cons);
        canvasHeight.setEnabled(false);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        inputPanel.add(new JLabel(res.getString("metricsAscender")), cons);
        cons.gridx = 1;
        ascender = getSpinner();
        ascender.addChangeListener(this::onSpinnerChanged);
        inputPanel.add(ascender, cons);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        inputPanel.add(new JLabel(res.getString("metricsDescender")), cons);
        cons.gridx = 1;
        descender = getSpinner();
        descender.addChangeListener(this::onSpinnerChanged);
        inputPanel.add(descender, cons);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        inputPanel.add(new JLabel(res.getString("metricsCapHeight")), cons);
        cons.gridx = 1;
        capHeight = getSpinner();
        capHeight.addChangeListener(this::onSpinnerChanged);
        inputPanel.add(capHeight, cons);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        inputPanel.add(new JLabel(res.getString("metricsXHeight")), cons);
        cons.gridx = 1;
        xHeight = getSpinner();
        xHeight.addChangeListener(this::onSpinnerChanged);
        inputPanel.add(xHeight, cons);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        inputPanel.add(new JLabel(res.getString("metricsDefaultCharacterWidth")), cons);
        cons.gridx = 1;
        defaultCharacterWidth = getSpinner();
        defaultCharacterWidth.addChangeListener(this::onSpinnerChanged);
        inputPanel.add(defaultCharacterWidth, cons);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        inputPanel.add(new JLabel(res.getString("metricsCharacterSpacing")), cons);
        cons.gridx = 1;
        spacing = getSpinner(0);
        inputPanel.add(spacing, cons);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        inputPanel.add(new JLabel(res.getString("metricsSpaceSize")), cons);
        cons.gridx = 1;
        spaceSize = getSpinner(0);
        inputPanel.add(spaceSize, cons);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        inputPanel.add(new JLabel(res.getString("metricsLineSpacing")), cons);
        cons.gridx = 1;
        lineSpacing = getSpinner(0);
        inputPanel.add(lineSpacing, cons);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        inputPanel.add(new JLabel(res.getString("metricsIsMonospaced")), cons);
        cons.gridx = 1;
        isMonospaced = new JCheckBox();
        inputPanel.add(isMonospaced, cons);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        cons.weighty = 1.0;
        inputPanel.add(new JPanel(), cons);

        final var scroll = new JScrollPane(inputPanel);
        scroll.setBorder(Borders.EMPTY);
        content.add(scroll, BorderLayout.CENTER);

        applyButton = new JButton(res.getString("apply"));
        Components.setFixedSize(applyButton, Dimensions.TEXT_BUTTON_SIZE);
        applyButton.addActionListener(this::onApply);

        final var cancelButton = new JButton(res.getString("cancel"));
        Components.setFixedSize(cancelButton, Dimensions.TEXT_BUTTON_SIZE);
        cancelButton.addActionListener(e -> setVisible(false));

        final var helpAction = new ApplicationAction(
                "metricsDialogHelpAction",
                (event, action) -> logger.log(Level.INFO, "Metrics help")
        ).setIcon(Icons.HELP, res.colors.icon(), res.colors.disabledIcon()).setAccelerator(KeyEvent.VK_F1, 0);
        Actions.registerShortcuts(java.util.List.of(helpAction), content);
        final var helpButton = new JButton(helpAction);
        helpButton.putClientProperty(
                FlatClientProperties.BUTTON_TYPE,
                FlatClientProperties.BUTTON_TYPE_BORDERLESS
        );

        final var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(helpButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(applyButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonPanel.add(cancelButton);
        buttonPanel.setBorder(Borders.SMALL_EMPTY);
        content.add(buttonPanel, BorderLayout.SOUTH);

        content.setBorder(Borders.MEDIUM_EMPTY);

        setContentPane(content);
        getRootPane().setDefaultButton(cancelButton);

        setSize(300, 564);
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    public Metrics getResult() {
        return result;
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
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            result = null;
            setLocationRelativeTo(getOwner());
        }
        super.setVisible(visible);
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

    private void onApply(final ActionEvent e) {
        result = Metrics.Builder.getDefault()
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
        setVisible(false);
    }

    @SuppressWarnings({"java:S2178"})
    private void onSpinnerChanged(final ChangeEvent e) {
        // Do not short-circuit
        final var valid = validateAscender() & validateDescender() & validateCapHeight() & validateXHeight()
                & validateDefaultWidth();
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
}
