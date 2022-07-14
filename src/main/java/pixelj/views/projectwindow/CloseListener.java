package pixelj.views.projectwindow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import pixelj.actions.ApplicationAction;
import pixelj.models.Project;
import pixelj.resources.Resources;
import pixelj.services.AppState;
import pixelj.services.JavaPropertiesService;

public final class CloseListener extends WindowAdapter {

    private final Project project;
    private final AppState appState;
    private final JFrame window;
    private final ApplicationAction saveAction;

    public CloseListener(
            final Project project,
            final AppState appState,
            final ApplicationAction saveAction,
            final JFrame window
    ) {
        this.project = project;
        this.appState = appState;
        this.saveAction = saveAction;
        this.window = window;
    }

    @Override
    public void windowClosing(final WindowEvent e) {
        System.out.println(window.getDefaultCloseOperation());
        if (window.getDefaultCloseOperation() == WindowConstants.EXIT_ON_CLOSE) {
            // If quitting
            // TODO: DI
            try {
                new JavaPropertiesService().save(appState);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

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
