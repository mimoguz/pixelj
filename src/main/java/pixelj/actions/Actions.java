package pixelj.actions;

import java.util.Collection;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public final class Actions {
    private Actions() {
    }

    /**
     * Register keyboard shortcuts.
     * 
     * @param actions Actions to register.
     * @param root Component to register shortcuts.
     */
    public static void registerShortcuts(final Collection<ApplicationAction> actions, final JComponent root) {
        for (var action : actions) {
            if (action.getValue(Action.ACCELERATOR_KEY) instanceof KeyStroke accelerator) {
                root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(accelerator, action.getKey());
                root.getActionMap().put(action.getKey(), action);
            }
        }
    }

    /**
     * Enable or disable all actions in the given collection.
     * 
     * @param actions Actions to change state
     * @param isEnabled Target state
     */
    public static void setEnabled(final Collection<? extends Action> actions, final boolean isEnabled) {
        for (var action : actions) {
            action.setEnabled(isEnabled);
        }
    }
}
