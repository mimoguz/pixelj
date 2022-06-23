package pixelj.models;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import pixelj.util.ChangeableValue;

public class Project {

    private final SortedList<Glyph> glyphs;
    private final SortedList<KerningPair> kerningPairs;
    public final ChangeableValue<Metrics> metricsProperty;
    public final ChangeableValue<String> titleProperty;
    public final ChangeableValue<Path> pathProperty;

    public Project(
            final String title,
            final SortedList<Glyph> glyphs,
            final SortedList<KerningPair> kerningPairs,
            final Metrics metrics,
            final Path path
    ) {
        titleProperty = new ChangeableValue<>(title);
        this.glyphs = glyphs;
        this.kerningPairs = kerningPairs;
        pathProperty = new ChangeableValue<>(path);
        metricsProperty = new ChangeableValue<>(metrics);

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

    public int countDependent(final Glyph model) {
        return kerningPairs.countWhere(p -> p.getLeft().equals(model) || p.getRight().equals(model));
    }

    public List<KerningPair> findDependent(final Glyph model) {
        return kerningPairs.find(p -> p.getLeft().equals(model) || p.getRight().equals(model));
    }

    public SortedList<Glyph> getGlyphs() {
        return glyphs;
    }

    public SortedList<KerningPair> getKerningPairs() {
        return kerningPairs;
    }

    public Metrics getMetrics() {
        return metricsProperty.getValue();
    }

    public Path getPath() {
        return pathProperty.getValue();
    }

    public void setPath(Path value) {
        pathProperty.setValue(value);
    }

    public String getTitle() {
        return titleProperty.getValue();
    }

    public void setMetrics(final Metrics value) {
        metricsProperty.setValue(value);
    }

    public void setTitle(final String value) {
        titleProperty.setValue(value);
    }
}
