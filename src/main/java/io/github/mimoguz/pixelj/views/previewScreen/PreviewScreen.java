package io.github.mimoguz.pixelj.views.previewScreen;

import io.github.mimoguz.pixelj.actions.Actions;
import io.github.mimoguz.pixelj.actions.PreviewScreenActions;
import io.github.mimoguz.pixelj.models.ProjectModel;
import io.github.mimoguz.pixelj.util.Detachable;
import io.github.mimoguz.pixelj.views.shared.Components;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;
import java.awt.event.ActionEvent;

@ParametersAreNonnullByDefault
public class PreviewScreen extends JPanel implements Detachable {
    private final PreviewScreenActions actions;
    private final JButton clearButton;
    private final ProjectModel project;
    private final ProjectModel.ProjectChangeListener projectChangeListener;
    private final JButton refreshButton;
    private final JTextArea textInput;

    public PreviewScreen(final ProjectModel project, final JComponent root) {
        this.project = project;

        textInput = new JTextArea();
        JPanel container = new JPanel();
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
                actions.refreshAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
            }
        };
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
