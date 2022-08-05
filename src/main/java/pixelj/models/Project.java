package pixelj.models;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import pixelj.util.ChangeableBoolean;
import pixelj.util.ChangeableValue;
import pixelj.util.ReadOnlyValue;

public final class Project {

    /**
     * Document settings.
     */
    public final ChangeableValue<DocumentSettings> documentSettingsProperty;
    /**
     * Read only view for document title.
     */
    public final ReadOnlyValue<String> titleProperty;
    /**
     * Save path. Its value will be null if the project is not yet saved.
     */
    public final ChangeableValue<Path> pathProperty;
    /**
     * Save state.
     */
    public final ChangeableBoolean dirtyProperty = new ChangeableBoolean(false);

    private final SortedList<Glyph> glyphs;
    private final SortedList<KerningPair> kerningPairs;
    private final ChangeableValue<String> title;

    public Project(
        final SortedList<Glyph> glyphs,
        final SortedList<KerningPair> kerningPairs,
        final DocumentSettings settings,
        final Path path
    ) {
        title = new ChangeableValue<>(settings.title());
        titleProperty = new ReadOnlyValue<>(title);
        this.glyphs = glyphs;
        this.kerningPairs = kerningPairs;
        pathProperty = new ChangeableValue<>(path);
        documentSettingsProperty = new ChangeableValue<>(settings);

        // Ignore
        // Kerning pairs which depend on non-existing characters
        final ListDataListener kerningPairRemover = new ListDataListener() {
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

                if (kerningPairs == null) {
                    return;
                }

                // Kerning pairs which depend on non-existing characters
                final var marked = new ArrayList<KerningPair>();
                for (var index = 0; index < kerningPairs.getSize(); index++) {
                    final var model = kerningPairs.getElementAt(index);
                    if (!glyphs.sourceContains(model.getLeft()) || !glyphs.sourceContains(model.getRight())) {
                        marked.add(model);
                    }
                }

                kerningPairs.removeAll(marked);
            }
        };

        glyphs.addListDataListener(kerningPairRemover);
    }

    /**
     * @param glyph
     * @return Number of the kerning pairs that includes the glyphs
     */
    public int countDependent(final Glyph glyph) {
        return kerningPairs.countWhere(p -> p.getLeft().equals(glyph) || p.getRight().equals(glyph));
    }

    /**
     * @param glyph
     * @return List of the kerning pairs that includes the glyphs
     */
    public List<KerningPair> findDependent(final Glyph glyph) {
        return kerningPairs.find(p -> p.getLeft().equals(glyph) || p.getRight().equals(glyph));
    }

    public SortedList<Glyph> getGlyphs() {
        return glyphs;
    }

    public SortedList<KerningPair> getKerningPairs() {
        return kerningPairs;
    }

    public DocumentSettings getDocumentSettings() {
        return documentSettingsProperty.getValue();
    }

    public void setDocumentSettings(final DocumentSettings value) {
        documentSettingsProperty.setValue(value);
        title.setValue(value.title());
    }

    public boolean isDirty() {
        return dirtyProperty.getValue();
    }

    public void setDirty(final boolean value) {
        dirtyProperty.setValue(value);
    }

    public Path getPath() {
        return pathProperty.getValue();
    }

    public void setPath(final Path value) {
        pathProperty.setValue(value);
    }

    public String getTitle() {
        return titleProperty.getValue();
    }
}
