package io.github.mimoguz.pixelj.controls;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.util.Collection;
import java.util.Vector;

/**
 * An implementation of Thomas Bierhance's auto-complete combo-box:<br />
 * <a href="http://www.orbital-computer.de/JComboBox/">http://www.orbital-computer.de/JComboBox/</a>
 */
public class SearchableComboBox<E> extends JComboBox<E> {
    public SearchableComboBox(final Collection<E> elements) {
        super(new Vector<>(elements));
        final var editor = (JTextComponent) getEditor().getEditorComponent();
        final var document = new SearchableDocument<E>(this);
        editor.setDocument(document);
    }
}
