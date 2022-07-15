package pixelj.views.projectwindow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import pixelj.actions.Shared;
import pixelj.models.Project;
import pixelj.services.AppState;
import pixelj.services.JavaPropertiesService;

public final class CloseListener extends WindowAdapter {

    private final Project project;
    private final AppState appState;
    private final JFrame window;
    private final Action saveAction;

    public CloseListener(
            final Project project,
            final AppState appState,
            final Action saveAction,
            final JFrame window
    ) {
        this.project = project;
        this.appState = appState;
        this.saveAction = saveAction;
        this.window = window;
    }

    @Override
    public void windowClosing(final WindowEvent e) {
        Shared.checkUnsaved(project, window, saveAction);

        if (window.getDefaultCloseOperation() != WindowConstants.DISPOSE_ON_CLOSE) {
            // Save state if not just returning to the home window
            // TODO: DI
            try {
                new JavaPropertiesService().save(appState);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
