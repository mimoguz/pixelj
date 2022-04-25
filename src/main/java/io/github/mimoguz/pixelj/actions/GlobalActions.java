package io.github.mimoguz.pixelj.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;

import javax.swing.Action;

import io.github.mimoguz.pixelj.resources.Icons;

public class GlobalActions {
    public final Collection<ApplicationAction> all;
    public final ApplicationAction exportAction;
    public final ApplicationAction newProjectAction;
    public final ApplicationAction openProjectAction;
    public final ApplicationAction quitAction;
    public final ApplicationAction saveAction;
    public final ApplicationAction saveAsAction;
    public final ApplicationAction showHelpAction;
    public final ApplicationAction showMetricsAction;
    public final ApplicationAction showSettingsAction;

    private boolean enabled = true;

    public GlobalActions() {
        exportAction = new ApplicationAction("exportAction", this::export)
                .setTextKey("exportAction")
                .setIcon(Icons.FILE_EXPORT, null, null)
                .setAccelerator(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK);

        newProjectAction = new ApplicationAction("newProjectAction", this::newProject)
                .setTextKey("newProjectAction")
                .setIcon(Icons.FILE_NEW, null, null)
                .setAccelerator(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);

        openProjectAction = new ApplicationAction("openProjectAction", this::openProject)
                .setTextKey("openProjectAction")
                .setIcon(Icons.FILE_OPEN, null, null)
                .setAccelerator(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK);

        quitAction = new ApplicationAction("quitAction", this::quit).setTextKey("quitAction")
                .setIcon(Icons.EXIT, null, null)
                .setAccelerator(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK);

        saveAction = new ApplicationAction("saveProjectAction", this::save)
                .setTextKey("saveAction")
                .setIcon(Icons.FILE_SAVE, null, null)
                .setAccelerator(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);

        saveAsAction = new ApplicationAction("saveAsAction", this::saveAs)
                .setTextKey("saveAsAction")
                .setIcon(Icons.FILE_SAVE_AS, null, null)
                .setAccelerator(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);

        showHelpAction = new ApplicationAction("showHelpAction", this::showHelp)
                .setTextKey("showHelpAction")
                .setIcon(Icons.HELP, null, null)
                .setAccelerator(KeyEvent.VK_F1, 0);

        showMetricsAction = new ApplicationAction("showMetricsAction", this::showMetrics)
                .setTextKey("showMetricsAction")
                .setIcon(Icons.METRICS, null, null)
                .setAccelerator(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK);

        showSettingsAction = new ApplicationAction("showSettingsAction", this::showSettings)
                .setTextKey("showSettingsAction")
                .setIcon(Icons.SETTINGS, null, null)
                .setAccelerator(KeyEvent.VK_PERIOD, InputEvent.CTRL_DOWN_MASK);

        all = List.of(
                exportAction,
                newProjectAction,
                openProjectAction,
                quitAction,
                saveAction,
                saveAsAction,
                showHelpAction,
                showMetricsAction,
                showSettingsAction
        );
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean value) {
        enabled = value;
        Actions.setEnabled(all, enabled);
    }

    private void export(ActionEvent event, Action action) {
        System.out.println(action.getValue(Action.NAME));
    }

    private void newProject(ActionEvent event, Action action) {
        System.out.println(action.getValue(Action.NAME));
    }

    private void openProject(ActionEvent event, Action action) {
        System.out.println(action.getValue(Action.NAME));
    }

    private void quit(ActionEvent event, Action action) {
        System.out.println(action.getValue(Action.NAME));
    }

    private void save(ActionEvent event, Action action) {
        System.out.println(action.getValue(Action.NAME));
    }

    private void saveAs(ActionEvent event, Action action) {
        System.out.println(action.getValue(Action.NAME));
    }

    private void showHelp(ActionEvent event, Action action) {
        System.out.println(action.getValue(Action.NAME));
    }

    private void showMetrics(ActionEvent event, Action action) {
        System.out.println(action.getValue(Action.NAME));
    }

    private void showSettings(ActionEvent event, Action action) {
        System.out.println(action.getValue(Action.NAME));
    }
}
