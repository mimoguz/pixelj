package pixelj.views.projectwindow.glyphspage;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import pixelj.actions.ApplicationAction;
import pixelj.messaging.CopyFromMessage;
import pixelj.messaging.Messenger;
import pixelj.models.Block;
import pixelj.models.FilteredList;
import pixelj.models.Glyph;
import pixelj.models.Project;
import pixelj.resources.Icon;
import pixelj.resources.Resources;
import pixelj.views.shared.Dimensions;
import pixelj.views.shared.GlyphCellRenderer;
import pixelj.views.shared.Help;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class CopyDialog extends CopyDialogBase {

    private final ListSelectionModel selectionModel = new DefaultListSelectionModel();
    private IntList targets = new IntArrayList();
    private final FilteredList<Glyph> listModel;

    public CopyDialog(final Frame owner, Project project) {
        super(owner);

        listModel = new FilteredList<>(project.getGlyphs());
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        glyphList.setSelectionModel(selectionModel);
        glyphList.setModel(listModel);
        glyphList.setCellRenderer(new GlyphCellRenderer(Dimensions.MAXIMUM_PREVIEW_SIZE));

        filterBox.setSelectedIndex(0);
        filterBox.addActionListener(event -> {
            if (filterBox.getSelectedItem() instanceof final Block block) {
                listModel.setFilter(
                    chr -> chr.getCodePoint() >= block.starts() && chr.getCodePoint() <= block.ends()
                );
            }
        });

        helpButton.setAction(new ApplicationAction(
                "copyFromDialogHelpAction",
                (e, action) -> Help.showPage(Help.Page.GLYPHS)
            )
                .setIcon(Icon.HELP)
                .setTooltip(Resources.get().getString("help"))
        );

        cancelButton.addActionListener(event -> setVisible(false));

        copyButton.addActionListener(event -> {
            sendMessage();
            setVisible(false);
        });
        copyButton.setEnabled(false);

        selectionModel.addListSelectionListener(e -> {
            copyButton.setEnabled(selectionModel.getMinSelectionIndex() >= 0);
        });
    }

    public void setTargets(int[] codePoints) {
        targets = IntList.of(codePoints);
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            selectionModel.clearSelection();
        }
        super.setVisible(visible);
    }

    private void sendMessage() {
        if (selectionModel.getMinSelectionIndex() >= 0 && targets.size() > 0) {
            final var source = listModel.getElementAt(selectionModel.getMinSelectionIndex());
            Messenger
                .get(CopyFromMessage.class)
                .send(new CopyFromMessage(source.getCodePoint(), targets.toArray(new int[]{})));
        }
    }
}
