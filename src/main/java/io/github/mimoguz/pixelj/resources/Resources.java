package io.github.mimoguz.pixelj.resources;

import io.github.mimoguz.pixelj.graphics.FontIcon;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.io.IOException;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@ParametersAreNonnullByDefault
public class Resources {
    private static final String base = "io/github/mimoguz/pixelj/resources/";
    @Nullable
    private static Resources instance = null;
    public final Colors colors;
    private final Font iconFont;
    private final Strings strings;

    private Resources(Colors colors) {
        iconFont = loadFont();
        strings = new Strings(loadResourceBundle());
        this.colors = colors;
    }

    public static @NotNull Resources get() {
        if (instance == null) {
            initialize(new OneDarkColors());
        }
        return instance;
    }

    private static void initialize(Colors colors) {
        instance = new Resources(colors);
    }

    public @NotNull String formatString(@NotNull String key, Object... arguments) {
        return strings.format(key, arguments);
    }

    @NotNull
    public FontIcon getIcon(@NotNull Icons icon, @NotNull Color color) {
        return new FontIcon(icon.codePoint, color, iconFont);
    }

    public @NotNull FontIcon getIcon(@NotNull Icons icon, @NotNull Color color, @NotNull Color disabledColor) {
        return new FontIcon(icon.codePoint, color, disabledColor, iconFont);
    }

    public @NotNull Locale getLocale() {
        return strings.getLocale();
    }

    public @NotNull String getString(@NotNull String key) {
        return strings.get(key);
    }

    private @NotNull Font loadFont() {
        try (var stream = getClass().getResourceAsStream(base + "pxf16.otf")) {
            if (stream == null) {
                throw new IOException("The resource " + base + "pxf16.otf is not found.");
            }
            try {
                final var font = Font.createFont(Font.TRUETYPE_FONT, stream);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
                return font;
            } catch (IOException | FontFormatException e) {
                throw new ResourceInitializationException("Can't read font file: \n" + e.getMessage());
            }
        } catch (IOException e) {
            throw new ResourceInitializationException("Can't read font file:\n" + e.getMessage());
        }
    }

    private @NotNull ResourceBundle loadResourceBundle() {
        final var bundleBase = "strings/strings";
        ResourceBundle bundle;
        try {
            bundle = ResourceBundle.getBundle(base + bundleBase, Locale.getDefault(), getClass().getClassLoader());
        } catch (MissingResourceException e1) {
            try {
                bundle = ResourceBundle.getBundle(base + bundleBase, Locale.US, getClass().getClassLoader());
            } catch (MissingResourceException e2) {
                throw new ResourceInitializationException(
                        "Can't find strings:\n"
                                + e1.getMessage()
                                + "\n\n--------------------------\n\n"
                                + e2.getMessage()
                );
            }
        }
        return bundle;
    }

    public static class ResourceInitializationException extends RuntimeException {
        public ResourceInitializationException(String message) {
            super(message);
        }
    }
}
