package pixelj.models;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import pixelj.graphics.BinaryImage;
import pixelj.messaging.AddCharactersMessage;
import pixelj.messaging.AddKerningPairMessage;
import pixelj.messaging.ProjectModifiedMessage;
import pixelj.util.ChangeableBoolean;
import pixelj.util.ChangeableValue;
import pixelj.messaging.Messenger;
import pixelj.util.ReadOnlyValue;
import pixelj.util.Receiver;

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
    private final Receiver projectModifiedReceiver;
    private final Receiver addCharactersReceiver;
    private final Receiver addKerningPairReceiver;
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

        projectModifiedReceiver = new Receiver() {
            @Override
            public Class<?> messageType() {
                return ProjectModifiedMessage.class;
            }

            @Override
            public void receive(final Object message) {
                Project.this.setDirty(true);
            }
        };

        addCharactersReceiver = new Receiver() {
            @Override
            public Class<?> messageType() {
                return AddCharactersMessage.class;
            }

            @Override
            public void receive(final Object message) {
                if (message instanceof AddCharactersMessage msg) {
                    Project.this.addCharacters(msg.condePoints());
                }
            }
        };

        addKerningPairReceiver = new Receiver() {
            @Override
            public Class<?> messageType() {
                return AddKerningPairMessage.class;
            }

            @Override
            public void receive(final Object message) {
                if (message instanceof  AddKerningPairMessage msg) {
                    Project.this.addKerningPair(msg.left(), msg.right(), msg.addMirror());
                }
            }
        };

        Messenger.getDefault().register(projectModifiedReceiver);
        Messenger.getDefault().register(addCharactersReceiver);
        Messenger.getDefault().register(addKerningPairReceiver);
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

    public void addCharacters(final int... codePoints)
    {
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
}
