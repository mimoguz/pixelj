package io.github.mimoguz.pixelj.resources;

import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@ParametersAreNonnullByDefault
public class Strings {
    final ResourceBundle resourceBundle;

    public Strings(ResourceBundle bundle) {
        this.resourceBundle = bundle;
    }


    public @NotNull String format(String key, Object... arguments) {
        try {
            final var str = resourceBundle.getString(key);
            return MessageFormat.format(str, arguments);
        } catch (MissingResourceException e) {
            return key + " -> " + Arrays.toString(arguments);
        }
    }

    public @NotNull String get(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public @NotNull Locale getLocale() {
        return resourceBundle.getLocale();
    }
}
