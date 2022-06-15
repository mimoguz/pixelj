package pixelj;

import pixelj.resources.Resources;
import pixelj.themes.PixeljDark;
import pixelj.themes.PixeljLight;
import pixelj.views.HomeView;

import javax.swing.*;

public class Main {
    private static final boolean USE_DARK_THEME = false;

    public static void main(final String[] args) {
        if (USE_DARK_THEME) {
            PixeljDark.setup();
        } else {
            PixeljLight.setup();
        }
        Resources.initialize(USE_DARK_THEME);

        final var view = new HomeView();
        view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        view.setVisible(true);
    }
}
