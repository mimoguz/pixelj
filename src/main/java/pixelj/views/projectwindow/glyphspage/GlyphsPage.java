package pixelj.views.projectwindow.glyphspage;

import javax.swing.JFrame;
import javax.swing.ListSelectionModel;

import pixelj.messaging.CopyFromMessage;
import pixelj.messaging.Messenger;
import pixelj.messaging.Receiver;
import pixelj.models.Project;
import pixelj.util.Detachable;
import pixelj.views.controls.GlyphView;

public final class GlyphsPage extends GlyphsPageBase implements Detachable, Receiver<CopyFromMessage, Void> {
    private final ListSelectionModel selectionModel;
    private Project project;

    public GlyphsPage(final Project project, final JFrame window) {
        super(new ListPanel(project, window), new PainterPanel(project, window));
        this.project = project;
        selectionModel = listPanel.getSelectionModel();

        // Connect the listModel to the painter
        selectionModel.addListSelectionListener(e -> {
            final var index = selectionModel.getMinSelectionIndex();
            if (index == selectionModel.getMaxSelectionIndex() && index >= 0) {
                painterPanel.setModel(listPanel.getListModel().getElementAt(index));
            } else {
                painterPanel.setModel(null);
            }
        });

        // Connect the painter to the listModel
        painterPanel.getPainter().addChangeListener((sender, event) -> {
            if (event == GlyphView.ViewChangeEvent.GLYPH_MODIFIED) {
                final var index = selectionModel.getMinSelectionIndex();
                if (index >= 0 && painterPanel.getModel() == listPanel.getListModel().getElementAt(index)) {
                    listPanel.getListModel().requestEvent(index);
                }
            }
        });

        Messenger.get(CopyFromMessage.class).register(this);
    }

    @Override
    public void detach() {
        listPanel.detach();
        painterPanel.detach();
        Messenger.get(CopyFromMessage.class).unregister(this);
    }

    @Override
    public Void receive(final CopyFromMessage message) {
        final var source = project.getGlyphs().findId(message.source());
        if (source == null) {
            return null;
        }
        final var currentModel = painterPanel.getModel();
        painterPanel.setModel(source);
        painterPanel.getActions().clipboardCopyAction.actionPerformed(null);
        for (var i : message.targets()) {
            final var target = project.getGlyphs().findId(i);
            if (target == null) {
                continue;
            }
            painterPanel.setModel(target);
            painterPanel.getActions().clipboardPasteAction.actionPerformed(null);
        }
        painterPanel.setModel(currentModel);
        return null;
    }
}
