package pixelj.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.nfd.NativeFileDialog;

import pixelj.models.DocumentSettings;
import pixelj.models.Project;
import pixelj.models.SortedList;
import pixelj.resources.Icons;
import pixelj.resources.Resources;
import pixelj.services.DBFileService;
import pixelj.views.projectwindow.ProjectWindow;
import pixelj.views.shared.Components;
import pixelj.views.shared.DocumentSettingsDialog;

public final class HomeActions {
    /**
     * Display an open dialog and load the selected project.
     */
    public final ApplicationAction openProjectAction;
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
    public final ApplicationAction loadSelectedAction;
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
    public final ApplicationAction showOptionsDialogAction;
    /**
     * Collection of all actions.
     */
    public final Collection<ApplicationAction> all;

    private final JFrame window;
    private final Logger logger;

    public HomeActions(final JFrame window) {

        logger = Logger.getLogger(this.getClass().getName());
        logger.addHandler(new ConsoleHandler());
        this.window = window;

        final var res = Resources.get();

        newProjectAction = new ApplicationAction("newProjectAction", this::newProject)
                .withText();

        loadSelectedAction = new ApplicationAction("loadSelectedAction", this::openSelectedProject)
                .withText();

        openProjectAction = new ApplicationAction("openProjectAction", this::openProject)
                .withText();

        quitAction = new ApplicationAction("quitAction", this::quit)
                .setTooltip(res.getString("quit"))
                .setIcon(Icons.EXIT, res.colors.icon(), res.colors.disabledIcon());

        showOptionsDialogAction = new ApplicationAction("showOptionsDialogAction", this::showOptionsDialog)
                .setTooltip(res.getString("options"))
                .setIcon(Icons.SETTINGS, res.colors.icon(), res.colors.disabledIcon());

        removeRecentItemAction = new ApplicationAction("removeRecentItemAction", this::removeItem)
                .withText()
                .setIcon(Icons.REMOVE_ITEM, res.colors.icon(), res.colors.disabledIcon());

        openContainingFolderAction = new ApplicationAction("openContainingFolderAction", this::openFolder)
                .withText()
                .setIcon(Icons.FILE_OPEN, res.colors.icon(), res.colors.disabledIcon());

        all = List.of(
                newProjectAction,
                loadSelectedAction,
                quitAction,
                openProjectAction,
                showOptionsDialogAction
        );
    }

    private void log(final Action action) {
        final var name = action.getValue(Action.NAME);
        final var toolTip = action.getValue(Action.SHORT_DESCRIPTION);
        logger.log(Level.INFO, "{0}", name == null ? (toolTip == null ? action : toolTip) : name);
    }

    private void newProject(final ActionEvent event, final Action action) {
        final var res = Resources.get();
        final var dialog = new DocumentSettingsDialog(
                window,
                res.getString("newProjectDialogTitle"),
                res.getString("create"),
                true
        );
        dialog.set(DocumentSettings.getDefault());
        dialog.setVisible(true);
        final var settings = dialog.getResult();
        dialog.dispose();
        if (settings != null) {
            showProject(
                    new Project(
                            new SortedList<>(),
                            new SortedList<>(),
                            settings,
                            null
                    )
            );
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
            JOptionPane.showMessageDialog(window, e.getMessage());
        }
    }

    private void openSelectedProject(final ActionEvent event, final Action action) {
        showProject(
                new Project(
                    new SortedList<>(),
                    new SortedList<>(),
                    DocumentSettings.getDefault(),
                    null
                )
        );
    }

    private void quit(final ActionEvent event, final Action action) {
        log(action);
    }

    private void showOptionsDialog(final ActionEvent event, final Action action) {
        log(action);
    }

    private void openFolder(final ActionEvent event, final Action action) {
        log(action);
    }

    private void removeItem(final ActionEvent event, final Action action) {
        log(action);
    }

    private void showProject(final Project project) {
        Components.switchFrames(window, new ProjectWindow(project));
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
