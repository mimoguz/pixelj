package io.github.mimoguz.pixelj.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import io.github.mimoguz.pixelj.models.Project;
import io.github.mimoguz.pixelj.resources.Icons;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.MetricsDialog;

public class MainActions {
    public final Collection<ApplicationAction> all;
    public final ApplicationAction returnToProjectManagerAction;
    public final ApplicationAction exportAction;
    public final ApplicationAction quitAction;
    public final ApplicationAction saveAction;
    public final ApplicationAction saveAsAction;
    public final ApplicationAction showHelpAction;
    public final ApplicationAction showMetricsAction;
    public final ApplicationAction showSettingsAction;
    private boolean enabled = true;
    private final Logger logger;
    private final MetricsDialog metricsDialog;
    private final Project project;

    public MainActions(final Project project, final JComponent root) {
        this.project = project;
        metricsDialog = new MetricsDialog((Frame) SwingUtilities.windowForComponent(root));

        logger = Logger.getLogger(this.getClass().getName());
        logger.addHandler(new ConsoleHandler());

        final var res = Resources.get();

        returnToProjectManagerAction = new ApplicationAction("returnToProjectManagerAction", this::export)
                .setTextKey("returnToProjectManagerAction")
                .setIcon(Icons.PROJECT_MANAGER, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_W, ActionEvent.CTRL_MASK);

        exportAction = new ApplicationAction("exportAction", this::export).setTextKey("exportAction")
                .setIcon(Icons.FILE_EXPORT, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_E, ActionEvent.CTRL_MASK);

        quitAction = new ApplicationAction("quitAction", this::quit).setTextKey("quitAction")
                .setIcon(Icons.EXIT, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_Q, ActionEvent.CTRL_MASK);

        saveAction = new ApplicationAction("saveProjectAction", this::save).setTextKey("saveAction")
                .setIcon(Icons.FILE_SAVE, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_S, ActionEvent.CTRL_MASK);

        saveAsAction = new ApplicationAction("saveAsAction", this::saveAs).setTextKey("saveAsAction")
                .setIcon(Icons.FILE_SAVE_AS, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK);

        showHelpAction = new ApplicationAction("showHelpAction", this::showHelp).setTextKey("showHelpAction")
                .setIcon(Icons.HELP, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_F1, 0);

        showMetricsAction = new ApplicationAction("showMetricsAction", this::showMetrics)
                .setTextKey("showMetricsAction")
                .setIcon(Icons.METRICS, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_M, ActionEvent.CTRL_MASK);

        showSettingsAction = new ApplicationAction("showSettingsAction", this::showSettings)
                .setTextKey("showSettingsAction")
                .setIcon(Icons.SETTINGS, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_PERIOD, ActionEvent.CTRL_MASK);

        all = java.util.Collections.unmodifiableCollection(
                List.of(
                        returnToProjectManagerAction,
                        exportAction,
                        quitAction,
                        saveAction,
                        saveAsAction,
                        showHelpAction,
                        showMetricsAction,
                        showSettingsAction
                )
        );
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean value) {
        enabled = value;
        Actions.setEnabled(all, enabled);
    }

    private void export(final ActionEvent event, final Action action) {
        logger.log(Level.INFO, "{0}", action.getValue(Action.NAME));
    }

    private void quit(final ActionEvent event, final Action action) {
        logger.log(Level.INFO, "{0}", action.getValue(Action.NAME));
    }

    private void save(final ActionEvent event, final Action action) {
        logger.log(Level.INFO, "{0}", action.getValue(Action.NAME));
    }

    private void saveAs(final ActionEvent event, final Action action) {
        logger.log(Level.INFO, "{0}", action.getValue(Action.NAME));
    }

    private void showHelp(final ActionEvent event, final Action action) {
        logger.log(Level.INFO, "{0}", action.getValue(Action.NAME));
    }

    private void showMetrics(final ActionEvent event, final Action action) {
        metricsDialog.set(project.getMetrics());
        metricsDialog.setVisible(true);
        final var result = metricsDialog.getResult();
        if (result != null && !project.getMetrics().equals(result)) {
            project.setMetrics(result);
        }
    }

    private void showSettings(final ActionEvent event, final Action action) {
        logger.log(Level.INFO, "{0}", action.getValue(Action.NAME));
    }
}
