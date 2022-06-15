package pixelj.actions;

import pixelj.models.ExampleData;
import pixelj.models.Project;
import pixelj.resources.Icons;
import pixelj.resources.Resources;
import pixelj.views.NewProjectDialog;
import pixelj.views.ProjectView;

import javax.swing.*;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.nfd.NativeFileDialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeActions {
    private static final String EXTENSION = "pixj";

    public final Collection<ApplicationAction> all;
    public final ApplicationAction openContainingFolderAction;
    public final ApplicationAction openSelectedAction;
    public final ApplicationAction quitAction;
    public final ApplicationAction removeRecentItemAction;
    public final ApplicationAction newProjectAction;
    public final ApplicationAction loadProjectAction;
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

        openSelectedAction = new ApplicationAction("openSelectedAction", this::openSelectedProject)
                .setTextKey("openSelectedAction")
                .setIcon(Icons.FILE_OPEN_SELECTED, res.colors.icon(), res.colors.disabledIcon());

        quitAction = new ApplicationAction("quitAction", this::quit)
                .setTooltip(res.getString("quitActionTooltip"))
                .setIcon(Icons.EXIT, res.colors.icon(), res.colors.disabledIcon());

        loadProjectAction = new ApplicationAction("loadProjectAction", this::openProject)
                .setTextKey("loadProjectAction")
                .setIcon(Icons.FILE_OPEN, res.colors.icon(), res.colors.disabledIcon());

        showOptionsDialogAction = new ApplicationAction("showOptionsDialogAction", this::showOptionsDialog)
                .setTooltip(res.getString("showOptionsDialogActionTooltip"))
                .setIcon(Icons.SETTINGS, res.colors.icon(), res.colors.disabledIcon());

        removeRecentItemAction = new ApplicationAction("removeRecentItemAction", this::showOptionsDialog)
                .setTextKey("removeRecentItemAction");

        openContainingFolderAction = new ApplicationAction(
                "openContainingFolderAction",
                this::showOptionsDialog
        ).setTextKey("openContainingFolderAction");

        all = List.of(
                newProjectAction,
                openSelectedAction,
                quitAction,
                loadProjectAction,
                showOptionsDialogAction
        );
    }

    private void log(final Action action) {
        final var name = action.getValue(Action.NAME);
        final var toolTip = action.getValue(Action.SHORT_DESCRIPTION);
        logger.log(Level.INFO, "{0}", name == null ? (toolTip == null ? action : toolTip) : name);
    }

    private void openSelectedProject(final ActionEvent event, final Action action) {
        showProject(ExampleData.createProject());
    }

    private void quit(final ActionEvent event, final Action action) {
        log(action);
    }

    private void newProject(final ActionEvent event, final Action action) {
        final var dialog = new NewProjectDialog((Frame) SwingUtilities.getWindowAncestor(root));
        dialog.setVisible(true);
        final var project = dialog.getProject();
        dialog.dispose();
        if (project != null) {
            showProject(project);
        }
    }

    private void openProject(final ActionEvent event, final Action action) {
        log(action);
        final var outPath = MemoryUtil.memAllocPointer(1);
        try {
            if (NativeFileDialog.NFD_OpenDialog(EXTENSION, null, outPath) == NativeFileDialog.NFD_OKAY) {
                logger.log(Level.INFO, "Selected {0}", outPath.getStringUTF8());
                NativeFileDialog.nNFD_Free(outPath.get(0));
            } else {
                logger.log(Level.INFO, "Cancelled or error");
            }
        } finally {
            if (outPath != null) {
                MemoryUtil.memFree(outPath);
            }
        }
    }

    private void showOptionsDialog(final ActionEvent event, final Action action) {
        log(action);
    }

    private void showProject(final Project project) {
        final var frame = ((JFrame) SwingUtilities.getWindowAncestor(root));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final var projectView = new ProjectView(project);
        projectView.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        projectView.setVisible(true);
        frame.setVisible(false);
    }
}
