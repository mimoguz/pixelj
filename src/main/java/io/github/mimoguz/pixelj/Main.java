package io.github.mimoguz.pixelj;

import java.awt.Insets;
import java.awt.Dimension;

import javax.swing.UIManager;

import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneDarkContrastIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightContrastIJTheme;

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
            FlatAtomOneDarkContrastIJTheme.setup();
        } else {
            FlatAtomOneLightContrastIJTheme.setup();
        }
        final var colors = USE_DARK_THEME ? new OneDarkColors() : new OneLightColors();
        Resources.initialize(colors);
        setTweaks(colors);

        // TODO
        final var view = new ProjectView(null);
        view.setVisible(true);
    }

    private static void setTweaks(Colors colors) {
        final var divider = String.format(
                HEX_FORMAT,
                colors.divider().getRed(),
                colors.divider().getGreen(),
                colors.divider().getBlue()
        );
        final var buttonText = String.format(
                HEX_FORMAT,
                colors.icon().getRed(),
                colors.icon().getGreen(),
                colors.icon().getBlue()
        );
        final var focusBackground = String.format(
                HEX_FORMAT,
                colors.focusBackground().getRed(),
                colors.focusBackground().getGreen(),
                colors.focusBackground().getBlue()
        );
        final var focusForeground = String.format(
                HEX_FORMAT,
                colors.focusForeground().getRed(),
                colors.focusForeground().getGreen(),
                colors.focusForeground().getBlue()
        );
        UIManager.put("Button.arc", 4);
        UIManager.put("TitlePane.iconMargins", new Insets(8, 16, 8, 16));
        UIManager.put("TitlePane.buttonSize", new Dimension(54, 40));
        UIManager.put("TitlePane.foreground", colors.faintIcon());
        UIManager.put("Popup.dropShadowOpacity", 0.3);
        UIManager.put("MenuItem.margin", new Insets(12, 28, 12, 24));
        UIManager.put("MenuItem.iconTextGap", 8);
        UIManager.put("Popup.dropShadowOpacity", 0.3);
        UIManager.put("Popup.dropShadowInsets", new Insets(0, 6, 12, 6));
        UIManager.put("[style]TabbedPane.divided", "contentAreaColor: " + divider + ";");
        UIManager.put("[style]Separator.divider", "contentAreaColor: " + divider + ";");
        UIManager.put("[style]Button.textButton", "foreground: " + buttonText + ";");
        UIManager.put(
                "[style]List.focusList",
                "selectionBackground: " + focusBackground + "; selectionForeground: " + focusForeground + ";"
        );
    }
}
