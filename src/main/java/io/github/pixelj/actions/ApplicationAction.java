package io.github.pixelj.actions;

import io.github.pixelj.resources.Icons;
import io.github.pixelj.resources.Resources;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.BiConsumer;

public class ApplicationAction extends AbstractAction {
    private final @NotNull BiConsumer<ActionEvent, Action> consumer;
    private final @NotNull String key;

    public ApplicationAction(
            @NotNull String key,
            @NotNull BiConsumer<ActionEvent, Action> consumer,
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
            final var icon = res.getIcon(iconVariant, iconColor != null ? iconColor : res.colors.icon(),
                    disabledIconColor != null ? disabledIconColor : res.colors.disabledIcon()
            );
            putValue(Action.SMALL_ICON, icon);
            putValue(Action.LARGE_ICON_KEY, icon);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        consumer.accept(e, this);
    }

    public @NotNull String getKey() {
        return key;
    }
}
