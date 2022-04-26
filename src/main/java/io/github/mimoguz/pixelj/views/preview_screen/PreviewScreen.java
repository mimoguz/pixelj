package io.github.mimoguz.pixelj.views.preview_screen;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import io.github.mimoguz.pixelj.actions.Actions;
import io.github.mimoguz.pixelj.actions.PreviewScreenActions;
import io.github.mimoguz.pixelj.models.ProjectModel;
import io.github.mimoguz.pixelj.util.Detachable;
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
        final var container = new JPanel();
        actions = new PreviewScreenActions(project, textInput, container);

        refreshButton = new JButton();
        refreshButton.setAction(actions.refreshAction);
        Components.setFixedSize(refreshButton, Dimensions.textButtonSize);

        clearButton = new JButton();
        clearButton.setAction(actions.clearAction);
        Components.setFixedSize(clearButton, Dimensions.textButtonSize);

        Actions.registerShortcuts(actions.all, root);

        projectChangeListener = (sender, event) -> {
            if (event instanceof ProjectModel.ProjectChangeEvent.MetricsChanged metricsChanged) {
                actions.refreshAction
                        .actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
            }
        };

        final var layout = new GridBagLayout();
        setLayout(layout);

        final var constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 2;
        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
        constraints.ipadx = 8;
        constraints.ipady = 8;
        add(textInput, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        constraints.weightx = 0.0;
        add(refreshButton, constraints);

        constraints.gridy = 1;
        add(clearButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.weighty = 1.0;
        constraints.ipadx = 0;
        constraints.ipady = 0;
        final var scrollPanel = new JScrollPane(container);
        add(scrollPanel, constraints);
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
        Actions.setEnabled(actions.all, value);
    }
}
