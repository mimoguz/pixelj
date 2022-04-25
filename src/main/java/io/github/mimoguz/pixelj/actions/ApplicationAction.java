package io.github.mimoguz.pixelj.actions;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.function.BiConsumer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import org.eclipse.jdt.annotation.Nullable;

import io.github.mimoguz.pixelj.resources.Icons;
import io.github.mimoguz.pixelj.resources.Resources;

public class ApplicationAction extends AbstractAction {
    private static final long serialVersionUID = 5365300941571995035L;

    private final BiConsumer<ActionEvent, Action> consumer;
    private final String key;

    public ApplicationAction(String key, BiConsumer<ActionEvent, Action> consumer) {
        this(key, consumer, null, null, null, null, null, null);
    }

    public ApplicationAction(
            String key,
            BiConsumer<ActionEvent, Action> consumer,
            @Nullable String textKey,
            @Nullable String tooltipKey,
            @Nullable Icons iconVariant,
            @Nullable Color iconColor,
            @Nullable Color disabledIconColor,
            @Nullable KeyStroke accelerator
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
    public void actionPerformed(ActionEvent e) {
        consumer.accept(e, this);
    }

    public String getKey() {
        return key;
    }

    public ApplicationAction setAccelerator(int key, int mask) {
        return setAccelerator(KeyStroke.getKeyStroke(key, mask));
    }

    public ApplicationAction setAccelerator(KeyStroke value) {
        putValue(Action.ACCELERATOR_KEY, value);
        return this;
    }

    public ApplicationAction setIcon(
            Icons iconVariant,
            @Nullable Color color,
            @Nullable Color disabledColor
    ) {
        final var res = Resources.get();
        final var icon = res
                .getIcon(
                        iconVariant,
                        color != null ? color : res.colors.icon(),
                        disabledColor != null ? disabledColor : res.colors.disabledIcon()
                );
        putValue(Action.SMALL_ICON, icon);
        putValue(Action.LARGE_ICON_KEY, icon);
        return this;
    }

    public ApplicationAction setTextKey(String value) {
        putValue(Action.NAME, value);
        return this;
    }

    public ApplicationAction setTooltipKey(String value) {
        putValue(Action.SHORT_DESCRIPTION, value);
        return this;
    }
}
