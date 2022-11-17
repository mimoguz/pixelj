package pixelj.models;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import pixelj.graphics.BinaryImage;
import pixelj.messaging.AddCharactersMessage;
import pixelj.messaging.AddKerningPairMessage;
import pixelj.messaging.DependentPairsQuestion;
import pixelj.messaging.ProjectModifiedMessage;
import pixelj.messaging.RemoveGlyphsMessage;
import pixelj.messaging.RemoveKerningPairsMessage;
import pixelj.util.ChangeableBoolean;
import pixelj.util.ChangeableValue;
import pixelj.messaging.Messenger;
import pixelj.util.Detachable;
import pixelj.util.ReadOnlyValue;
import pixelj.messaging.Receiver;

public final class Project implements Detachable {

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
    private final Receiver<ProjectModifiedMessage, Void> projectModifiedReceiver;
    private final Receiver<AddCharactersMessage, Void> addCharactersReceiver;
    private final Receiver<AddKerningPairMessage, Void> addKerningPairReceiver;
    private final Receiver<RemoveKerningPairsMessage, Void> removeKerningPairsReceiver;
    private final Receiver<RemoveGlyphsMessage, Void> removeGlyphsReceiver;
    private final Receiver<DependentPairsQuestion, Integer> dependentPairsResponder;
    private final DocumentSettings settings;

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
        this.settings = settings;
        pathProperty = new ChangeableValue<>(path);
        documentSettingsProperty = new ChangeableValue<>(settings);

        // Ignore
        // Kerning pairs which depend on non-existing characters
        final ListDataListener kerningPairRemover = new ListDataListener() {
            @Override
            public void intervalAdded(final ListDataEvent e) { // Ignore
            }

            @Override
            public void intervalRemoved(final ListDataEvent e) {
                sync();
            }

            @Override
            public void contentsChanged(final ListDataEvent e) {
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

        projectModifiedReceiver = msg -> {
            setDirty(true);
            return null;
        };
        addCharactersReceiver = msg -> {
            addCharacters(msg.condePoints());
            return null;
        };
        addKerningPairReceiver = msg -> {
            addKerningPair(msg.left(), msg.right(), msg.addMirror());
            return null;
        };
        removeKerningPairsReceiver = msg -> {
            removeKerningPairs(msg.pairs());
            return null;
        };
        removeGlyphsReceiver = msg -> {
            removeGlyphs(msg.glyphs());
            return null;
        };
        dependentPairsResponder = q -> countDependent(q.glyphs());

        Messenger.get(ProjectModifiedMessage.class).register(projectModifiedReceiver);
        Messenger.get(AddCharactersMessage.class).register(addCharactersReceiver);
        Messenger.get(AddKerningPairMessage.class).register(addKerningPairReceiver);
        Messenger.get(RemoveKerningPairsMessage.class).register(removeKerningPairsReceiver);
        Messenger.get(RemoveGlyphsMessage.class).register(removeGlyphsReceiver);
        Messenger.get(DependentPairsQuestion.class, Integer.class).register(dependentPairsResponder);
    }

    /**
     * @param glyph
     * @return Number of the kerning pairs that includes the glyphs
     */
    public int countDependent(final Glyph glyph) {
        return kerningPairs.countWhere(p -> p.getLeft().equals(glyph) || p.getRight().equals(glyph));
    }

    public int countDependent(final Collection<Glyph> glyphs) {
        return glyphs.stream().flatMap(g -> findDependent(g).stream()).collect(Collectors.toSet()).size();
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
        setDirty(true);
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

    public void addCharacters(final int... codePoints) {
        glyphs.addAll(
            Arrays
                .stream(codePoints)
                .mapToObj(codePoint -> new Glyph(
                    codePoint,
                    settings.defaultWidth(),
                    BinaryImage.of(settings.canvasWidth(), settings.canvasHeight(), true)
                ))
                .toList()
        );
        setDirty(true);
    }

    private void addKerningPair(final Glyph left, final Glyph right, final boolean addMirror) {
        if (left != null && right != null) {
            kerningPairs.add(new KerningPair(left, right, 0));
            if (addMirror) {
                kerningPairs.add(new KerningPair(right, left, 0));
            }
            setDirty(true);
        }
    }

    private void removeKerningPairs(final KerningPair... pairs) {
        removeKerningPairs(Arrays.stream(pairs).toList());
    }

    private void removeKerningPairs(final Collection<KerningPair> pairs) {
        kerningPairs.removeAll(pairs);
        setDirty(true);
    }

    private void removeGlyphs(final Collection<Glyph> glyphs) {
        this.glyphs.removeAll(glyphs);
        setDirty(true);
    }

    @Override
    public void detach() {
        Messenger.get(ProjectModifiedMessage.class).unregister(projectModifiedReceiver);
        Messenger.get(AddCharactersMessage.class).unregister(addCharactersReceiver);
        Messenger.get(AddKerningPairMessage.class).unregister(addKerningPairReceiver);
        Messenger.get(RemoveKerningPairsMessage.class).unregister(removeKerningPairsReceiver);
        Messenger.get(RemoveGlyphsMessage.class).unregister(removeGlyphsReceiver);
        Messenger.get(DependentPairsQuestion.class).unregister(dependentPairsResponder);
    }
}
