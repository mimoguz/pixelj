package pixelj.actions;

import java.util.Collection;

import javax.swing.JComponent;

import pixelj.views.shared.Components;

public interface Actions {

    /**
     * @return All actions
     */
    Collection<ApplicationAction> getAll();

    /**
     * Register keyboard shortcuts.
     *
     * @param component Component to register shortcuts.
     */
    default void registerShortcuts(final JComponent component) {
        final var actions = getAll();
        for (var action :actions) {
            Components.registerShortcut(component, action);
        }
    }

    /**
     * Enable or disable all actions in the given collection.
     *
     * @param isEnabled Target state
     */
    void setEnabled(boolean isEnabled);
}
