package io.github.pixelj.actions;

import io.github.pixelj.resources.Icons;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;

public class GlobalActions {
    private final ApplicationAction exportAction = new ApplicationAction(
            "export",
            (e) -> System.out.println("Export action"),
            "exportAction",
            null,
            Icons.FILE_EXPORT,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK)
    );
    private final ApplicationAction newProjectAction = new ApplicationAction(
            "newProject",
            (e) -> System.out.println("New project action"),
            "newProjectAction",
            null,
            Icons.FILE_NEW,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)
    );
    private final ApplicationAction openProjectAction = new ApplicationAction(
            "openProject",
            (e) -> System.out.println("Open project action"),
            "openProjectAction",
            null,
            Icons.FILE_NEW,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK)
    );
    private final ApplicationAction quitAction = new ApplicationAction(
            "quit",
            (e) -> System.exit(0),
            "quitAction",
            null,
            Icons.FILE_NEW,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK)
    );
    private final ApplicationAction saveAction = new ApplicationAction(
            "saveProject",
            (e) -> System.out.println("Save action"),
            "saveAction",
            null,
            Icons.FILE_NEW,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)
    );
    private final ApplicationAction saveAsAction = new ApplicationAction(
            "saveAs",
            (e) -> System.out.println("Save as action"),
            "saveAsAction",
            null,
            Icons.FILE_NEW,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)
    );
    private final ApplicationAction showHelpAction = new ApplicationAction(
            "showHelp",
            (e) -> System.out.println("Show help action"),
            "showHelpAction",
            null,
            Icons.FILE_NEW,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0)
    );
    private final ApplicationAction showMetricsAction = new ApplicationAction(
            "showMetrics",
            (e) -> System.out.println("Show metrics action"),
            "showMetricsAction",
            null,
            Icons.FILE_NEW,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK)
    );
    private final ApplicationAction showSettingsAction = new ApplicationAction(
            "showSettings",
            (e) -> System.out.println("Show settings action"),
            "showSettingsAction",
            null,
            Icons.FILE_NEW,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, InputEvent.CTRL_DOWN_MASK)
    );
    private final Collection<ApplicationAction> all = List.of(
            exportAction,
            newProjectAction,
            openProjectAction,
            quitAction,
            saveAction,
            saveAsAction,
            showMetricsAction,
            showSettingsAction,
            showHelpAction
    );

    public Collection<ApplicationAction> getAll() {
        return all;
    }

    public ApplicationAction getExportAction() {
        return exportAction;
    }

    public ApplicationAction getNewProjectAction() {
        return newProjectAction;
    }

    public ApplicationAction getOpenProjectAction() {
        return openProjectAction;
    }

    public ApplicationAction getQuitAction() {
        return quitAction;
    }

    public ApplicationAction getSaveAction() {
        return saveAction;
    }

    public ApplicationAction getSaveAsAction() {
        return saveAsAction;
    }

    public ApplicationAction getShowHelpAction() {
        return showHelpAction;
    }

    public ApplicationAction getShowMetricsAction() {
        return showMetricsAction;
    }

    public ApplicationAction getShowSettingsAction() {
        return showSettingsAction;
    }
}
