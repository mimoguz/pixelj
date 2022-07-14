package pixelj.views.homewindow;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import pixelj.services.AppState;
import pixelj.services.JavaPropertiesService;

public final class CloseListener extends WindowAdapter {

    private final AppState state;
    private final JFrame window;

    public CloseListener(final AppState state, final JFrame window) {
        this.state = state;
        this.window = window;
    }

    @Override
    public void windowClosing(final WindowEvent e) {
        // If quitting
        if (window.getDefaultCloseOperation() == WindowConstants.EXIT_ON_CLOSE) {
            try {
                new JavaPropertiesService().save(state);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
