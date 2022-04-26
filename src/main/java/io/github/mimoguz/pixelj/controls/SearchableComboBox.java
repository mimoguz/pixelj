package io.github.mimoguz.pixelj.controls;

import java.util.Collection;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;

import org.eclipse.jdt.annotation.NonNull;

/**
 * An implementation of Thomas Bierhance's auto-complete combo-box:<br />
 * <a href=
 * "http://www.orbital-computer.de/JComboBox/">http://www.orbital-computer.de/JComboBox/</a>
 */
public class SearchableComboBox<E> extends JComboBox<E> {
    private static final long serialVersionUID = 4325541939040411860L;

    public SearchableComboBox() {
        super();
        final var editor = (JTextComponent) getEditor().getEditorComponent();
        editor.setDocument(new SearchableDocument<>(this));
    }

    public SearchableComboBox(@NonNull final Collection<@NonNull E> elements) {
        super(new Vector<>(elements));
        final var editor = (JTextComponent) getEditor().getEditorComponent();
        editor.setDocument(new SearchableDocument<>(this));
    }
}
