package io.github.pixelj.actions;

import io.github.pixelj.resources.Icons;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;

public class GlobalActions {
    final ApplicationAction exportAction = new ApplicationAction(
            "exportAction",
            (e, action) -> System.out.println("Export action"),
            "exportAction",
            null,
            Icons.FILE_EXPORT,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK)
    );
    final ApplicationAction newProjectAction = new ApplicationAction(
            "newProjectAction",
            (e, action) -> System.out.println("New project action"),
            "newProjectAction",
            null,
            Icons.FILE_NEW,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)
    );
    final ApplicationAction openProjectAction = new ApplicationAction(
            "openProjectAction",
            (e, action) -> System.out.println("Open project action"),
            "openProjectAction",
            null,
            Icons.FILE_OPEN,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK)
    );
    final ApplicationAction quitAction = new ApplicationAction(
            "quitAction",
            (e, action) -> System.exit(0),
            "quitAction",
            null,
            Icons.EXIT,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK)
    );
    final ApplicationAction saveAction = new ApplicationAction(
            "saveProjectAction",
            (e, action) -> System.out.println("Save action"),
            "saveAction",
            null,
            Icons.FILE_SAVE,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)
    );
    final ApplicationAction saveAsAction = new ApplicationAction(
            "saveAsAction",
            (e, action) -> System.out.println("Save as action"),
            "saveAsAction",
            null,
            Icons.FILE_SAVE_AS,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK)
    );
    final ApplicationAction showHelpAction = new ApplicationAction(
            "showHelpAction",
            (e, action) -> System.out.println("Show help action"),
            "showHelpAction",
            null,
            Icons.HELP,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0)
    );
    final ApplicationAction showMetricsAction = new ApplicationAction(
            "showMetricsAction",
            (e, action) -> System.out.println("Show metrics action"),
            "showMetricsAction",
            null,
            Icons.METRICS,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK)
    );
    final ApplicationAction showSettingsAction = new ApplicationAction(
            "showSettingsAction",
            (e, action) -> System.out.println("Show settings action"),
            "showSettingsAction",
            null,
            Icons.SETTINGS,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, InputEvent.CTRL_DOWN_MASK)
    );
    final Collection<ApplicationAction> all = List.of(
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
}
