package io.github.mimoguz.pixelj.actions;


import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;
import java.util.Collection;

@ParametersAreNonnullByDefault
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
}

