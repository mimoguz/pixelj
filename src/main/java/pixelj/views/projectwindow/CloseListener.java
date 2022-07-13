package pixelj.views.projectwindow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pixelj.actions.ApplicationAction;
import pixelj.models.Project;
import pixelj.resources.Resources;

public final class CloseListener extends WindowAdapter {

    private final Project project;
    private final JFrame window;
    private final ApplicationAction saveAction;

    public CloseListener(final Project project, final ApplicationAction saveAction, final JFrame window) {
        this.project = project;
        this.saveAction = saveAction;
        this.window = window;
    }

    @Override
    public void windowClosing(final WindowEvent e) {
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
                saveAction.actionPerformed(null);
            }
        }
    }
}
