package io.github.mimoguz.pixelj.views.preview_screen;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.*;

import io.github.mimoguz.pixelj.actions.Actions;
import io.github.mimoguz.pixelj.actions.PreviewScreenActions;
import io.github.mimoguz.pixelj.models.ProjectModel;
import io.github.mimoguz.pixelj.util.Detachable;
import io.github.mimoguz.pixelj.views.shared.Borders;
import io.github.mimoguz.pixelj.views.shared.Components;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

public class PreviewScreen extends JPanel implements Detachable {
    private static final long serialVersionUID = -4480174487039009081L;

    private final transient PreviewScreenActions actions;
    private final JButton clearButton;
    private final transient ProjectModel project;
    private final transient ProjectModel.ProjectChangeListener projectChangeListener;
    private final JButton refreshButton;
    private final JTextArea textInput;

    public PreviewScreen(final ProjectModel project, final JComponent root) {
        this.project = project;

        textInput = new JTextArea();
        textInput.setMinimumSize(new Dimension(200, 120));
        textInput.setPreferredSize(new Dimension(600, 120));
        textInput.setMaximumSize(new Dimension(800, 120));

        final var container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        actions = new PreviewScreenActions(project, textInput, container);

        refreshButton = new JButton();
        refreshButton.setAction(actions.refreshAction);
        Components.setFixedSize(refreshButton, Dimensions.textButtonSize);

        clearButton = new JButton();
        clearButton.setAction(actions.clearAction);
        Components.setFixedSize(clearButton, Dimensions.textButtonSize);

        Actions.registerShortcuts(actions.all, root);

        projectChangeListener = (sender, event) -> {
            if (event instanceof ProjectModel.ProjectChangeEvent.MetricsChanged) {
                actions.refreshAction
                .actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
            }
        };

        setLayout(new BoxLayout(this ,BoxLayout.Y_AXIS));

        final var topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBorder(Borders.smallEmptyCup);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(textInput);
        topPanel.add(Box.createRigidArea(Dimensions.smallSquare));
        final var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(refreshButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(Box.createVerticalGlue());
        topPanel.add(buttonPanel);
        topPanel.add(Box.createHorizontalGlue());
        add(topPanel);

        final var previewPanel = new JPanel(new GridBagLayout());
        previewPanel.add(container);
        final var scrollPanel = new JScrollPane(previewPanel);
        scrollPanel.setMaximumSize(Dimensions.maximum);
        scrollPanel.setBorder(Borders.smallEmptyCup);
        add(scrollPanel);
    }

    @Override
    public void detach() {
        project.removeChangeListener(projectChangeListener);
    }

    public JButton getClearButton() {
        return clearButton;
    }

    public JButton getRefreshButton() {
        return refreshButton;
    }

    public JTextArea getTextInput() {
        return textInput;
    }

    @Override
    public void setEnabled(final boolean value) {
        super.setEnabled(value);
        textInput.setEnabled(value);
        Actions.setEnabled(actions.all, value);
    }
}
