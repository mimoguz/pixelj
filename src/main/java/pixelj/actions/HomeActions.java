package pixelj.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.nfd.NativeFileDialog;

import pixelj.models.ExampleData;
import pixelj.models.Project;
import pixelj.resources.Icons;
import pixelj.resources.Resources;
import pixelj.services.DBFileService;
import pixelj.views.NewProjectDialog;
import pixelj.views.ProjectView;
import pixelj.views.shared.Components;

public final class HomeActions {
    /**
     * Display an open dialog and load the selected project.
     */
    public final ApplicationAction loadProjectAction;
    /**
     * Display the new project dialog.
     */
    public final ApplicationAction newProjectAction;
    /**
     * Context action: Open the parent folder of the selected recent item.
     */
    public final ApplicationAction openContainingFolderAction;
    /**
     * Open the selected recent item.
     */
    public final ApplicationAction openSelectedAction;
    /**
     * Quit the application.
     */
    public final ApplicationAction quitAction;
    /**
     * Context action: Remove the selected recent item from the list.
     */
    public final ApplicationAction removeRecentItemAction;
    /**
     * Display the application settings dialog.
     */
    public final ApplicationAction showSettingsDialogAction;
    /**
     * Collection of all actions.
     */
    public final Collection<ApplicationAction> all;

    private final JComponent root;
    private final Logger logger;

    public HomeActions(final JComponent root) {

        logger = Logger.getLogger(this.getClass().getName());
        logger.addHandler(new ConsoleHandler());
        this.root = root;

        final var res = Resources.get();

        newProjectAction = new ApplicationAction("newProjectAction", this::newProject)
                .withText()
                .setIcon(Icons.FILE_NEW, res.colors.icon(), res.colors.disabledIcon());

        openSelectedAction = new ApplicationAction("openSelectedAction", this::openSelectedProject)
                .withText()
                .setIcon(Icons.FILE_OPEN_SELECTED, res.colors.icon(), res.colors.disabledIcon());

        quitAction = new ApplicationAction("quitAction", this::quit)
                .setTooltip(res.getString("quitActionTooltip"))
                .setIcon(Icons.EXIT, res.colors.icon(), res.colors.disabledIcon());

        loadProjectAction = new ApplicationAction("loadProjectAction", this::openProject)
                .withText()
                .setIcon(Icons.FILE_OPEN, res.colors.icon(), res.colors.disabledIcon());

        showSettingsDialogAction = new ApplicationAction("showOptionsDialogAction", this::showOptionsDialog)
                .setTooltip(res.getString("showOptionsDialogActionTooltip"))
                .setIcon(Icons.SETTINGS, res.colors.icon(), res.colors.disabledIcon());

        removeRecentItemAction = new ApplicationAction("removeRecentItemAction", this::showOptionsDialog)
                .withText();

        openContainingFolderAction = new ApplicationAction(
                "openContainingFolderAction",
                this::showOptionsDialog
        ).withText();

        all = List.of(
                newProjectAction,
                openSelectedAction,
                quitAction,
                loadProjectAction,
                showSettingsDialogAction
        );
    }

    private void log(final Action action) {
        final var name = action.getValue(Action.NAME);
        final var toolTip = action.getValue(Action.SHORT_DESCRIPTION);
        logger.log(Level.INFO, "{0}", name == null ? (toolTip == null ? action : toolTip) : name);
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
        final var path = showOpenDialog();
        if (path == null || path.getFileName() == null) {
            return;
        }
        try {
            // TODO: DI
            showProject(new DBFileService().readFile(path));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(root), e.getMessage());
        }
    }

    private void openSelectedProject(final ActionEvent event, final Action action) {
        showProject(ExampleData.createProject());
    }

    private void quit(final ActionEvent event, final Action action) {
        log(action);
    }

    private void showOptionsDialog(final ActionEvent event, final Action action) {
        log(action);
    }

    private void showProject(final Project project) {
        Components.switchFrames((JFrame) SwingUtilities.getWindowAncestor(root), new ProjectView(project));
    }

    private Path showOpenDialog() {
        final var outPath = MemoryUtil.memAllocPointer(1);
        try {
            final var dialogResult = NativeFileDialog.NFD_OpenDialog(
                    DBFileService.EXTENSION, 
                    null, 
                    outPath
            );
            if (dialogResult == NativeFileDialog.NFD_OKAY) {
                final var pathStr = outPath.getStringUTF8();
                NativeFileDialog.nNFD_Free(outPath.get(0));
                return Path.of(pathStr);
            } else {
                return null;
            }
        } finally {
            if (outPath != null) {
                MemoryUtil.memFree(outPath);
            }
        }
    }
}
