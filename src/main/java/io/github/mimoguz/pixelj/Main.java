package io.github.mimoguz.pixelj;

import javax.swing.WindowConstants;

import io.github.mimoguz.pixelj.models.ExampleData;
import io.github.mimoguz.pixelj.resources.DarkColors;
import io.github.mimoguz.pixelj.resources.LightColors;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.themes.PixeljDark;
import io.github.mimoguz.pixelj.themes.PixeljLight;
import io.github.mimoguz.pixelj.views.ProjectView;

public class Main {
    private static final boolean USE_DARK_THEME = true;

    public static void main(final String[] args) {
        if (USE_DARK_THEME) {
            PixeljDark.setup();
        } else {
            PixeljLight.setup();
        }
        final var colors = USE_DARK_THEME ? new DarkColors() : new LightColors();
        Resources.initialize(colors);

        final var view = new ProjectView(ExampleData.project);
        view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        view.setVisible(true);
    }
}
