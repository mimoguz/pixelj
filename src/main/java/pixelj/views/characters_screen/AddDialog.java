package pixelj.views.characters_screen;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import pixelj.models.BlockData;
import pixelj.models.CharacterData;
import pixelj.resources.Resources;
import pixelj.views.controls.SearchableComboBox;
import pixelj.views.shared.Borders;
import pixelj.views.shared.CharacterDataCellRenderer;
import pixelj.views.shared.Components;
import pixelj.views.shared.Dimensions;

public class AddDialog extends JDialog {
    private final DefaultListModel<CharacterData> listModel = new DefaultListModel<>();
    private final ArrayList<CharacterData> result = new ArrayList<>();
    private final ListSelectionModel selectionModel = new DefaultListSelectionModel();

    public AddDialog(final Frame owner) {
        super(
                owner,
                Resources.get().getString("charactersAddDialogTitle"),
                Dialog.ModalityType.APPLICATION_MODAL
        );

        final var res = Resources.get();

        final JList<CharacterData> list = new JList<>();
        selectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setModel(listModel);
        list.setSelectionModel(selectionModel);
        list.setCellRenderer(new CharacterDataCellRenderer());

        final var scrollPanel = new JScrollPane(list);
        scrollPanel.setMaximumSize(Dimensions.MAXIMUM);

        final var filterBox = new SearchableComboBox<>(res.getBlocks().stream().skip(1).toList());
        filterBox.setSelectedIndex(0);
        if (filterBox.getSelectedItem() instanceof final BlockData block) {
            listModel.addAll(res.getCharacters(block.id()));
        }
        filterBox.addActionListener(event -> {
            if (filterBox.getSelectedItem() instanceof final BlockData block) {
                listModel.clear();
                listModel.addAll(res.getCharacters(block.id()));
            }
        });

        final var addButton = new JButton(res.getString("add"));
        Components.setFixedSize(addButton, Dimensions.TEXT_BUTTON_SIZE);
        addButton.setEnabled(false);
        addButton.addActionListener(event -> {
            fillResult();
            setVisible(false);
        });
        selectionModel.addListSelectionListener(
                e -> addButton.setEnabled(selectionModel.getMinSelectionIndex() >= 0)
        );

        final var cancelButton = new JButton(res.getString("cancel"));
        Components.setFixedSize(cancelButton, Dimensions.TEXT_BUTTON_SIZE);
        cancelButton.addActionListener(event -> setVisible(false));

        final var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonPanel.add(cancelButton);

        final var content = new JPanel();
        content.setLayout(new BorderLayout(Dimensions.MEDIUM_PADDING, Dimensions.MEDIUM_PADDING));

        content.add(filterBox, BorderLayout.NORTH);
        content.add(scrollPanel, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);
        content.setBorder(Borders.MEDIUM_EMPTY);
        setContentPane(content);
        getRootPane().setDefaultButton(cancelButton);

        pack();
        list.requestFocusInWindow();
        setSize(400, 600);
        setResizable(true);
    }

    public Collection<CharacterData> getResult() {
        return Collections.unmodifiableCollection(result);
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            result.clear();
            selectionModel.clearSelection();
            setLocationRelativeTo(getOwner());
        }
        super.setVisible(visible);
    }

    private void fillResult() {
        final var indices = selectionModel.getSelectedIndices();
        for (final var i : indices) {
            result.add(listModel.get(i));
        }
    }
}
