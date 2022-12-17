package pixelj;

import java.awt.Color;
import java.awt.image.RGBImageFilter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

import javax.swing.*;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import pixelj.resources.Resources;
import pixelj.services.AppState;
import pixelj.services.DBFileService;
import pixelj.services.DefaultLogger;
import pixelj.services.JavaPropertiesService;
import pixelj.views.homewindow.HomeWindow;
import pixelj.views.projectwindow.ProjectWindow;

public final class Main {

    private Main() {
    }

    public static void main(final String[] args) {
        final var appState = loadState();
        initializeResources(appState);

        // Try loading the program argument.
        if (args.length == 1) {
            try {
                final var path = Path.of(args[0]);
                final var project = new DBFileService().readFile(path);
                final var view = new ProjectWindow(project, appState);
                view.setVisible(true);
                return;
            } catch (InvalidPathException | IOException e) {
                DefaultLogger.get().logError("Can't load the path: " + e.getLocalizedMessage());
            }
        }

        // Else show the home window.
        final var view = new HomeWindow(appState);
        view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        view.setVisible(true);
    }

    private static AppState loadState() {
        final var appState = new AppState();
        try {
            new JavaPropertiesService().set(appState);
        } catch (IOException e) {
            DefaultLogger.get().logInfo("Can't load the previous state: " + e.getLocalizedMessage());
        }
        return appState;
    }

    private static void initializeResources(final AppState state) {
        Resources.initialize(state.getColorTheme(), state.getIconTheme());
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        UIManager.put("Component.grayFilter", new DisabledIconFilter(Resources.get().colors.disabledIcon()));
        FlatLaf.registerCustomDefaultsSource("pixelj.themes");
        if (state.isDarkTheme()) {
            FlatDarkLaf.setup();
        } else {
            FlatLightLaf.setup();
        }
    }

    private static class DisabledIconFilter extends RGBImageFilter {
        private final int fill;

        public DisabledIconFilter(final Color fill) {
            this.fill = fill.getRGB();
            canFilterIndexColorModel = true;
        }

        @Override
        public int filterRGB(final int x, final int y, final int rgb) {
            return fill;
        }
    }
}
