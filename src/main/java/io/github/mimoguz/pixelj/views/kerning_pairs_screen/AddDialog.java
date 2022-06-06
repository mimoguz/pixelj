package io.github.mimoguz.pixelj.views.kerning_pairs_screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.swing.*;

import io.github.mimoguz.pixelj.controls.SearchableComboBox;
import io.github.mimoguz.pixelj.models.BlockData;
import io.github.mimoguz.pixelj.models.CharacterData;
import io.github.mimoguz.pixelj.models.CharacterListModel;
import io.github.mimoguz.pixelj.models.CharacterModel;
import io.github.mimoguz.pixelj.models.KerningPairModel;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.shared.Borders;
import io.github.mimoguz.pixelj.views.shared.CharacterCellRenderer;
import io.github.mimoguz.pixelj.views.shared.CharacterModelCell;
import io.github.mimoguz.pixelj.views.shared.Components;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

public class AddDialog extends JDialog {
    private static final long serialVersionUID = -2069391980463198716L;

    private transient KerningPairModel result;
    private final ListSelectionModel selectionModel = new DefaultListSelectionModel();
    private transient CharacterModel left;
    private transient CharacterModel right;

    public AddDialog(CharacterListModel listModel, final Frame owner) {
        super(
                owner,
                Resources.get().getString("charactersAddDialogTitle"),
                Dialog.ModalityType.APPLICATION_MODAL
        );

        final var res = Resources.get();

        final JList<CharacterModel> list = new JList<>();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setModel(listModel);
        list.setSelectionModel(selectionModel);
        list.setCellRenderer(new CharacterCellRenderer(48));

        final var scrollPanel = new JScrollPane(list);
        scrollPanel.setMaximumSize(Dimensions.MAXIMUM);

        final var filterBox = new SearchableComboBox<>(res.getBlocks());
        filterBox.setSelectedIndex(0);
        filterBox.addActionListener(event -> {
            final var item = filterBox.getSelectedItem();
            try {
                final var block = (BlockData) item;
                listModel.setRange(block.starts(), block.ends());
            } catch (final Exception e) {
                // Ignore
            }
        });

        final var leftView = new CharacterModelCell(48);
        final var rightView = new CharacterModelCell(48);

        final var addButton = new JButton(res.getString("add"));
        Components.setFixedSize(addButton, Dimensions.TEXT_BUTTON_SIZE);
        addButton.setEnabled(false);
        addButton.addActionListener(event -> {
            if (left != null && right != null) {
                result = new KerningPairModel(left, right, 0);
            }
            setVisible(false);
        });

        final var setLeftButton = new JButton(res.getString("setLeft"));
        Components.setFixedSize(setLeftButton, Dimensions.TEXT_BUTTON_SIZE);
        setLeftButton.setEnabled(false);
        setLeftButton.addActionListener(event -> {
            if (selectionModel.getMinSelectionIndex() >= 0) {
                left = listModel.getElementAt(selectionModel.getMinSelectionIndex());
                leftView.set(left, Integer.MAX_VALUE);
                addButton.setEnabled(right != null);
            }
        });

        final var setRightButton = new JButton(res.getString("setRight"));
        Components.setFixedSize(setRightButton, Dimensions.TEXT_BUTTON_SIZE);
        setRightButton.setEnabled(false);
        setRightButton.addActionListener(event -> {
            if (selectionModel.getMinSelectionIndex() >= 0) {
                right = listModel.getElementAt(selectionModel.getMinSelectionIndex());
                rightView.set(right, Integer.MAX_VALUE);
                addButton.setEnabled(right != null);
            }
        });

        selectionModel.addListSelectionListener(e -> {
            setLeftButton.setEnabled(selectionModel.getMinSelectionIndex() >= 0);
            setRightButton.setEnabled(selectionModel.getMinSelectionIndex() >= 0);
        });

        final var cancelButton = new JButton(res.getString("cancel"));
        Components.setFixedSize(cancelButton, Dimensions.TEXT_BUTTON_SIZE);
        cancelButton.addActionListener(event -> setVisible(false));

        final var selectionPanel = new JPanel(new GridBagLayout());
        selectionPanel.setBackground(Color.CYAN);

        final var cons = new GridBagConstraints();

        cons.gridx = 0;
        cons.gridy = 0;
        cons.gridwidth = 2;
        cons.weighty = 1.0;
        cons.fill = GridBagConstraints.BOTH;
        selectionPanel.add(scrollPanel, cons);

        cons.gridy += 1;
        cons.gridx = 0;
        cons.gridwidth = 1;
        cons.weighty = 0.0;
        cons.weightx = 0.5;
        cons.fill = GridBagConstraints.HORIZONTAL;
        selectionPanel.add(setLeftButton, cons);
        cons.gridx = 1;
        selectionPanel.add(setRightButton, cons);

        cons.gridy += 1;
        cons.gridx = 0;
        selectionPanel.add(leftView, cons);
        cons.gridx = 1;
        selectionPanel.add(rightView, cons);

        final var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonPanel.add(cancelButton);

        final var content = new JPanel();
        content.setLayout(new BorderLayout(Dimensions.MEDIUM_PADDING, Dimensions.MEDIUM_PADDING));

        content.add(filterBox, BorderLayout.NORTH);
        content.add(selectionPanel, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);
        content.setBorder(Borders.MEDIUM_EMPTY);
        setContentPane(content);
        getRootPane().setDefaultButton(cancelButton);

        pack();
        list.requestFocusInWindow();
        setSize(400, 600);
        setResizable(true);
    }

    public KerningPairModel getResult() {
        return result;
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            result = null;
            selectionModel.clearSelection();
            setLocationRelativeTo(getOwner());
        }
        super.setVisible(visible);
    }
}
