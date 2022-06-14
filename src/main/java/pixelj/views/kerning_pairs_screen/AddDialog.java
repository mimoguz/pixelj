package pixelj.views.kerning_pairs_screen;

import pixelj.views.controls.SearchableComboBox;
import pixelj.models.*;
import pixelj.resources.Resources;
import pixelj.views.shared.*;

import javax.swing.*;
import java.awt.*;

public class AddDialog extends JDialog {
    private final ListSelectionModel selectionModel = new DefaultListSelectionModel();
    private CharacterItem left;
    private KerningPair result;
    private CharacterItem right;

    public AddDialog(final SortedList<CharacterItem> source, final Frame owner) {
        super(
                owner,
                Resources.get().getString("kerningPairsAddDialogTitle"),
                Dialog.ModalityType.APPLICATION_MODAL
        );

        final var res = Resources.get();

        final var listModel = new FilteredList<>(source);
        final JList<CharacterItem> list = new JList<>();
        list.setModel(listModel);
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectionModel(selectionModel);
        list.setCellRenderer(new CharacterCellRenderer(Dimensions.MAXIMUM_PREVIEW_SIZE));

        final var scrollPanel = new JScrollPane(list);
        scrollPanel.setMaximumSize(Dimensions.MAXIMUM);

        final var filterBox = new SearchableComboBox<>(res.getBlocks());
        filterBox.setSelectedIndex(0);
        filterBox.addActionListener(event -> {
            if (filterBox.getSelectedItem() instanceof BlockData block) {
                listModel.setFilter(
                        chr -> chr.getCodePoint() >= block.starts() && chr.getCodePoint() <= block.ends()
                );
            }
        });

        final var leftView = new CharacterModelCell(Dimensions.MAXIMUM_PREVIEW_SIZE);
        final var rightView = new CharacterModelCell(Dimensions.MAXIMUM_PREVIEW_SIZE);

        final var addButton = new JButton(res.getString("add"));
        Components.setFixedSize(addButton, Dimensions.TEXT_BUTTON_SIZE);
        addButton.setEnabled(false);
        addButton.addActionListener(event -> {
            if (left != null && right != null) {
                result = new KerningPair(left, right, 0);
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

        final var centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPanel, BorderLayout.CENTER);

        final var selectionPanel = new JPanel();
        selectionPanel.setBorder(Borders.EMPTY);
        final var selectionPanelLayout = new GroupLayout(selectionPanel);
        selectionPanel.setLayout(selectionPanelLayout);

        selectionPanelLayout.setHorizontalGroup(
                selectionPanelLayout.createSequentialGroup()
                        .addGroup(
                                selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(setLeftButton)
                                        .addComponent(setRightButton)
                        )
                        .addGroup(
                                selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(
                                                leftView,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                Short.MAX_VALUE
                                        )
                                        .addComponent(
                                                rightView,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE,
                                                Short.MAX_VALUE
                                        )
                        )
        );

        selectionPanelLayout.setVerticalGroup(
                selectionPanelLayout.createSequentialGroup()
                        .addGroup(
                                selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(setLeftButton)
                                        .addComponent(leftView, 64, 64, 64)
                        )
                        .addGroup(
                                selectionPanelLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(setRightButton)
                                        .addComponent(rightView, 64, 64, 64)
                        )
        );

        centerPanel.add(selectionPanel, BorderLayout.SOUTH);

        final var buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addButton);
        buttonPanel.add(Box.createRigidArea(Dimensions.MEDIUM_SQUARE));
        buttonPanel.add(cancelButton);

        final var content = new JPanel();
        content.setLayout(new BorderLayout(Dimensions.MEDIUM_PADDING, Dimensions.MEDIUM_PADDING));

        content.add(filterBox, BorderLayout.NORTH);
        content.add(centerPanel, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);
        content.setBorder(Borders.MEDIUM_EMPTY);
        setContentPane(content);
        getRootPane().setDefaultButton(cancelButton);

        pack();
        list.requestFocusInWindow();
        setSize(400, 600);
        setResizable(true);
    }

    public KerningPair getResult() {
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
