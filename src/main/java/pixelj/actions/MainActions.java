package pixelj.actions;

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
import pixelj.services.BasicImageWriter;
import pixelj.services.DBFileService;
import pixelj.services.ExportServiceImpl;
import pixelj.services.FileService;
import pixelj.util.packer.GridPacker;
import pixelj.views.homewindow.HomeWindow;
import pixelj.views.projectwindow.ExportDialog;
import pixelj.views.shared.Components;
import pixelj.views.shared.DocumentSettingsDialog;

public final class MainActions {
    /**
     * Return to home screen.
     */
    public final ApplicationAction returnHomeAction;
    /**
     * Export project.
     */
    public final ApplicationAction exportAction;
    /**
     * Quit the application.
     */
    public final ApplicationAction quitAction;
    /**
     * Save project. Calls saveAsAction if no path was set for the project.
     */
    public final ApplicationAction saveAction;
    /**
     * Display a save dialog.
     */
    public final ApplicationAction saveAsAction;
    /**
     * Display online help.
     */
    public final ApplicationAction showHelpAction;
    /**
     * Display the document settings dialog.
     */
    public final ApplicationAction showDocumentSettingsAction;
    /**
     * Display the application settings dialog.
     */
    public final ApplicationAction showOptionsAction;
    /**
     * Collection of all actions.
     */
    public final Collection<ApplicationAction> all;

    private boolean enabled = true;
    private final Logger logger;
    private final DocumentSettingsDialog documentSettingsDialog;
    private final Project project;
    private final JFrame window;

    public MainActions(final Project project, final JFrame window) {
        this.project = project;
        this.window = window;

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

        showOptionsAction = new ApplicationAction("showOptionsAction", this::showSettings)
                .withText()
                .setIcon(Icons.SETTINGS, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_PERIOD, menuShortcutMask);

        project.pathProperty.addChangeListener((sender, path) ->
                saveAction.setEnabled(enabled && path != null)
        );

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

    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param value Is enabled
     */
    public void setEnabled(final boolean value) {
        enabled = value;
        Actions.setEnabled(all, value);
    }

    private void export(final ActionEvent event, final Action action) {
        final var exportDialog = new ExportDialog(window, project.getDocumentSettings());
        exportDialog.setVisible(true);
        final var exportOptions = exportDialog.getResult();
        if (exportOptions == null) {
            return;
        }

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
        handleDirty();
        System.exit(0);
    }

    private void returnHome(final ActionEvent event, final Action action) {
        handleDirty();
        Components.switchFrames((JFrame) window, new HomeWindow());
    }

    private void save(final ActionEvent event, final Action action) {
        final var path = project.getPath();
        try {
            if (path == null || path.getFileName() == null) {
                saveAs(event, action);
            } else {
                // TODO: DI
                new DBFileService().writeFile(project, path);
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

    private void showSettings(final ActionEvent event, final Action action) {
        logAction(action);
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

    private void handleDirty() {
        // TODO: This code is duplicated here from the CloseListener
        if (project.isDirty()) {
            final var res = Resources.get();
            final var result = JOptionPane.showConfirmDialog(
                    window,
                    res.getString("unsavedWarning"),
                    null,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (result == JOptionPane.OK_OPTION) {
                saveAction.actionPerformed(null);
            }
        }
    }
}
