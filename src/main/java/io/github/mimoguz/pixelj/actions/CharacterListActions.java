package io.github.mimoguz.pixelj.actions;

import io.github.mimoguz.pixelj.models.CharacterListModel;
import io.github.mimoguz.pixelj.models.KerningPairModel;

import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@ParametersAreNonnullByDefault
public class CharacterListActions {
    public final ApplicationAction showAddDialogAction = new ApplicationAction(
            "showAddDialog",
            (e, action) -> System.out.println("Show add dialog action")
    )
            .setTextKey("showAddDialogAction")
            .setAccelerator(KeyEvent.VK_PLUS, InputEvent.ALT_DOWN_MASK);
    
    private final CharacterListModel listModel;
    private final ListSelectionModel selectionModel;
    public final ApplicationAction showRemoveDialogAction = new ApplicationAction(
            "showRemoveDialog",
            (e, action) -> removeSelected()
    )
            .setTextKey("showRemoveDialogAction")
            .setAccelerator(KeyEvent.VK_MINUS, InputEvent.ALT_DOWN_MASK);

    public final Collection<ApplicationAction> all = List.of(showAddDialogAction, showRemoveDialogAction);
    @NotNull
    private Dimension canvasSize;
    private int defaultCharacterWidth;
    private boolean enabled = true;

    public CharacterListActions(
            final CharacterListModel listModel,
            final ListSelectionModel selectionModel,
            final Dimension canvasSize
    ) {
        this.listModel = listModel;
        this.selectionModel = selectionModel;
        this.canvasSize = canvasSize;


        selectionModel.addListSelectionListener(e -> {
            showRemoveDialogAction.setEnabled(selectionModel.getMinSelectionIndex() >= 0);
        });


    }

    public @NotNull Dimension getCanvasSize() {
        return canvasSize;
    }

    public void setCanvasSize(final @NotNull Dimension canvasSize) {
        this.canvasSize = canvasSize;
    }

    public int getDefaultCharacterWidth() {
        return defaultCharacterWidth;
    }

    public void setDefaultCharacterWidth(final int defaultCharacterWidth) {
        this.defaultCharacterWidth = defaultCharacterWidth;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean value) {
        enabled = value;
        Actions.setEnabled(all, enabled);
    }

    private void removeSelected() {
        final var index0 = selectionModel.getMinSelectionIndex();
        final var index1 = selectionModel.getMaxSelectionIndex();

        if (index0 < 0) {
            return;
        }

        final var kerningPairs = new HashSet<KerningPairModel>();
        for (var index = index0; index <= index1; index++) {
            kerningPairs.addAll(listModel.findDependent(listModel.getElementAt(index)));
        }
        if (kerningPairs.size() > 0) {
            System.out.println(
                    "Caution! "
                            + Integer.toString(kerningPairs.size())
                            + " kerning pairs will also be removed."
            );
        }
        System.out.println("Removing " + Integer.toString(index1 - index0 + 1) + " characters.");
    }
}
