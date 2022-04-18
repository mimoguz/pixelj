package io.github.mimoguz.pixelj.models;

import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@ParametersAreNonnullByDefault
public class CharacterListModel extends DisplayListModel<CharacterModel> {
    private @Nullable KerningPairListModel pairedKerningPairListModel;

    // Removes the kerning pairs which depend on the deleted characters.
    private final ListDataListener kerningPairRemover = new ListDataListener() {
        @Override
        public void contentsChanged(final ListDataEvent e) {
            sync(e);
        }

        @Override
        public void intervalAdded(final ListDataEvent e) {
        }

        @Override
        public void intervalRemoved(final ListDataEvent e) {
            sync(e);
        }

        private void sync(final ListDataEvent e) {
            if (pairedKerningPairListModel == null) {
                return;
            }

            // Kerning pairs which depend on non-existing characters
            final var marked = new ArrayList<KerningPairModel>();
            for (var index = 0; index < pairedKerningPairListModel.getSize(); index++) {
                final var model = pairedKerningPairListModel.getElementAt(index);
                if (!sourceContains(model.getLeft()) || !sourceContains(model.getRight())) {
                    marked.add(model);
                }
            }

            pairedKerningPairListModel.removeAll(marked);
        }
    };

    public CharacterListModel() {
        super();
        addListDataListener(kerningPairRemover);
    }

    public CharacterListModel(final Collection<CharacterModel> elements) {
        super(elements);
        addListDataListener(kerningPairRemover);
    }

    public int countDependent(final CharacterModel model) {
        if (pairedKerningPairListModel == null) {
            return 0;
        }
        return pairedKerningPairListModel.countWhere(p -> p.getLeft().equals(model) || p.getRight().equals(model));
    }

    public List<KerningPairModel> findDependent(final CharacterModel model) {
        if (pairedKerningPairListModel == null) {
            return Collections.emptyList();
        }
        return pairedKerningPairListModel.find(p -> p.getLeft().equals(model) || p.getRight().equals(model));
    }

    public void pair(final @Nullable KerningPairListModel kerningPairListModel) {
        pairedKerningPairListModel = kerningPairListModel;
    }

    public void setRange(final int from, final int to) {
        super.setFilter(model -> model.getCodePoint() >= from && model.getCodePoint() <= to);
    }
}
