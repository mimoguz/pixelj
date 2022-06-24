package pixelj.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;
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
import pixelj.services.FileService;
import pixelj.views.NewProjectDialog;
import pixelj.views.ProjectView;
import pixelj.views.shared.Components;

public class HomeActions {
    public final Collection<ApplicationAction> all;
    public final ApplicationAction loadProjectAction;
    private final Logger logger;
    public final ApplicationAction newProjectAction;
    public final ApplicationAction openContainingFolderAction;
    public final ApplicationAction openSelectedAction;
    public final ApplicationAction quitAction;
    public final ApplicationAction removeRecentItemAction;
    private final JComponent root;

    public final ApplicationAction showOptionsDialogAction;

    public HomeActions(final JComponent root) {

        logger = Logger.getLogger(this.getClass().getName());
        logger.addHandler(new ConsoleHandler());
        this.root = root;

        final var res = Resources.get();

        newProjectAction = new ApplicationAction("newProjectAction", this::newProject).withText()
                .setIcon(Icons.FILE_NEW, res.colors.icon(), res.colors.disabledIcon());

        openSelectedAction = new ApplicationAction("openSelectedAction", this::openSelectedProject).withText()
                .setIcon(Icons.FILE_OPEN_SELECTED, res.colors.icon(), res.colors.disabledIcon());

        quitAction = new ApplicationAction("quitAction", this::quit)
                .setTooltip(res.getString("quitActionTooltip"))
                .setIcon(Icons.EXIT, res.colors.icon(), res.colors.disabledIcon());

        loadProjectAction = new ApplicationAction("loadProjectAction", this::openProject).withText()
                .setIcon(Icons.FILE_OPEN, res.colors.icon(), res.colors.disabledIcon());

        showOptionsDialogAction = new ApplicationAction("showOptionsDialogAction", this::showOptionsDialog)
                .setTooltip(res.getString("showOptionsDialogActionTooltip"))
                .setIcon(Icons.SETTINGS, res.colors.icon(), res.colors.disabledIcon());

        removeRecentItemAction = new ApplicationAction("removeRecentItemAction", this::showOptionsDialog)
                .withText();

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
            showProject(FileService.loadFile(path));
        } catch (Exception e) {
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
            if (
                NativeFileDialog
                        .NFD_OpenDialog(FileService.EXTENSION, null, outPath) == NativeFileDialog.NFD_OKAY
            ) {
                final var pathStr = outPath.getStringUTF8();
                NativeFileDialog.nNFD_Free(outPath.get(0));
                return Path.of(pathStr);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (outPath != null) {
                MemoryUtil.memFree(outPath);
            }
        }
    }
}
