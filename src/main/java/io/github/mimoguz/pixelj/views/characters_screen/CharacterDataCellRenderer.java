package io.github.mimoguz.pixelj.views.characters_screen;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.*;

import com.formdev.flatlaf.FlatClientProperties;

import io.github.mimoguz.pixelj.models.CharacterData;
import io.github.mimoguz.pixelj.resources.Resources;
import io.github.mimoguz.pixelj.views.shared.Borders;
import io.github.mimoguz.pixelj.views.shared.Dimensions;

public class CharacterDataCellRenderer implements ListCellRenderer<CharacterData> {
    private final JPanel component = new JPanel();
    private final JLabel letter = new JLabel();
    private final JLabel subtitle = new JLabel();
    private final JLabel title = new JLabel();
    private final JPanel titleBox = new JPanel();

    public CharacterDataCellRenderer() {
        letter.setMinimumSize(Dimensions.LETTER_BOX_SIZE);
        letter.setPreferredSize(Dimensions.LETTER_BOX_SIZE);

        subtitle.putClientProperty(FlatClientProperties.STYLE_CLASS, "small");

        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.Y_AXIS));
        titleBox.setOpaque(false);
        titleBox.setBackground(new Color(0, 0, 0, 0));
        titleBox.add(Box.createVerticalGlue());
        titleBox.add(subtitle);
        titleBox.add(title);
        titleBox.add(Box.createVerticalGlue());

        component.setLayout(new BoxLayout(component, BoxLayout.X_AXIS));
        component.setBorder(Borders.LIST_ITEM);
        component.add(letter);
        component.add(Box.createHorizontalStrut(8));
        component.add(titleBox);
        component.add(Box.createHorizontalGlue());
    }

    @Override
    public Component getListCellRendererComponent(
            final JList<? extends CharacterData> list,
            final CharacterData value,
            final int index,
            final boolean isSelected,
            final boolean cellHasFocus
    ) {
        title.setMaximumSize(
                new Dimension(
                        list.getWidth() - Dimensions.LETTER_BOX_SIZE.width - Dimensions.MEDIUM_PADDING * 3,
                        Integer.MAX_VALUE
                )
        );
        set(value);
        if (isSelected) {
            setBackgroundColor(list.getSelectionBackground());
            setForegroundColor(list.getSelectionForeground());
        } else {
            setBackgroundColor(list.getBackground());
            setForegroundColor(list.getForeground());
        }
        return component;
    }

    public void set(final CharacterData data) {
        title.setText(Resources.get().getCharacterData(data.codePoint()).name());
        subtitle.setText("0x" + Integer.toHexString(data.codePoint()));
        letter.setText(Character.toString((char) data.codePoint()));
    }

    public void setBackgroundColor(final Color color) {
        titleBox.setBackground(color);
        component.setBackground(color);
    }

    public void setForegroundColor(final Color color) {
        title.setForeground(color);
        subtitle.setForeground(color);
        letter.setForeground(color);
        component.setForeground(color);
    }
}