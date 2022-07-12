package pixelj;

import javax.swing.WindowConstants;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import pixelj.resources.Resources;
import pixelj.views.homewindow.HomeWindow;

public final class Main {
    private static final boolean USE_DARK_THEME = false;

    private Main() {
    }

    public static void main(final String[] args) {
        Resources.initialize(USE_DARK_THEME);
        FlatLaf.registerCustomDefaultsSource("pixelj.themes");
        if (USE_DARK_THEME) {
            FlatDarkLaf.setup();
        } else {
            FlatLightLaf.setup();
        }
        Resources.initialize(USE_DARK_THEME);

        final var view = new HomeWindow();
        view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        view.setVisible(true);
    }
}
