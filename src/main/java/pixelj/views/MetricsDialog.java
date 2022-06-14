package pixelj.views;

import pixelj.actions.Actions;
import pixelj.actions.ApplicationAction;
import pixelj.models.Metrics;
import pixelj.resources.Icons;
import pixelj.resources.Resources;
import pixelj.util.ChangeableBoolean;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;
import pixelj.views.shared.MetricsPanel;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MetricsDialog extends JDialog {
    private final JButton applyButton;

    private final MetricsPanel inputPanel;
    private Metrics result;

    public MetricsDialog(final Frame owner) {
        super(owner, Resources.get().getString("metricsDialogTitle"), Dialog.ModalityType.APPLICATION_MODAL);

        final var logger = Logger.getLogger(this.getClass().getName());
        logger.addHandler(new ConsoleHandler());

        inputPanel = new MetricsPanel(Metrics.getDefault(), false);

        final var res = Resources.get();

        applyButton = new JButton(res.getString("apply"));
        Components.setFixedSize(applyButton, Dimensions.TEXT_BUTTON_SIZE);
        applyButton.addActionListener(this::onApply);
        final ChangeableBoolean.Listener validationListener = (sender, args) -> applyButton.setEnabled(args);
        inputPanel.metricsValidProperty().addChangeListener(validationListener);

        final var cancelButton = new JButton(res.getString("cancel"));
        Components.setFixedSize(cancelButton, Dimensions.TEXT_BUTTON_SIZE);
        cancelButton.addActionListener(e -> setVisible(false));

        final var content = new JPanel();
        final var contentLayout = new BorderLayout(Dimensions.MEDIUM_PADDING, Dimensions.LARGE_PADDING * 2);
        content.setLayout(contentLayout);
        content.setBorder(Borders.LARGE_EMPTY);

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

        final var scroll = new JScrollPane(inputPanel);
        scroll.setBorder(Borders.EMPTY);
        scroll.setAlignmentX(0.5f);
        content.add(scroll, BorderLayout.CENTER);

        final var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(helpButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(applyButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonPanel.add(cancelButton);
        buttonPanel.setBorder(Borders.EMPTY);
        content.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(content);
        getRootPane().setDefaultButton(cancelButton);

        pack();
        inputPanel.doLayout();
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    public Metrics getResult() {
        return result;
    }

    public void set(final Metrics metrics) {
        inputPanel.setMetrics(metrics, false);
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            result = null;
            setLocationRelativeTo(getOwner());
        }
        super.setVisible(visible);
    }

    private void onApply(final ActionEvent e) {
        try {
            result = inputPanel.getMetrics();
        } catch (Metrics.ValidatedBuilder.InvalidStateException exception) {
            result = null;
        }
        setVisible(false);
    }
}
