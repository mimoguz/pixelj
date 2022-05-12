package io.github.mimoguz.pixelj.views;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;

import com.formdev.flatlaf.FlatClientProperties;

import io.github.mimoguz.pixelj.models.Metrics;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.shared.Borders;
import io.github.mimoguz.pixelj.views.shared.Components;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

public class MetricsDialog extends JDialog {
    private static final long serialVersionUID = 1185992736696485089L;

    private static JSpinner getSpinner(final int value) {
        return getSpinner(value, 1);
    }

    private static JSpinner getSpinner(final int value, final int minimum) {
        final var spinner = new JSpinner(new SpinnerNumberModel(value, minimum, 512, 1));
        Components.setFixedSize(spinner, Dimensions.SPINNER_SIZE);
        return spinner;
    }

    private static int getValue(JSpinner spinner) {
        return ((SpinnerNumberModel) spinner.getModel()).getNumber().intValue();
    }

    private final JButton applyButton;
    private final JSpinner ascender;
    private final JSpinner canvasHeight;
    private final JSpinner canvasWidth;
    private final JSpinner capHeight;
    private final JSpinner defaultCharacterWidth;
    private final JSpinner descender;
    private final JCheckBox isMonospaced;
    private final JSpinner lineSpacing;
    private transient Metrics result;
    private final JSpinner spaceSize;
    private final JSpinner spacing;
    private final JSpinner xHeight;

    public MetricsDialog(final Metrics source, final Frame owner) {
        super(owner, Resources.get().getString("metricsDialogTitle"), Dialog.ModalityType.APPLICATION_MODAL);

        final var res = Resources.get();

        final var root = new JPanel();
        root.setLayout(new BorderLayout(8, 8));

        final var content = new JPanel();
        content.setLayout(new GridBagLayout());

        final var cons = new GridBagConstraints();
        cons.insets = new Insets(4, 4, 4, 4);
        cons.anchor = GridBagConstraints.WEST;
        cons.weighty = 0.0;
        cons.weightx = 0.0;

        cons.gridx = 0;
        cons.gridy = 0;
        content.add(new JLabel(res.getString("metricsCanvasWidth")), cons);
        cons.gridx = 1;
        canvasWidth = getSpinner(source.canvasWidth());
        content.add(canvasWidth, cons);
        canvasWidth.setEnabled(false);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        content.add(new JLabel(res.getString("metricsCanvasHeight")), cons);
        cons.gridx = 1;
        canvasHeight = getSpinner(source.canvasHeight());
        content.add(canvasHeight, cons);
        canvasHeight.setEnabled(false);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        content.add(new JLabel(res.getString("metricsAscender")), cons);
        cons.gridx = 1;
        ascender = getSpinner(source.ascender());
        ascender.addChangeListener(this::onSpinnerChanged);
        content.add(ascender, cons);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        content.add(new JLabel(res.getString("metricsDescender")), cons);
        cons.gridx = 1;
        descender = getSpinner(source.descender());
        content.add(descender, cons);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        content.add(new JLabel(res.getString("metricsCapHeight")), cons);
        cons.gridx = 1;
        capHeight = getSpinner(source.capHeight());
        capHeight.addChangeListener(this::onSpinnerChanged);
        content.add(capHeight, cons);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        content.add(new JLabel(res.getString("metricsXHeight")), cons);
        cons.gridx = 1;
        xHeight = getSpinner(source.xHeight());
        xHeight.addChangeListener(this::onSpinnerChanged);
        content.add(xHeight, cons);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        content.add(new JLabel(res.getString("metricsDefaultCharacterWidth")), cons);
        cons.gridx = 1;
        defaultCharacterWidth = getSpinner(source.defaultCharacterWidth());
        defaultCharacterWidth.addChangeListener(this::onSpinnerChanged);
        content.add(defaultCharacterWidth, cons);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        content.add(new JLabel(res.getString("metricsCharacterSpacing")), cons);
        cons.gridx = 1;
        spacing = getSpinner(source.spacing(), 0);
        content.add(spacing, cons);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        content.add(new JLabel(res.getString("metricsSpaceSize")), cons);
        cons.gridx = 1;
        spaceSize = getSpinner(source.spaceSize(), 0);
        content.add(spaceSize, cons);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        content.add(new JLabel(res.getString("metricsLineSpacing")), cons);
        cons.gridx = 1;
        lineSpacing = getSpinner(source.lineSpacing(), 0);
        content.add(lineSpacing, cons);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        ;
        content.add(new JLabel(res.getString("metricsIsMonospaced")), cons);
        cons.gridx = 1;
        isMonospaced = new JCheckBox();
        isMonospaced.setSelected(source.isMonospaced());
        content.add(isMonospaced, cons);

        cons.gridx = 0;
        cons.gridy = cons.gridy + 1;
        cons.weighty = 1.0;
        content.add(new JPanel(), cons);

        final var scroll = new JScrollPane(content);
        scroll.setBorder(Borders.empty);
        root.add(scroll, BorderLayout.CENTER);

        applyButton = new JButton(res.getString("apply"));
        Components.setFixedSize(applyButton, Dimensions.TEXT_BUTTON_SIZE);
        applyButton.addActionListener(e -> {
            result = Metrics.Builder.from(source)
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
        });

        final var cancelButton = new JButton(res.getString("cancel"));
        Components.setFixedSize(cancelButton, Dimensions.TEXT_BUTTON_SIZE);
        cancelButton.addActionListener(e -> setVisible(false));

        final var infoPopup = new JPopupMenu();
        final var infoItem = new JLabel();
        infoItem.setIcon(res.metricsGuide);
        infoItem.setBorder(Borders.mediumEmpty);
        infoPopup.add(infoItem);
        final var infoButton = new JButton();
        infoButton.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_HELP);
        infoButton
                .addActionListener(
                        e -> infoPopup.show(
                                infoButton,
                                0,
                                -res.metricsGuide.getIconHeight() - infoButton.getHeight()
                                        - Dimensions.PADDING
                        )
                );

        final var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(infoButton);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonPanel.add(applyButton);
        buttonPanel.setBorder(Borders.smallEmpty);
        root.add(buttonPanel, BorderLayout.SOUTH);

        root.setBorder(Borders.mediumEmpty);

        setContentPane(root);
        getRootPane().setDefaultButton(cancelButton);

        setSize(300, 564);
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    public Metrics getResult() {
        return result;
    }

    @SuppressWarnings("java:S2178")
    private void onSpinnerChanged(ChangeEvent e) {
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
