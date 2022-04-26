package io.github.mimoguz.pixelj.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class CharacterListModel extends DisplayListModel<CharacterModel> {
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
            final var paired = pairedKerningPairListModel;

            if (paired == null) {
                return;
            }

            // Kerning pairs which depend on non-existing characters
            final var marked = new ArrayList<KerningPairModel>();
            for (var index = 0; index < paired.getSize(); index++) {
                final var model = paired.getElementAt(index);
                if (!sourceContains(model.getLeft()) || !sourceContains(model.getRight())) {
                    marked.add(model);
                }
            }

            paired.removeAll(marked);
        }
    };

    @Nullable
    private KerningPairListModel pairedKerningPairListModel;

    public CharacterListModel() {
        super();
        addListDataListener(kerningPairRemover);
    }

    public CharacterListModel(final Collection<CharacterModel> elements) {
        super(elements);
        addListDataListener(kerningPairRemover);
    }

    public int countDependent(final CharacterModel model) {
        final var paired = pairedKerningPairListModel;
        if (paired == null) {
            return 0;
        }
        return paired.countWhere(p -> p.getLeft().equals(model) || p.getRight().equals(model));
    }

    @NonNull
    public List<@NonNull KerningPairModel> findDependent(final CharacterModel model) {
        final var paired = pairedKerningPairListModel;
        if (paired == null) {
            return Collections.emptyList();
        }
        return paired.find(p -> p.getLeft().equals(model) || p.getRight().equals(model));
    }

    public void pair(@Nullable final KerningPairListModel kerningPairListModel) {
        pairedKerningPairListModel = kerningPairListModel;
    }

    public void setRange(final int from, final int to) {
        super.setFilter(model -> model.getCodePoint() >= from && model.getCodePoint() <= to);
    }
}
