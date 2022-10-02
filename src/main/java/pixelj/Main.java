package pixelj;

import java.io.IOException;

import javax.swing.WindowConstants;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import pixelj.resources.Resources;
import pixelj.services.AppState;
import pixelj.services.JavaPropertiesService;
import pixelj.views.homewindow.HomeWindow;

public final class Main {

    private Main() {
    }

    public static void main(final String[] args) {
        final var appState = new AppState();
        try {
            new JavaPropertiesService().set(appState);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Resources.initialize(appState.getColorTheme(), appState.getIconTheme());
        FlatLaf.registerCustomDefaultsSource("pixelj.themes");
        if (appState.isDarkTheme()) {
            FlatDarkLaf.setup();
        } else {
            FlatLightLaf.setup();
        }

        final var view = new HomeWindow(appState);
        view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        view.setVisible(true);
    }
}
