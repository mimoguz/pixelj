package pixelj.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import pixelj.resources.Icons;
import pixelj.resources.Resources;

public final class ApplicationAction extends AbstractAction {

    private static final String WORD_SEPARATOR = " ";

    private final BiConsumer<ActionEvent, Action> consumer;
    private final String key;

    /**
     * @param key      A unique key. Must not be a null.
     * @param consumer This will be called in the actionPerformed method. Its second
     *                 parameter is the action itself. Must not be a null.
     */
    public ApplicationAction(final String key, final BiConsumer<ActionEvent, Action> consumer) {
        this.key = key;
        this.consumer = consumer;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        consumer.accept(e, this);
    }

    public String getKey() {
        return key;
    }

    /**
     * Create KeyStroke using the key code and the modifiers, assign it as the shortcut key for the action.
     *
     * @param keyCode   Key codes defined in java.awt.event.KeyEvent class
     * @param modifiers Modifiers defined in java.awt.event.InputEvent class
     * @return This action
     */
    public ApplicationAction setAccelerator(final int keyCode, final int modifiers) {
        return setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
    }

    /**
     * Assign key stroke as the shortcut key for the action.
     *
     * @param value Key stroke
     * @return This action
     */
    public ApplicationAction setAccelerator(final KeyStroke value) {
        putValue(Action.ACCELERATOR_KEY, value);
        return this;
    }

    /**
     * Create a font icon using the icon variant, set it as both small and large icons.
     *
     * @param iconVariant Icon variant
     * @return This action
     */
    public ApplicationAction setIcon(final Icons iconVariant) {
        return setIcon(iconVariant, null, null);
    }

    /**
     * Create a font icon using the icon variant and the colors, set it as both small and large icons.
     *
     * @param iconVariant   Icon variant
     * @param color         Icon color
     * @param disabledColor Disabled icon color
     * @return This action
     */
    public ApplicationAction setIcon(final Icons iconVariant, final Color color, final Color disabledColor) {
        final var res = Resources.get();
        final var icon = res.getIcon(iconVariant, color, disabledColor);
        putValue(Action.SMALL_ICON, icon);
        putValue(Action.LARGE_ICON_KEY, icon);
        return this;
    }

    /**
     * Set the action's NAME value. This value is used as label text if a control needs one.
     *
     * @param value The key for the strings resource.
     * @return This action
     */
    public ApplicationAction setTextKey(final String value) {
        putValue(Action.NAME, Resources.get().getString(value));
        return this;
    }

    /**
     * Set the action's SHORT_DESCRIPTION value. This value is used as tooltip text.
     *
     * @param value Text value
     * @return This action
     */
    public ApplicationAction setTooltip(final String value) {
        putValue(Action.SHORT_DESCRIPTION, value);
        return this;
    }

    /**
     * Set both tooltip and shortcut key. This will add shortcut info to the tooltip.
     *
     * @param tooltip     Tooltip text
     * @param accelerator Shortcut
     * @return This action
     */
    public ApplicationAction setTooltipWithAccelerator(final String tooltip, final KeyStroke accelerator) {
        putValue(
                Action.SHORT_DESCRIPTION,
                String.format(
                        "<html><body>%s<br /><strong>%s</strong><body></html>",
                        tooltip,
                        acceleratorToString(accelerator)
                )
        );
        putValue(Action.ACCELERATOR_KEY, accelerator);
        return this;
    }

    /**
     * Use action's key to get its text from the strings resource.
     *
     * @return This action
     */
    public ApplicationAction withText() {
        return setTextKey(key);
    }

    private static String acceleratorToString(final KeyStroke accelerator) {
        final var str = accelerator.toString().replace("pressed", "+");
        // TODO: Investigate: Locale problems?
        return Arrays.stream(str.split(WORD_SEPARATOR))
                .map(word -> word.isEmpty()
                        ? word
                        : Character.toTitleCase(word.charAt(0)) + word.substring(1).toLowerCase()
                )
                .collect(Collectors.joining(WORD_SEPARATOR));
    }
}
