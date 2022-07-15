package pixelj.resources;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class Strings {

    final ResourceBundle resourceBundle;

    public Strings(final ResourceBundle bundle) {
        resourceBundle = bundle;
    }

    /**
     * @param key       Resource key
     * @param arguments Format arguments
     * @return Formatted string resource
     */
    public String format(final String key, final Object... arguments) {
        try {
            final var str = resourceBundle.getString(key);
            return MessageFormat.format(str, arguments);
        } catch (final MissingResourceException e) {
            return key + " -> " + Arrays.toString(arguments);
        }
    }

    /**
     * @param key Resource key
     * @return String resource
     */
    public String get(final String key) {
        try {
            return resourceBundle.getString(key);
        } catch (final MissingResourceException e) {
            return key;
        }
    }

    public Locale getLocale() {
        return resourceBundle.getLocale();
    }
}
