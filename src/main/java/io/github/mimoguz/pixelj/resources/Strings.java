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

    public Strings(final ResourceBundle bundle) {
        resourceBundle = bundle;
    }


    @NotNull
    public String format(final String key, final Object... arguments) {
        try {
            final var str = resourceBundle.getString(key);
            return MessageFormat.format(str, arguments);
        } catch (final MissingResourceException e) {
            return key + " -> " + Arrays.toString(arguments);
        }
    }

    @NotNull
    public String get(final String key) {
        try {
            return resourceBundle.getString(key);
        } catch (final MissingResourceException e) {
            return key;
        }
    }

    @NotNull
    public Locale getLocale() {
        return resourceBundle.getLocale();
    }
}
