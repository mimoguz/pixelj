package io.github.mimoguz.pixelj.actions;

import java.util.Collection;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class Actions {
    public static void registerShortcuts(Collection<ApplicationAction> actions, JComponent root) {
        for (var action : actions) {
            if (action.getValue(Action.ACCELERATOR_KEY) instanceof KeyStroke accelerator) {
                root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(accelerator, action.getKey());
                root.getActionMap().put(action.getKey(), action);
            }
        }
    }

    public static void setEnabled(Collection<? extends Action> actions, boolean isEnabled) {
        for (var action : actions) {
            action.setEnabled(isEnabled);
        }
    }

    private Actions() {}
}
