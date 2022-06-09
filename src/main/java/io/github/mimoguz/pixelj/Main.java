package io.github.mimoguz.pixelj;

import io.github.mimoguz.pixelj.models.ExampleData;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.themes.PixeljDark;
import io.github.mimoguz.pixelj.themes.PixeljLight;
import io.github.mimoguz.pixelj.views.ProjectView;

import javax.swing.*;

public class Main {
    private static final boolean USE_DARK_THEME = true;

    public static void main(final String[] args) {
        if (USE_DARK_THEME) {
            PixeljDark.setup();
        } else {
            PixeljLight.setup();
        }
        Resources.initialize(USE_DARK_THEME);

        final var view = new ProjectView(ExampleData.createProject());
        view.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        view.setVisible(true);
    }
}
