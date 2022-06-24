package pixelj.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.function.BiConsumer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import pixelj.resources.Icons;
import pixelj.resources.Resources;

public class ApplicationAction extends AbstractAction {
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

    public ApplicationAction setAccelerator(final int key, final int mask) {
        return setAccelerator(KeyStroke.getKeyStroke(key, mask));
    }

    public ApplicationAction setAccelerator(final KeyStroke value) {
        putValue(Action.ACCELERATOR_KEY, value);
        return this;
    }

    public ApplicationAction setIcon(final Icons iconVariant) {
        return setIcon(iconVariant, null, null);
    }

    public ApplicationAction setIcon(final Icons iconVariant, final Color color, final Color disabledColor) {
        final var res = Resources.get();
        final var icon = res.getIcon(iconVariant, color, disabledColor);
        putValue(Action.SMALL_ICON, icon);
        putValue(Action.LARGE_ICON_KEY, icon);
        return this;
    }

    public ApplicationAction setTextKey(final String value) {
        putValue(Action.NAME, Resources.get().getString(value));
        return this;
    }

    public ApplicationAction setTooltip(final String value) {
        putValue(Action.SHORT_DESCRIPTION, value);
        return this;
    }

    public ApplicationAction setTooltipWithAccelerator(final String tooltip, final KeyStroke accelerator) {
        putValue(
                Action.SHORT_DESCRIPTION,
                "<html><body>" + tooltip + "<br /><strong>" + accelerator.toString().replace("pressed", "+")
                        + "</strong><body></html>"
        );
        putValue(Action.ACCELERATOR_KEY, accelerator);
        return this;
    }

    public ApplicationAction withText() {
        return setTextKey(key);
    }
}
