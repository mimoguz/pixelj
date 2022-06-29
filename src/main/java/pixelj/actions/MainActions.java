package pixelj.actions;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
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

import pixelj.models.Project;
import pixelj.resources.Icons;
import pixelj.resources.Resources;
import pixelj.services.ExportServiceImpl;
import pixelj.services.FileService;
import pixelj.services.FileServiceImpl;
import pixelj.views.HomeView;
import pixelj.views.MetricsDialog;
import pixelj.views.shared.Components;

public class MainActions {
    public final Collection<ApplicationAction> all;
    public final ApplicationAction returnHomeAction;
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
    private final JComponent root;

    public MainActions(final Project project, final JComponent root) {
        this.project = project;
        this.root = root;
        metricsDialog = new MetricsDialog((Frame) SwingUtilities.windowForComponent(root));

        logger = Logger.getLogger(this.getClass().getName());
        logger.addHandler(new ConsoleHandler());

        final var res = Resources.get();

        returnHomeAction = new ApplicationAction("returnHomeAction", this::returnHome).withText()
                .setIcon(Icons.PROJECT_MANAGER, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_W, ActionEvent.CTRL_MASK);

        exportAction = new ApplicationAction("exportAction", this::export).withText()
                .setIcon(Icons.FILE_EXPORT, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_E, ActionEvent.CTRL_MASK);

        quitAction = new ApplicationAction("quitAction", this::quit).withText()
                .setIcon(Icons.EXIT, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_Q, ActionEvent.CTRL_MASK);

        saveAction = new ApplicationAction("saveAction", this::save).withText()
                .setIcon(Icons.FILE_SAVE, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_S, ActionEvent.CTRL_MASK);

        saveAsAction = new ApplicationAction("saveAsAction", this::saveAs).withText()
                .setIcon(Icons.FILE_SAVE_AS, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK);

        showHelpAction = new ApplicationAction("showHelpAction", this::showHelp).withText()
                .setIcon(Icons.HELP, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_F1, 0);

        showMetricsAction = new ApplicationAction("showMetricsAction", this::showMetrics).withText()
                .setIcon(Icons.METRICS, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_M, ActionEvent.CTRL_MASK);

        showSettingsAction = new ApplicationAction("showSettingsAction", this::showSettings).withText()
                .setIcon(Icons.SETTINGS, res.colors.icon(), res.colors.disabledIcon())
                .setAccelerator(KeyEvent.VK_PERIOD, ActionEvent.CTRL_MASK);

        project.pathProperty.addChangeListener((sender, path) -> {
            saveAction.setEnabled(enabled && path != null);
        });

        saveAction.setEnabled(enabled && project.getPath() != null);

        all = List.of(
                returnHomeAction,
                exportAction,
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
        Actions.setEnabled(all, value);
        saveAction.setEnabled(value && project.getPath() != null);
    }

    // TODO: Not finished yet.
    private void export(final ActionEvent event, final Action action) {
        logger.log(Level.INFO, "{0}", action.getValue(Action.NAME));
        final var path = showSaveDialog("fnt");
        if (path == null || path.getFileName() == null) {
            return;
        }
        try {
            // TODO: DI
            new ExportServiceImpl().export(project, path, false, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void quit(final ActionEvent event, final Action action) {
        System.exit(0);
    }

    private void returnHome(final ActionEvent event, final Action action) {
        Components.switchFrames((JFrame) SwingUtilities.getWindowAncestor(root), new HomeView());
    }

    private void save(final ActionEvent event, final Action action) {
        final var path = project.getPath();
        try {
            if (path == null || path.getFileName() == null) {
                saveAs(event, action);
            } else {
                // TODO: DI
                new FileServiceImpl().writeFile(project, path);
            }
        } catch (IOException e) {
            showInfo(Resources.get().getString("saveFailed"));
        }
    }

    private void saveAs(final ActionEvent event, final Action action) {
        final var path = showSaveDialog(FileService.EXTENSION);
        if (path == null) {
            return;
        }
        try {
            if (path.getFileName() == null) {
                // TODO: DI
                new FileServiceImpl().writeFile(project, path);
            } else {
                project.setPath(path);
            }
        } catch (IOException e) {
            showInfo(Resources.get().getString("saveFailed"));
        }
    }

    private Path showSaveDialog(String filter) {
        final var outPath = MemoryUtil.memAllocPointer(1);
        try {
            final var defaultPath = project.getPath() != null ? project.getPath().toAbsolutePath().toString()
                    : null;
            if (NativeFileDialog.NFD_SaveDialog(filter, defaultPath, outPath) == NativeFileDialog.NFD_OKAY) {
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

    private void showInfo(String message) {
        JOptionPane.showMessageDialog((Frame) SwingUtilities.getWindowAncestor(root), message);
    }
}
