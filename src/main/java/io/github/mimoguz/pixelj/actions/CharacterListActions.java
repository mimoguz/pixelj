package io.github.mimoguz.pixelj.actions;

import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ListSelectionModel;

import io.github.mimoguz.pixelj.graphics.BinaryImage;
import io.github.mimoguz.pixelj.models.CharacterListModel;
import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.models.KerningPairModel;
import io.github.mimoguz.pixelj.models.Metrics;

public class CharacterListActions {
    public final Collection<ApplicationAction> all;
    public final ApplicationAction showAddDialogAction;
    public final ApplicationAction showRemoveDialogAction;

    private Dimension canvasSize;
    private int defaultCharacterWidth;
    private boolean enabled = true;
    private final CharacterListModel listModel;
    private final ListSelectionModel selectionModel;
    private final Logger logger;

    public CharacterListActions(
            final CharacterListModel listModel,
            final ListSelectionModel selectionModel,
            final Metrics metrics
    ) {
        this.listModel = listModel;
        this.selectionModel = selectionModel;

        logger = Logger.getLogger(this.getClass().getName());
        logger.addHandler(new ConsoleHandler());
        logger.setLevel(Level.INFO);

        showAddDialogAction = new ApplicationAction(
                "charactersShowAddDialogAction",
                (e, action) -> logger.log(Level.INFO, "Show add dialog action")
        ).setTextKey("charactersShowAddDialogAction")
                .setAccelerator(KeyEvent.VK_PLUS, InputEvent.ALT_DOWN_MASK);

        showRemoveDialogAction = new ApplicationAction(
                "charactersShowRemoveDialogAction",
                (e, action) -> removeSelected()
        ).setTextKey("charactersShowRemoveDialogAction")
                .setAccelerator(KeyEvent.VK_MINUS, InputEvent.ALT_DOWN_MASK);

        all = java.util.Collections
                .unmodifiableCollection(List.of(showAddDialogAction, showRemoveDialogAction));

        canvasSize = new Dimension(metrics.canvasWidth(), metrics.canvasHeight());
        defaultCharacterWidth = metrics.defaultCharacterWidth();

        selectionModel.addListSelectionListener(
                e -> showRemoveDialogAction.setEnabled(selectionModel.getMinSelectionIndex() >= 0)
        );
    }

    public int getDefaultCharacterWidth() {
        return defaultCharacterWidth;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setDefaultCharacterWidth(final int defaultCharacterWidth) {
        this.defaultCharacterWidth = defaultCharacterWidth;
    }

    public void setEnabled(final boolean value) {
        enabled = value;
        Actions.setEnabled(all, enabled);
    }

    public void updateMetrics(final Metrics metrics) {
        canvasSize = new Dimension(metrics.canvasWidth(), metrics.canvasHeight());
        defaultCharacterWidth = metrics.defaultCharacterWidth();
    }

    @SuppressWarnings("unused")
    private void addCharacters(int... codePoints) {
        for (var codePoint : codePoints) {
            listModel.add(
                    new CharacterModel(
                            codePoint,
                            defaultCharacterWidth,
                            BinaryImage.of(canvasSize.width, canvasSize.height)
                    )
            );
        }
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
        if (!kerningPairs.isEmpty()) {
            logger.log(Level.INFO, "Caution! {0}  kerning pairs will also be removed.", kerningPairs.size());
        }
        logger.log(Level.INFO, "Removing  {0} characters.", index1 - index0 + 1);
    }
}
