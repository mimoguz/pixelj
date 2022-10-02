package pixelj.actions;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pixelj.models.Project;
import pixelj.resources.Resources;
import pixelj.services.AppState;
import pixelj.views.shared.OptionsDialog;

public final class Shared {

    private Shared() {
    }

    /**
     * Check if the project is dirty, run action if it is.
     *
     * @param project
     * @param window
     * @param action Save action
     */
    public static void checkUnsaved(final Project project, final JFrame window, final Action action) {
        if (project.isDirty()) {
            final var res = Resources.get();
            final var result = JOptionPane.showConfirmDialog(
                window,
                res.getString("unsavedWarning"),
                null,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) {
                action.actionPerformed(null);
            }
        }
    }

    /**
     * Show options dialog and then set app state.
     *
     * @param dialog
     * @param appState
     */
    public static void getOptions(final OptionsDialog dialog, final AppState appState) {
        dialog.setTheme(appState.getColorTheme(), appState.getIconTheme());
        dialog.setVisible(true);
        final var result = dialog.getResult();
        if (result != null) {
            appState.setColorTheme(result.colorTheme());
            appState.setIconTheme(result.iconTheme());
        }
    }
}
