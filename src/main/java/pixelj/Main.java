package pixelj;

import javax.swing.WindowConstants;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import pixelj.resources.Resources;
import pixelj.views.HomeView;

public class Main {
    private static final boolean USE_DARK_THEME = true;

    public static void main(final String[] args) {
        FlatLaf.registerCustomDefaultsSource("pixelj.themes");
        if (USE_DARK_THEME) {
            FlatDarkLaf.setup();
        } else {
            FlatLightLaf.setup();
        }
        Resources.initialize(USE_DARK_THEME);

        final var view = new HomeView();
        view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        view.setVisible(true);
    }
}
