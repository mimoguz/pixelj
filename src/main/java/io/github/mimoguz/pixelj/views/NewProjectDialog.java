package io.github.mimoguz.pixelj.views;

import io.github.mimoguz.pixelj.actions.Actions;
import io.github.mimoguz.pixelj.actions.ApplicationAction;
import io.github.mimoguz.pixelj.models.Metrics;
import io.github.mimoguz.pixelj.models.Project;
import io.github.mimoguz.pixelj.models.SortedList;
import io.github.mimoguz.pixelj.resources.Icons;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.util.ChangeableBoolean;
import io.github.mimoguz.pixelj.views.shared.Borders;
import io.github.mimoguz.pixelj.views.shared.Components;
import io.github.mimoguz.pixelj.views.shared.Dimensions;
import io.github.mimoguz.pixelj.views.shared.MetricsPanel;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyEvent;

public class NewProjectDialog extends JDialog {
    private final JButton createButton = new JButton(Resources.get().getString("create"));
    private final JTextField titleField = new JTextField();
    private Project project;

    public NewProjectDialog(final Frame owner) {
        super(
                owner,
                Resources.get().getString("newProjectDialogTitle"),
                Dialog.ModalityType.APPLICATION_MODAL
        );

        final var res = Resources.get();
        final var content = new JPanel(new BorderLayout(Dimensions.SMALL_PADDING, Dimensions.LARGE_PADDING * 2));
        final var metricsPanel = new MetricsPanel(Metrics.Builder.getDefault().build(), true);
        final var cancelButton = new JButton(res.getString("cancel"));

        final var validTitle = new ChangeableBoolean(false);
        final ChangeableBoolean.Listener validTitleListener = (sender, valid) -> titleField.putClientProperty(
                FlatClientProperties.OUTLINE,
                valid ? null : FlatClientProperties.OUTLINE_ERROR
        );
        validTitle.addChangeListener(validTitleListener);

        final var canCreate = metricsPanel.validProperty().and(validTitle);
        canCreate.addChangeListener((sender, valid) -> createButton.setEnabled(valid));

        final var helpAction = new ApplicationAction(
                "metricsDialogHelpAction",
                (event, action) -> {
                }
        ).setIcon(Icons.HELP, res.colors.icon(), res.colors.disabledIcon()).setAccelerator(KeyEvent.VK_F1, 0);
        final var helpButton = new JButton(helpAction);

        createButton.addActionListener(e -> {
            final var title = titleField.getText().trim();
            if (metricsPanel.isMetricsValid() && !title.isBlank()) {
                project = new Project(title, new SortedList<>(), new SortedList<>(), metricsPanel.getMetrics());
            }
            setVisible(false);
        });

        final MetricsPanel.InputChangeListener metricsChangeListener = (MetricsPanel sender, Boolean valid) -> {
            createButton.setEnabled(valid && !titleField.getText().trim().isBlank());
        };

        metricsPanel.addChangeListener(metricsChangeListener);
        cancelButton.addActionListener(e -> setVisible(false));
        titleField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(final DocumentEvent e) {
                check();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                check();
            }

            @Override
            public void changedUpdate(final DocumentEvent e) {
                check();
            }

            private void check() {
                validTitle.setValue(!titleField.getText().trim().isBlank());
            }
        });

        Actions.registerShortcuts(java.util.List.of(helpAction), content);
        Components.setFixedSize(createButton, Dimensions.TEXT_BUTTON_SIZE);
        Components.setFixedSize(cancelButton, Dimensions.TEXT_BUTTON_SIZE);
        helpButton.putClientProperty(
                FlatClientProperties.BUTTON_TYPE,
                FlatClientProperties.BUTTON_TYPE_BORDERLESS
        );

        content.setBorder(Borders.MEDIUM_EMPTY);

        final var titlePanel = new JPanel(new BorderLayout(0, Dimensions.MEDIUM_PADDING));
        final var titleLabel = new JLabel(res.getString("projectTitlePrompt"));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, Dimensions.SMALL_PADDING, 0));
        titleLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(titleField, BorderLayout.CENTER);
        content.add(titlePanel, BorderLayout.NORTH);

        final var centerPanel = new JPanel(new BorderLayout());
        final var metricsLabel = new JLabel(res.getString("metricsPanelTitle"));
        metricsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, Dimensions.SMALL_PADDING, 0));
        metricsLabel.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");
        centerPanel.add(metricsLabel, BorderLayout.NORTH);
        final var scroll = new JScrollPane(metricsPanel);
        scroll.setBorder(Borders.EMPTY);
        centerPanel.add(scroll, BorderLayout.CENTER);
        content.add(centerPanel, BorderLayout.CENTER);

        final var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(helpButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(createButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonPanel.add(cancelButton);
        buttonPanel.setBorder(Borders.EMPTY);
        content.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(content);
        getRootPane().setDefaultButton(cancelButton);
        cancelButton.requestFocus();

        pack();
//        setSize(new Dimension(300, 600));
        metricsPanel.doLayout();
        setResizable(false);
        setLocationRelativeTo(owner);
    }

    public Project getProject() {
        return project;
    }

    @Override
    public void setVisible(final boolean value) {
        if (value) {
            project = null;
            titleField.setText("");
            titleField.putClientProperty(FlatClientProperties.OUTLINE, FlatClientProperties.OUTLINE_ERROR);
            createButton.setEnabled(false);
        }
        super.setVisible(value);
    }
}
