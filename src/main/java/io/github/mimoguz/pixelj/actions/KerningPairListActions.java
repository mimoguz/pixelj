package io.github.mimoguz.pixelj.actions;

import io.github.mimoguz.pixelj.models.KerningPairListModel;
import io.github.mimoguz.pixelj.models.Metrics;

import javax.swing.*;
import java.util.Collection;

public class KerningPairListActions {
    public Collection<ApplicationAction> all;
    public ApplicationAction showAddDialogAction;
    public ApplicationAction showRemoveDialogAction;

    public KerningPairListActions(
            final KerningPairListModel listModel,
            final ListSelectionModel selectionModel,
            final Metrics metrics
    ) {
        super();
    }
}
