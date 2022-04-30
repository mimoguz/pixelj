package io.github.mimoguz.pixelj.views.preview_screen;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.*;

import com.formdev.flatlaf.FlatClientProperties;

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
        textInput.setMaximumSize(Dimensions.maximum);

        final var container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        actions = new PreviewScreenActions(project, textInput, container);

        refreshButton = new JButton();
        refreshButton.setAction(actions.refreshAction);
        Components.setFixedSize(refreshButton, Dimensions.textButtonSize);
        refreshButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "textButton");

        clearButton = new JButton();
        clearButton.setAction(actions.clearAction);
        Components.setFixedSize(clearButton, Dimensions.textButtonSize);
        clearButton.putClientProperty(FlatClientProperties.STYLE_CLASS, "textButton");

        Actions.registerShortcuts(actions.all, root);

        projectChangeListener = (sender, event) -> {
            if (event instanceof ProjectModel.ProjectChangeEvent.MetricsChanged) {
                actions.refreshAction
                        .actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
            }
        };

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final var previewPanel = new JPanel(new GridBagLayout());
        previewPanel.add(container);
        final var scrollPanel = new JScrollPane(previewPanel);
        scrollPanel.setMaximumSize(Dimensions.maximum);
        scrollPanel.setBorder(Borders.smallEmptyCenter);
        add(scrollPanel);

        final var inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setMinimumSize(new Dimension(200, 144));
        inputPanel.setPreferredSize(new Dimension(600, 144));
        inputPanel.setMaximumSize(new Dimension(800, 144));
        inputPanel.add(textInput);
        inputPanel.add(Box.createRigidArea(Dimensions.smallSquare));
        final var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(refreshButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.smallSquare));
        buttonPanel.add(clearButton);
        inputPanel.add(buttonPanel);
        final var topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBorder(Borders.smallEmptyCupCenter);
        topPanel.add(Box.createHorizontalGlue());
        topPanel.add(inputPanel);
        topPanel.add(Box.createHorizontalGlue());
        add(topPanel);
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
