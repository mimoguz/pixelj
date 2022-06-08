package io.github.mimoguz.pixelj.views.kerning_pairs_screen;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.*;

import io.github.mimoguz.pixelj.controls.SearchableComboBox;
import io.github.mimoguz.pixelj.models.*;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.shared.*;

public class AddDialog extends JDialog {
    private static final long serialVersionUID = -2069391980463198716L;

    private transient CharacterModel left;
    private transient KerningPairModel result;
    private transient CharacterModel right;
    private final ListSelectionModel selectionModel = new DefaultListSelectionModel();

    public AddDialog(final HashListModel<CharacterModel> source, final Frame owner) {
        super(
                owner,
                Resources.get().getString("kerningPairsAddDialogTitle"),
                Dialog.ModalityType.APPLICATION_MODAL
        );

        final var res = Resources.get();

        final var listModel = new FilteredListModel<>(source);
        final JList<CharacterModel> list = new JList<>();
        list.setModel(listModel);
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectionModel(selectionModel);
        list.setCellRenderer(new CharacterCellRenderer(Dimensions.MAXIMUM_PREVIEW_SIZE));

        final var scrollPanel = new JScrollPane(list);
        scrollPanel.setMaximumSize(Dimensions.MAXIMUM);

        final var filterBox = new SearchableComboBox<>(res.getBlocks());
        filterBox.setSelectedIndex(0);
        filterBox.addActionListener(event -> {
            final var item = filterBox.getSelectedItem();
            try {
                final var block = (BlockData) item;
                listModel.setFilter(
                        chr -> chr.getCodePoint() >= block.starts() && chr.getCodePoint() <= block.ends()
                );
            } catch (final Exception e) {
                // Ignore
            }
        });

        final var leftView = new CharacterModelCell(Dimensions.MAXIMUM_PREVIEW_SIZE);
        final var rightView = new CharacterModelCell(Dimensions.MAXIMUM_PREVIEW_SIZE);

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
                left = source.getElementAt(selectionModel.getMinSelectionIndex());
                leftView.set(left, Integer.MAX_VALUE);
                addButton.setEnabled(right != null);
            }
        });

        final var setRightButton = new JButton(res.getString("setRight"));
        Components.setFixedSize(setRightButton, Dimensions.TEXT_BUTTON_SIZE);
        setRightButton.setEnabled(false);
        setRightButton.addActionListener(event -> {
            if (selectionModel.getMinSelectionIndex() >= 0) {
                right = source.getElementAt(selectionModel.getMinSelectionIndex());
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

        final var selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setBorder(Borders.EMPTY);

        final var leftSelectionPanel = new JPanel();
        leftSelectionPanel.setLayout(new BoxLayout(leftSelectionPanel, BoxLayout.X_AXIS));
        leftSelectionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Dimensions.MAXIMUM_PREVIEW_SIZE));
        leftSelectionPanel.add(setLeftButton);
        leftSelectionPanel.add(leftView);
        leftSelectionPanel.add(Box.createHorizontalGlue());
        leftSelectionPanel.setBorder(Borders.MEDIUM_EMPTY);
        Components.addOuterBorder(
                leftSelectionPanel,
                BorderFactory.createMatteBorder(1, 0, 1, 0, res.colors.divider())
        );

        final var rightSelectionPanel = new JPanel();
        rightSelectionPanel.setLayout(new BoxLayout(rightSelectionPanel, BoxLayout.X_AXIS));
        rightSelectionPanel
                .setMaximumSize(new Dimension(Integer.MAX_VALUE, 48 + Dimensions.SMALL_PADDING * 2));
        rightSelectionPanel.add(setRightButton);
        rightSelectionPanel.add(rightView);
        rightSelectionPanel.add(Box.createHorizontalGlue());
        rightSelectionPanel.setBorder(Borders.MEDIUM_EMPTY);
        Components.addOuterBorder(
                rightSelectionPanel,
                BorderFactory.createMatteBorder(0, 0, 1, 0, res.colors.divider())
        );

        selectionPanel.add(scrollPanel);
        selectionPanel.add(Box.createVerticalStrut(Dimensions.MEDIUM_PADDING));
        selectionPanel.add(leftSelectionPanel);
        selectionPanel.add(rightSelectionPanel);

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
