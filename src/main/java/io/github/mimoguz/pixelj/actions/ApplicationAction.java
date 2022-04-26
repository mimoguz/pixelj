package io.github.mimoguz.pixelj.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.function.BiConsumer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import io.github.mimoguz.pixelj.resources.Icons;
import io.github.mimoguz.pixelj.resources.Resources;

public class ApplicationAction extends AbstractAction {
    private static final long serialVersionUID = 5365300941571995035L;

    private final BiConsumer<ActionEvent, Action> consumer;

    private final String key;

    public ApplicationAction(final String key, final BiConsumer<ActionEvent, Action> consumer) {
        this(key, consumer, null, null, null, null, null, null);
    }

    /**
     * @param key               A unique key. Must not be a null.
     * @param consumer          This will be called in the actionPerformed method.
     *                          Its second parameter is the action itself. Must not
     *                          be a null.
     * @param textKey           Action.NAME
     * @param tooltipKey        Action.SHORT_DESCRIPTION
     * @param iconVariant       Icon parameters are used to set Action.SMALL_ICON
     *                          and Action.LARGE_ICON_KEY.
     * @param iconColor         Icon parameters are used to set Action.SMALL_ICON
     *                          and Action.LARGE_ICON_KEY.
     * @param disabledIconColor Icon parameters are used to set Action.SMALL_ICON
     *                          and Action.LARGE_ICON_KEY.
     * @param accelerator       Action.ACCELERATOR_KEY
     */
    public ApplicationAction(
            String key,
            BiConsumer<ActionEvent, Action> consumer,
            String textKey,
            String tooltipKey,
            Icons iconVariant,
            Color iconColor,
            Color disabledIconColor,
            KeyStroke accelerator
    ) {
        this.consumer = consumer;
        this.key = key;

        final var res = Resources.get();
        if (textKey != null) {
            putValue(Action.NAME, res.getString(textKey));
        }
        if (tooltipKey != null) {
            putValue(Action.SHORT_DESCRIPTION, res.getString(tooltipKey));
        }
        if (accelerator != null) {
            putValue(Action.ACCELERATOR_KEY, accelerator);
        }
        if (iconVariant != null) {
            setIcon(iconVariant, iconColor, disabledIconColor);
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e != null) {
            consumer.accept(e, this);
        }
    }

    public String getKey() {
        return key;
    }

    public ApplicationAction setAccelerator(int key, int mask) {
        return setAccelerator(KeyStroke.getKeyStroke(key, mask));
    }

    public ApplicationAction setAccelerator(final KeyStroke value) {
        putValue(Action.ACCELERATOR_KEY, value);
        return this;
    }

    public ApplicationAction setIcon(final Icons iconVariant, final Color color, final Color disabledColor) {
        final var res = Resources.get();
        final var icon = res.getIcon(
                iconVariant,
                color != null ? color : res.colors.icon(),
                disabledColor != null ? disabledColor : res.colors.disabledIcon()
        );
        putValue(Action.SMALL_ICON, icon);
        putValue(Action.LARGE_ICON_KEY, icon);
        return this;
    }

    public ApplicationAction setTextKey(final String value) {
        putValue(Action.NAME, value);
        return this;
    }

    public ApplicationAction setTooltipKey(final String value) {
        putValue(Action.SHORT_DESCRIPTION, value);
        return this;
    }
}
