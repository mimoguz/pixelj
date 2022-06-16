package pixelj.views;

import pixelj.actions.Actions;
import pixelj.actions.ApplicationAction;
import pixelj.models.Metrics;
import pixelj.models.Project;
import pixelj.models.SortedList;
import pixelj.resources.Icons;
import pixelj.resources.Resources;
import pixelj.util.ChangeableBoolean;
import pixelj.util.ReadOnlyBoolean;
import pixelj.views.shared.Borders;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;
import pixelj.views.shared.MetricsPanel;

import com.formdev.flatlaf.FlatClientProperties;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyEvent;

public class NewProjectDialog extends JDialog {
    private static final Metrics DEFAULT_METRICS = Metrics.getDefault();
    final MetricsPanel metricsPanel = new MetricsPanel(DEFAULT_METRICS, true);
    private final JButton createButton = new JButton(Resources.get().getString("create"));
    private final JTextField titleField = new JTextField();
    private Project project;

    public NewProjectDialog(final Frame owner) {
        super(
                owner,
                Resources.get().getString("newProjectDialogTitle"),
                Dialog.ModalityType.APPLICATION_MODAL
        );

        final var cancelButton = new JButton(Resources.get().getString("cancel"));
        final var helpButton = new JButton();
        final var isTitleValid = getTitleValid();
        final var areInputsValid = metricsPanel.metricsValidProperty().and(isTitleValid);

        final var content = new JPanel(
                new BorderLayout(Dimensions.SMALL_PADDING, Dimensions.LARGE_PADDING * 2)
        );
        content.setBorder(Borders.LARGE_EMPTY);

        setupCreateButton(metricsPanel, areInputsValid);
        setupHelpButton(content, helpButton);
        setupCancelButton(cancelButton);
        setupTitlePanel(content, isTitleValid);
        setupCenterPanel(content, metricsPanel);
        setupButtonPanel(content, cancelButton, helpButton, areInputsValid);

        setContentPane(content);
        getRootPane().setDefaultButton(createButton);
        cancelButton.requestFocus();
        pack();
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
            metricsPanel.setMetrics(DEFAULT_METRICS, true);
            titleField.putClientProperty(FlatClientProperties.OUTLINE, FlatClientProperties.OUTLINE_ERROR);
            createButton.setEnabled(false);
        }
        super.setVisible(value);
    }

    private ChangeableBoolean getTitleValid() {
        final var validTitle = new ChangeableBoolean(false);
        titleField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(final DocumentEvent e) {
                check();
            }

            @Override
            public void insertUpdate(final DocumentEvent e) {
                check();
            }

            @Override
            public void removeUpdate(final DocumentEvent e) {
                check();
            }

            private void check() {
                validTitle.setValue(!titleField.getText().trim().isBlank());
            }
        });
        return validTitle;
    }

    private void setupButtonPanel(
            final JPanel content,
            final JButton cancelButton,
            final JButton helpButton,
            ReadOnlyBoolean canCreate
    ) {
        canCreate.addChangeListener((sender, valid) -> createButton.setEnabled(valid));

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
    }

    private void setupCancelButton(final JButton cancelButton) {
        cancelButton.addActionListener(e -> setVisible(false));
        Components.setFixedSize(cancelButton, Dimensions.TEXT_BUTTON_SIZE);
    }

    private void setupCenterPanel(final JPanel content, final MetricsPanel metricsPanel) {
        final var centerPanel = new JPanel(new BorderLayout(0, Dimensions.MEDIUM_PADDING));

        final var metricsLabel = new JLabel(Resources.get().getString("metricsPanelTitle"));
        setupPanelTitle(metricsLabel);
        centerPanel.add(metricsLabel, BorderLayout.NORTH);

        final var scroll = new JScrollPane(metricsPanel);
        scroll.setBorder(Borders.EMPTY);
        centerPanel.add(scroll, BorderLayout.CENTER);
        content.add(centerPanel, BorderLayout.CENTER);
    }

    private void setupCreateButton(final MetricsPanel metricsPanel, ReadOnlyBoolean areInputsValid) {
        createButton.addActionListener(e -> {
            final var title = titleField.getText().trim();
            try {
                project = new Project(
                        title,
                        new SortedList<>(),
                        new SortedList<>(),
                        metricsPanel.getMetrics()
                );
            } catch (Metrics.ValidatedBuilder.InvalidStateException exception) {
                project = null;
            }
            setVisible(false);
        });

        Components.setFixedSize(createButton, Dimensions.TEXT_BUTTON_SIZE);
    }

    private void setupHelpButton(final JPanel content, final JButton helpButton) {
        final var res = Resources.get();
        final var helpAction = new ApplicationAction("metricsDialogHelpAction", (event, action) -> {
        }).setIcon(Icons.HELP, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_F1, 0);
        helpButton.setAction(helpAction);
        helpButton.putClientProperty(
                FlatClientProperties.BUTTON_TYPE,
                FlatClientProperties.BUTTON_TYPE_BORDERLESS
        );
        Actions.registerShortcuts(java.util.List.of(helpAction), content);
    }

    private void setupTitlePanel(final JPanel content, ChangeableBoolean validTitle) {
        final var titlePanel = new JPanel(new BorderLayout(0, Dimensions.MEDIUM_PADDING));
        final var titleLabel = new JLabel(Resources.get().getString("projectTitlePrompt"));
        setupPanelTitle(titleLabel);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(titleField, BorderLayout.CENTER);

        validTitle.addChangeListener(
                (sender, valid) -> titleField.putClientProperty(
                        FlatClientProperties.OUTLINE,
                        valid ? null : FlatClientProperties.OUTLINE_ERROR
                )
        );

        content.add(titlePanel, BorderLayout.NORTH);
    }

    private void setupPanelTitle(final JLabel title) {
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, Dimensions.SMALL_PADDING, 0));
        Components.addOuterBorder(
                title,
                BorderFactory.createMatteBorder(0, 0, 1, 0, Resources.get().colors.divider())
        );
        title.putClientProperty(FlatClientProperties.STYLE_CLASS, "h4");
    }
}
