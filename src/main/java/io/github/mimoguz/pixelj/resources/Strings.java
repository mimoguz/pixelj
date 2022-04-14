package io.github.mimoguz.pixelj.resources;

import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Strings {
    final ResourceBundle resourceBundle;

    public Strings(@NotNull ResourceBundle bundle) {
        this.resourceBundle = bundle;
    }

    @NotNull
    public Locale getLocale() {
        return resourceBundle.getLocale();
    }

    @NotNull
    public String get(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    @NotNull
    public String format(String key, Object... arguments) {
        try {
            final var str = resourceBundle.getString(key);
            return MessageFormat.format(str, arguments);
        } catch (MissingResourceException e) {
            return key + " -> " + Arrays.toString(arguments);
        }
    }
}
