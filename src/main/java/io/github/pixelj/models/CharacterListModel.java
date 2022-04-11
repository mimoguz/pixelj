package io.github.pixelj.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.Collection;

public class CharacterListModel extends SortedFilteredListModel<CharacterModel> {
    private @Nullable KerningPairListModel attachedKerningPairListModel;

    // Removes the kerning pairs which depend on the deleted characters.
    private final ListDataListener kerningPairRemover =
            new ListDataListener() {
                @Override
                public void contentsChanged(ListDataEvent e) {
                    sync(e);
                }

                @Override
                public void intervalAdded(ListDataEvent e) {
                }

                @Override
                public void intervalRemoved(ListDataEvent e) {
                    sync(e);
                }

                private void sync(ListDataEvent e) {
                    if (attachedKerningPairListModel == null) return;

                    final var set = ((CharacterListModel) e.getSource()).source;
                    
                    // Kerning pairs which depend on non-existing characters
                    final var marked = new ArrayList<KerningPairModel>();
                    for (var index = 0; index < attachedKerningPairListModel.size(); index++) {
                        final var pair = attachedKerningPairListModel.get(index);
                        if (!set.contains(pair.getLeft()) || !set.contains(pair.getRight())) {
                            marked.add(pair);
                        }
                    }

                    attachedKerningPairListModel.removeElements(marked);
                }
            };

    public CharacterListModel() {
        super(CharacterModel.class);
        addListDataListener(kerningPairRemover);
    }

    public CharacterListModel(@NotNull Collection<CharacterModel> elements) {
        super(CharacterModel.class, elements);
        addListDataListener(kerningPairRemover);
    }

    public void setRange(int from, int to) {
        super.setFilter(model -> model.getCodePoint() >= from && model.getCodePoint() <= to);
    }

    public void syncWith(@Nullable KerningPairListModel kerningPairListModel) {
        attachedKerningPairListModel = kerningPairListModel;
    }
}
