package pixelj.views.projectwindow;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import com.formdev.flatlaf.FlatClientProperties;

import pixelj.resources.Resources;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

abstract class ExportDialogBase extends JDialog {

    protected final JButton exportButton = new JButton(Resources.get().getString("export"));
    protected final JButton cancelButton = new JButton(Resources.get().getString("cancel"));
    protected final JComboBox<LayoutStrategy> layoutStrategyIn = new JComboBox<>();
    protected final JSpinner widthIn = new JSpinner();
    protected final JSpinner heightIn = new JSpinner();

    ExportDialogBase(final Frame owner) {
        super(owner, Resources.get().getString("exportDialogTitle"), Dialog.ModalityType.APPLICATION_MODAL);

        Components.setFixedSize(exportButton, Dimensions.TEXT_BUTTON_SIZE);
        Components.setFixedSize(cancelButton, Dimensions.TEXT_BUTTON_SIZE);
        Components.setFixedSize(widthIn, Dimensions.SPINNER_SIZE);
        Components.setFixedSize(heightIn, Dimensions.SPINNER_SIZE);
        layoutStrategyIn.setPreferredSize(new Dimension(120, layoutStrategyIn.getPreferredSize().height));

        final var content = new JPanel(new BorderLayout());

        final var inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(Borders.LARGE_EMPTY);
        final var res = Resources.get();
        final var cons = new GridBagConstraints();
        final var rowHeightDelta = Dimensions.SPINNER_SIZE.height 
                - layoutStrategyIn.getPreferredSize().height;
        cons.fill = GridBagConstraints.NONE;

        cons.gridx = 0;
        cons.gridy = 0;
        cons.weightx = 1.0;
        cons.anchor = GridBagConstraints.LINE_START;
        cons.insets = new Insets(0, 0, Dimensions.SMALL_PADDING + rowHeightDelta, Dimensions.MEDIUM_PADDING);
        inputPanel.add(new JLabel(res.getString("layoutStrategy")), cons);
        cons.insets = new Insets(0, 0, Dimensions.SMALL_PADDING, Dimensions.MEDIUM_PADDING);
        cons.gridy = GridBagConstraints.RELATIVE;
        inputPanel.add(new JLabel(res.getString("exportImageWidth")), cons);
        inputPanel.add(new JLabel(res.getString("exportImageHeight")), cons);

        cons.gridx = 1;
        cons.gridy = 0;
        cons.weightx = 0.0;
        cons.anchor = GridBagConstraints.LINE_END;
        cons.insets = new Insets(0, Dimensions.MEDIUM_PADDING, Dimensions.SMALL_PADDING + rowHeightDelta, 0);
        inputPanel.add(layoutStrategyIn, cons);
        cons.insets = new Insets(0, Dimensions.MEDIUM_PADDING, Dimensions.SMALL_PADDING, 0);
        cons.gridy = GridBagConstraints.RELATIVE;
        inputPanel.add(widthIn, cons);
        inputPanel.add(heightIn, cons);

        content.add(inputPanel, BorderLayout.NORTH);

        final var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(Borders.LARGE_EMPTY);
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(exportButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonPanel.add(cancelButton);
        content.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(content);
        getRootPane().setDefaultButton(exportButton);
        getRootPane().putClientProperty(FlatClientProperties.TITLE_BAR_SHOW_ICON, false);
        pack();
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            setLocationRelativeTo(getOwner());
        }
        super.setVisible(visible);
    }
}
