package io.github.mimoguz.pixelj;

import javax.swing.UIManager;
import javax.swing.WindowConstants;

import io.github.mimoguz.pixelj.models.ExampleData;
import io.github.mimoguz.pixelj.resources.Colors;
import io.github.mimoguz.pixelj.resources.OneDarkColors;
import io.github.mimoguz.pixelj.resources.OneLightColors;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.ProjectView;

public class Main {
    private static final String HEX_FORMAT = "#%02x%02x%02x";
    private static final boolean USE_DARK_THEME = true;

    public static void main(final String[] args) {
        if (USE_DARK_THEME) {
            io.github.mimoguz.pixelj.themes.PixeljDark.setup();
        } else {
            io.github.mimoguz.pixelj.themes.PixeljLight.setup();
        }
        final var colors = USE_DARK_THEME ? new OneDarkColors() : new OneLightColors();
        Resources.initialize(colors);

        final var view = new ProjectView(ExampleData.project);
        view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        view.setVisible(true);
    }
}
