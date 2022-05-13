package io.github.mimoguz.pixelj.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class CharacterListModel extends DisplayListModel<CharacterModel> {
    // Removes the kerning pairs which depend on the deleted characters.
    private final ListDataListener kerningPairRemover = new ListDataListener() {
        @Override
        public void contentsChanged(final ListDataEvent e) {
            sync();
        }

        @Override
        public void intervalAdded(final ListDataEvent e) { // Ignore
        }

        @Override
        public void intervalRemoved(final ListDataEvent e) {
            sync();
        }

        private void sync() {

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

    public List<KerningPairModel> findDependent(final CharacterModel model) {
        final var paired = pairedKerningPairListModel;
        if (paired == null) {
            return Collections.emptyList();
        }
        return paired.find(p -> p.getLeft().equals(model) || p.getRight().equals(model));
    }

    /**
     * To prevent orphan characters in the kerning pair list, the character list and
     * the kerninig pair list can be paired. Any kerning pairs that contain a
     * deleted character will be deleted from the kerning pair list as well.
     *
     * @param kerningPairListModel May be null
     */
    public void pair(final KerningPairListModel kerningPairListModel) {
        pairedKerningPairListModel = kerningPairListModel;
    }

    public void setRange(final int from, final int to) {
        super.setFilter(model -> model.getCodePoint() >= from && model.getCodePoint() <= to);
    }
}
