package io.github.pixelj.actions;

import io.github.pixelj.resources.Icons;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;

public class GlobalActions {
    private boolean enabled = true;

    final ApplicationAction exportAction = new ApplicationAction(
            "exportAction",
            (e, action) -> System.out.println("Export action")
    )
            .setTextKey("exportAction")
            .setIcon(Icons.FILE_EXPORT, null, null)
            .setAccelerator(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK);

    final ApplicationAction newProjectAction = new ApplicationAction(
            "newProjectAction",
            (e, action) -> System.out.println("New project action")
    )
            .setTextKey("newProjectAction")
            .setIcon(Icons.FILE_NEW, null, null)
            .setAccelerator(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);

    final ApplicationAction openProjectAction = new ApplicationAction(
            "openProjectAction",
            (e, action) -> System.out.println("Open project action")
    )
            .setTextKey("openProjectAction")
            .setIcon(Icons.FILE_OPEN, null, null)
            .setAccelerator(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK);

    final ApplicationAction quitAction = new ApplicationAction(
            "quitAction",
            (e, action) -> System.exit(0)
    )
            .setTextKey("quitAction")
            .setIcon(Icons.EXIT, null, null)
            .setAccelerator(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK);

    final ApplicationAction saveAction = new ApplicationAction(
            "saveProjectAction",
            (e, action) -> System.out.println("Save action")
    )
            .setTextKey("saveAction")
            .setIcon(Icons.FILE_SAVE, null, null)
            .setAccelerator(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);

    final ApplicationAction saveAsAction = new ApplicationAction(
            "saveAsAction",
            (e, action) -> System.out.println("Save as action")
    )
            .setTextKey("saveAsAction")
            .setIcon(Icons.FILE_SAVE_AS, null, null)
            .setAccelerator(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);

    final ApplicationAction showHelpAction = new ApplicationAction(
            "showHelpAction",
            (e, action) -> System.out.println("Show help action")
    )
            .setTextKey("showHelpAction")
            .setIcon(Icons.HELP, null, null)
            .setAccelerator(KeyEvent.VK_F1, 0);

    final ApplicationAction showMetricsAction = new ApplicationAction(
            "showMetricsAction",
            (e, action) -> System.out.println("Show metrics action")
    )
            .setTextKey("showMetricsAction")
            .setIcon(Icons.METRICS, null, null)
            .setAccelerator(KeyEvent.VK_M, InputEvent.CTRL_DOWN_MASK);

    final ApplicationAction showSettingsAction = new ApplicationAction(
            "showSettingsAction",
            (e, action) -> System.out.println("Show settings action")
    )
            .setTextKey("showSettingsAction")
            .setIcon(Icons.SETTINGS, null, null)
            .setAccelerator(KeyEvent.VK_PERIOD, InputEvent.CTRL_DOWN_MASK);

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean value) {
        enabled = value;
        Actions.setEnabled(all, enabled);
    }
}
