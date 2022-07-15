package pixelj.actions;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pixelj.models.Project;
import pixelj.resources.Resources;

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
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (result == JOptionPane.OK_OPTION) {
                action.actionPerformed(null);
            }
        }
    }
}
