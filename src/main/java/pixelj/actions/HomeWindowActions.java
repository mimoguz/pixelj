package pixelj.actions;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.nfd.NativeFileDialog;

import pixelj.models.DocumentSettings;
import pixelj.models.Project;
import pixelj.models.SortedList;
import pixelj.resources.Icon;
import pixelj.resources.Resources;
import pixelj.services.AppState;
import pixelj.services.DBFileService;
import pixelj.services.JavaPropertiesService;
import pixelj.services.RecentItem;
import pixelj.views.projectwindow.ProjectWindow;
import pixelj.views.shared.Components;
import pixelj.views.shared.DocumentSettingsDialog;
import pixelj.views.shared.Help;
import pixelj.views.shared.OptionsDialog;

public final class HomeWindowActions implements Actions {

    /** Display an open dialog and load the selected project. */
    public final ApplicationAction openProjectAction;
    /** Display the new project dialog. */
    public final ApplicationAction newProjectAction;
    /** Context action: Open the parent folder of the selected recent item. */
    public final ApplicationAction openContainingFolderAction;
    /** Open the selected recent item. */
    public final ApplicationAction loadSelectedAction;
    /** Show home window help page. */
    public final ApplicationAction showHelpAction;
    /** Quit the application. */
    public final ApplicationAction quitAction;
    /** Context action: Remove the selected recent item from the list. */
    public final ApplicationAction removeRecentItemAction;
    /** Display the application settings dialog. */
    public final ApplicationAction showOptionsDialogAction;

    private final Collection<ApplicationAction> all;
    private final JFrame window;
    private final AppState appState;
    private final ListSelectionModel selectionModel;
    private final OptionsDialog optionsDialog;

    public HomeWindowActions(
        final JFrame window,
        final AppState appState,
        final ListSelectionModel selectionModel
    ) {
        this.window = window;
        this.appState = appState;
        this.selectionModel = selectionModel;
        this.optionsDialog = new OptionsDialog(window);

        final var res = Resources.get();

        newProjectAction = new ApplicationAction("newProjectAction", this::newProject)
            .withText();

        loadSelectedAction = new ApplicationAction("loadSelectedAction", this::openSelectedProject)
            .withText();

        openProjectAction = new ApplicationAction("openProjectAction", this::openProject)
            .withText();

        showHelpAction = new ApplicationAction("showHomeHelpAction", this::showHelp)
            .setTooltip(res.getString("help"))
            .setIcon(Icon.HELP);

        quitAction = new ApplicationAction("quitAction", this::quit)
            .setTooltip(res.getString("quit"))
            .setIcon(Icon.EXIT);

        showOptionsDialogAction = new ApplicationAction("showOptionsDialogAction", this::showOptionsDialog)
            .setTooltip(res.getString("options"))
            .setIcon(Icon.SETTINGS);

        removeRecentItemAction = new ApplicationAction("removeRecentItemAction", this::removeItem)
            .withText()
            .setIcon(Icon.REMOVE_ITEM);

        openContainingFolderAction = new ApplicationAction("openContainingFolderAction", this::openFolder)
            .withText()
            .setIcon(Icon.FILE_OPEN);

        all = List.of(
            newProjectAction,
            loadSelectedAction,
            showHelpAction,
            quitAction,
            openProjectAction,
            showOptionsDialogAction
        );
    }

    @Override
    public void detach() {
        optionsDialog.dispose();
    }

    private void newProject(final ActionEvent event, final Action action) {
        final var res = Resources.get();
        final var dialog = new DocumentSettingsDialog(
            window,
            res.getString("newProjectDialogTitle"),
            res.getString("create"),
            true
        );
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.set(DocumentSettings.getDefault());
        dialog.setVisible(true);
        final var settings = dialog.getResult();
        dialog.dispose();
        if (settings != null) {
            showProject(new Project(new SortedList<>(), new SortedList<>(), settings, null));
        }
    }

    private void openProject(final ActionEvent event, final Action action) {
        final var path = showOpenDialog();
        if (path == null || path.getFileName() == null) {
            return;
        }
        try {
            // TODO: DI
            final var project = new DBFileService().readFile(path);
            appState.addRecentItem(
                new RecentItem(project.getTitle(), project.getPath(), OffsetDateTime.now())
            );
            showProject(new DBFileService().readFile(path));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(window, e.getMessage());
        }
    }

    private void openSelectedProject(final ActionEvent event, final Action action) {
        final var index = selectionModel.getMinSelectionIndex();
        if (index < 0) {
            return;
        }
        final var item = appState.getRecentItem(index);
        try {
            appState.replaceRecentItemDate(item.path(), OffsetDateTime.now());
            final var project = new DBFileService().readFile(item.path());
            showProject(project);
        } catch (IOException e) {
            askRemoveItem(item);
        }
    }

    private void quit(final ActionEvent event, final Action action) {
        try {
            // TODO: DI
            new JavaPropertiesService().save(appState);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.exit(0);
    }

    private void showOptionsDialog(final ActionEvent event, final Action action) {
        Shared.getOptions(optionsDialog, appState);
    }

    private void showHelp(final ActionEvent event, final Action action) {
        Help.showPage(Help.Page.HOME);
    }

    private void openFolder(final ActionEvent event, final Action action) {
        final var index = selectionModel.getMinSelectionIndex();
        if (index < 0) {
            return;
        }
        final var item = appState.getRecentItem(index);
        final var parent = item.path().getParent();
        final var dir = parent.toFile();
        try {
            Desktop.getDesktop().open(dir);
        } catch (IOException | NullPointerException e) {
            askRemoveItem(item);
        }
    }

    private void removeItem(final ActionEvent event, final Action action) {
        final var index = selectionModel.getMinSelectionIndex();
        if (index >= 0) {
            final var item = appState.getRecentItem(index);
            appState.removeRecentItem(item);
        }
    }

    private void showProject(final Project project) {
        Components.switchFrames(window, new ProjectWindow(project, appState));
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

    @Override
    public Collection<ApplicationAction> getAll() {
        return all;
    }

    @Override
    public void setEnabled(final boolean enabled) {
        for (var action : all) {
            action.setEnabled(enabled);
        }
    }

    private void askRemoveItem(final RecentItem item) {
        final var response = JOptionPane.showConfirmDialog(
            window,
            Resources.get().getString("cantOpenRecentWarning"),
            null,
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        if (response == JOptionPane.OK_OPTION) {
            appState.removeRecentItem(item);
        }
    }
}
