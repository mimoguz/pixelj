package io.github.mimoguz.pixelj.views.characters_screen;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.swing.*;

import io.github.mimoguz.pixelj.controls.SearchableComboBox;
import io.github.mimoguz.pixelj.models.BlockData;
import io.github.mimoguz.pixelj.models.CharacterData;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.shared.Borders;
import io.github.mimoguz.pixelj.views.shared.Components;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

public class AddDialog extends JDialog {
    private static final long serialVersionUID = 7882899053849031516L;

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
        listModel.addAll(res.getCharacters(((BlockData) filterBox.getSelectedItem()).id()));
        filterBox.addActionListener(event -> {
            final var item = filterBox.getSelectedItem();
            try {
                final var block = (BlockData) item;
                listModel.clear();
                listModel.addAll(res.getCharacters(block.id()));
            } catch (final Exception e) {
                // Ignore
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
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonPanel.add(addButton);

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
    public void setVisible(final boolean value) {
        if (value) {
            result.clear();
            selectionModel.clearSelection();
            setLocationRelativeTo(getOwner());
        }
        super.setVisible(value);
    }

    private void fillResult() {
        final var indices = selectionModel.getSelectedIndices();
        for (final var i : indices) {
            result.add(listModel.get(i));
        }
    }
}
