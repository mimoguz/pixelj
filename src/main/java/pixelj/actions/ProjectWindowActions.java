package pixelj.actions;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOError;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.file.InvalidPathException;
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

import pixelj.models.Project;
import pixelj.resources.Icons;
import pixelj.resources.Resources;
import pixelj.services.AppState;
import pixelj.services.BasicImageWriter;
import pixelj.services.DBFileService;
import pixelj.services.ExportServiceImpl;
import pixelj.services.FileService;
import pixelj.services.JavaPropertiesService;
import pixelj.services.RecentItem;
import pixelj.util.packer.GridPacker;
import pixelj.views.homewindow.HomeWindow;
import pixelj.views.projectwindow.ExportDialog;
import pixelj.views.shared.Components;
import pixelj.views.shared.DocumentSettingsDialog;
import pixelj.views.shared.OptionsDialog;

public final class ProjectWindowActions implements Actions {

    /** * Return to home screen. */
    public final ApplicationAction returnHomeAction;
    /** * Export project. */
    public final ApplicationAction exportAction;
    /** Quit the application. */
    public final ApplicationAction quitAction;
    /** Save project. Calls saveAsAction if no path was set for the project. */
    public final ApplicationAction saveAction;
    /** Display a save dialog. */
    public final ApplicationAction saveAsAction;
    /** Display online help. */
    public final ApplicationAction showHelpAction;
    /** Display the document settings dialog. */
    public final ApplicationAction showDocumentSettingsAction;
    /** Display the application settings dialog. */
    public final ApplicationAction showOptionsAction;

    private final Collection<ApplicationAction> all;
    private final Logger logger;
    private final DocumentSettingsDialog documentSettingsDialog;
    private final Project project;
    private final JFrame window;
    private final AppState appState;
    private final OptionsDialog optionsDialog;

    public ProjectWindowActions(final Project project, final JFrame window, final AppState appState) {
        this.project = project;
        this.window = window;
        this.appState = appState;
        optionsDialog = new OptionsDialog(window);

        final var res = Resources.get();

        documentSettingsDialog = new DocumentSettingsDialog(
                window,
                res.getString("documentSettingsDialogTitle"),
                res.getString("apply"),
                false
        );

        logger = Logger.getLogger(this.getClass().getName());
        logger.addHandler(new ConsoleHandler());

        final var menuShortcutMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

        returnHomeAction = new ApplicationAction("returnHomeAction", this::returnHome)
                .withText()
                .setIcon(Icons.HOME, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_W, menuShortcutMask);

        exportAction = new ApplicationAction("exportAction", this::export)
                .withText()
                .setIcon(Icons.FILE_EXPORT, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_E, menuShortcutMask);

        quitAction = new ApplicationAction("quitAction", this::quit)
                .withText()
                .setIcon(Icons.EXIT, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_Q, menuShortcutMask);

        saveAction = new ApplicationAction("saveAction", this::save)
                .withText()
                .setIcon(Icons.FILE_SAVE, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_S, menuShortcutMask);

        saveAsAction = new ApplicationAction("saveAsAction", this::saveAs)
                .withText()
                .setIcon(Icons.FILE_SAVE_AS, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_S, menuShortcutMask | ActionEvent.SHIFT_MASK);

        showHelpAction = new ApplicationAction("showHelpAction", this::showHelp)
                .withText()
                .setIcon(Icons.HELP, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_F1, 0);

        showDocumentSettingsAction = new ApplicationAction(
                "showDocumentSettingsAction",
                this::showDocumentSettings
        )
                .withText()
                .setIcon(Icons.METRICS, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_D, menuShortcutMask);

        showOptionsAction = new ApplicationAction("showOptionsAction", this::showOptions)
                .withText()
                .setIcon(Icons.SETTINGS, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_PERIOD, menuShortcutMask);

        all = List.of(
                returnHomeAction,
                exportAction,
                quitAction,
                saveAction,
                saveAsAction,
                showHelpAction,
                showDocumentSettingsAction,
                showOptionsAction
        );
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

    private void export(final ActionEvent event, final Action action) {
        final var settings = project.getDocumentSettings();
        final var minimumSize = new Dimension(settings.canvasWidth(), settings.canvasHeight());
        final var defaultSize = new Dimension(
                Math.max(settings.canvasWidth(), appState.getExportImageWidth()),
                Math.max(settings.canvasHeight(), appState.getExportImageHeight())
        );
        final var exportDialog = new ExportDialog(window, defaultSize, minimumSize);
        exportDialog.setVisible(true);
        final var exportOptions = exportDialog.getResult();
        if (exportOptions == null) {
            return;
        }

        appState.setExportImageWidth(exportOptions.width());
        appState.setExportImageHeight(exportOptions.height());

        final var path = showSaveDialog("fnt");
        if (path == null || path.getFileName() == null) {
            return;
        }

        try {
            // TODO: Select layout strategy
            new ExportServiceImpl(new GridPacker<>(), new BasicImageWriter())
                    .export(project, path, exportOptions.width(), exportOptions.height());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void quit(final ActionEvent event, final Action action) {
        try {
            new JavaPropertiesService().save(appState);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Shared.checkUnsaved(project, window, saveAction);
        System.exit(0);
    }

    private void returnHome(final ActionEvent event, final Action action) {
        Shared.checkUnsaved(project, window, saveAction);
        Components.switchFrames((JFrame) window, new HomeWindow(appState));
    }

    private void save(final ActionEvent event, final Action action) {
        final var path = project.getPath();
        try {
            if (path == null || path.getFileName() == null) {
                saveAs(event, action);
            } else {
                // TODO: DI
                new DBFileService().writeFile(project, path);
                appState.replaceRecentItemTitle(project.getPath(), project.getTitle());
                project.setDirty(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showLoadFailure(e.getLocalizedMessage());
        }
    }

    private void saveAs(final ActionEvent event, final Action action) {
        final var path = showSaveDialog(FileService.EXTENSION);
        if (path == null) {
            return;
        }
        try {
            if (path.getFileName() != null) {
                // TODO: DI
                new DBFileService().writeFile(project, path);
                appState.addRecentItem(new RecentItem(project.getTitle(), path));
                project.setPath(path);
                project.setDirty(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
            showLoadFailure(e.getLocalizedMessage());
        }
    }

    private Path showSaveDialog(final String filter) {
        final var outPath = MemoryUtil.memAllocPointer(1);
        try {
            final var defaultPath = project.getPath() != null
                    ? project.getPath().toAbsolutePath().toString()
                    : null;
            if (NativeFileDialog.NFD_SaveDialog(filter, defaultPath, outPath) == NativeFileDialog.NFD_OKAY) {
                final var pathStr = outPath.getStringUTF8();
                NativeFileDialog.nNFD_Free(outPath.get(0));
                return Path.of(pathStr);
            } else {
                return null;
            }
        } catch (IOError | SecurityException | BufferUnderflowException | InvalidPathException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (outPath != null) {
                MemoryUtil.memFree(outPath);
            }
        }
    }

    private void showHelp(final ActionEvent event, final Action action) {
        logAction(action);
    }

    private void showDocumentSettings(final ActionEvent event, final Action action) {
        documentSettingsDialog.set(project.getDocumentSettings());
        documentSettingsDialog.setVisible(true);
        final var result = documentSettingsDialog.getResult();
        if (result != null && !project.getDocumentSettings().equals(result)) {
            project.setDocumentSettings(result);
            project.setDirty(true);
        }
    }

    private void showOptions(final ActionEvent event, final Action action) {
        optionsDialog.setDarkTheme(appState.isDarkTheme());
        optionsDialog.setVisible(true);
        final var result = optionsDialog.getResult();
        if (result != null) {
            appState.setDarkTheme(result);
        }
    }

    private void showInfo(final String message) {
        JOptionPane.showMessageDialog(window, message);
    }

    private void showLoadFailure(final String cause) {
        showInfo(Resources.get().getString("saveFailed") + ":\n" + cause);
    }

    private void logAction(final Action action) {
        logger.log(Level.INFO, "{0}", action.getValue(Action.NAME));
    }
}
