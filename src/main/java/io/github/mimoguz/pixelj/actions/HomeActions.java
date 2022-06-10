package io.github.mimoguz.pixelj.actions;

import io.github.mimoguz.pixelj.models.ExampleData;
import io.github.mimoguz.pixelj.resources.Icons;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.ProjectView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeActions {
    public final Collection<ApplicationAction> all;
    public final ApplicationAction newProjectAction;
    public final ApplicationAction openContainigFolderAction;
    public final ApplicationAction openProjectAction;
    public final ApplicationAction quitAction;
    public final ApplicationAction removeRecentItemAction;
    public final ApplicationAction showOpenDialogAction;
    public final ApplicationAction showOptionsDialogAction;
    private final Logger logger;

    private final JComponent root;

    public HomeActions(final JComponent root) {

        logger = Logger.getLogger(this.getClass().getName());
        logger.addHandler(new ConsoleHandler());
        this.root = root;

        final var res = Resources.get();

        newProjectAction = new ApplicationAction("newProjectAction", this::newProject)
                .setTextKey("newProjectAction")
                .setIcon(Icons.FILE_NEW, res.colors.icon(), res.colors.disabledIcon());

        openProjectAction = new ApplicationAction("openProjectAction", this::openProject)
                .setTextKey("openProjectAction")
                .setIcon(Icons.FILE_OPEN, res.colors.icon(), res.colors.disabledIcon());

        quitAction = new ApplicationAction("quitAction", this::quit)
                .setTooltip(res.getString("quitActionTooltip"))
                .setIcon(Icons.EXIT, res.colors.icon(), res.colors.disabledIcon());

        showOpenDialogAction = new ApplicationAction("showOpenDialogAction", this::showOpenDialog)
                .setTextKey("showOpenDialogAction")
                .setIcon(Icons.FILE_OPEN, res.colors.icon(), res.colors.disabledIcon());

        showOptionsDialogAction = new ApplicationAction("showOptionsDialogAction", this::showOptionsDialog)
                .setTooltip(res.getString("showOptionsDialogActionTooltip"))
                .setIcon(Icons.SETTINGS, res.colors.icon(), res.colors.disabledIcon());

        removeRecentItemAction = new ApplicationAction("removeRecentItemAction", this::showOptionsDialog)
                .setTextKey("removeRecentItemAction");

        openContainigFolderAction = new ApplicationAction("openContainigFolderAction", this::showOptionsDialog)
                .setTextKey("openContainigFolderAction");

        all = List.of(
                newProjectAction,
                openProjectAction,
                quitAction,
                showOpenDialogAction,
                showOptionsDialogAction
        );
    }

    private void log(final Action action) {
        final var name = action.getValue(Action.NAME);
        final var toolTip = action.getValue(Action.SHORT_DESCRIPTION);
        logger.log(
                Level.INFO,
                "{0}",
                name == null
                        ? (toolTip == null ? action : toolTip)
                        : name
        );
    }

    private void newProject(final ActionEvent event, final Action action) {
        log(action);
    }

    private void openProject(final ActionEvent event, final Action action) {
        final var frame = ((JFrame) SwingUtilities.getWindowAncestor(root));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final var projectView = new ProjectView(ExampleData.createProject());
        projectView.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        projectView.setVisible(true);
        frame.setVisible(false);
    }

    private void quit(final ActionEvent event, final Action action) {
        log(action);
    }

    private void showOpenDialog(final ActionEvent event, final Action action) {
        log(action);
    }

    private void showOptionsDialog(final ActionEvent event, final Action action) {
        log(action);
    }
}