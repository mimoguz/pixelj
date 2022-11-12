package pixelj;

import java.awt.Color;
import java.awt.image.RGBImageFilter;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import pixelj.resources.Resources;
import pixelj.services.AppState;
import pixelj.services.DefaultLogger;
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
            DefaultLogger.get().logInfo("Can't load the previous state: " + e.getLocalizedMessage());
        }

        Resources.initialize(appState.getColorTheme(), appState.getIconTheme());

        UIManager.put("Component.grayFilter", new DisabledIconFilter(Resources.get().colors.disabledIcon()));

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
