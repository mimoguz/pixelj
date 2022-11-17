package pixelj.views.projectwindow.kerningpairspage;

import javax.swing.JFrame;

import pixelj.messaging.ProjectModifiedMessage;
import pixelj.models.Project;
import pixelj.util.Detachable;
import pixelj.messaging.Messenger;

public final class KerningPairsPage extends KerningPairsPageBase implements Detachable {

    public KerningPairsPage(final Project project, final JFrame window) {
        super(new EditorPanel(project), new ListPanel(project, window));

        // Connect the listModel to the editor
        final var selectionModel = listPanel.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            final var index = selectionModel.getMinSelectionIndex();
            if (index == selectionModel.getMaxSelectionIndex() && index >= 0) {
                // Set the editor panel model when one and only one item is selected:
                editorPanel.setModel(listPanel.getListModel().getElementAt(index));
            } else {
                editorPanel.setModel(null);
            }
        });

        editorPanel.valueSpinner.addChangeListener(e -> Messenger
            .messengerFor(ProjectModifiedMessage.class)
            .send(ProjectModifiedMessage.get()));
    }

    @Override
    public void detach() {
        listPanel.detach();
    }
}
