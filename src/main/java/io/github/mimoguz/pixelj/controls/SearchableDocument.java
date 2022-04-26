package io.github.mimoguz.pixelj.controls;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.Objects;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;

import com.formdev.flatlaf.FlatClientProperties;

/**
 * An implementation of Thomas Bierhance's auto-complete combo-box:<br />
 * <a href=
 * "http://www.orbital-computer.de/JComboBox/">http://www.orbital-computer.de/JComboBox/</a>
 */
class SearchableDocument<E> extends PlainDocument {
    private enum BackspaceState {
        ERROR, HIT, HIT_ON_SELECTION, NO_HIT
    }

    private static final long serialVersionUID = -7793331737675680882L;

    private BackspaceState backspaceState = BackspaceState.NO_HIT;
    private final JComboBox<E> comboBox;
    private final JTextComponent editor;
    private final ComboBoxModel<E> model;

    private boolean selecting = false;

    SearchableDocument(final JComboBox<E> comboBox) {
        this.comboBox = comboBox;
        model = comboBox.getModel();
        editor = (JTextComponent) comboBox.getEditor().getEditorComponent();
        comboBox.setEditable(true);

        // Highlight text when the user select an item using mouse
        comboBox.addActionListener(e -> {
            if (!selecting) {
                highlightCompletedText(0);
            }
        });

        // Show pop-up on key event
        // Handle backspace key
        editor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                if (comboBox.isDisplayable()) {
                    comboBox.setPopupVisible(true);
                }

                backspaceState = switch (e.getKeyCode()) {
                    case KeyEvent.VK_BACK_SPACE -> editor.getSelectionStart() != editor.getSelectionEnd()
                            ? BackspaceState.HIT_ON_SELECTION
                            : BackspaceState.HIT;
                    case KeyEvent.VK_DELETE -> BackspaceState.ERROR;
                    default -> BackspaceState.NO_HIT;
                };

                // Ignore error state
                if (backspaceState == BackspaceState.ERROR) {
                    e.consume();
                }
            }
        });

        // Show the selected item on startup
        final var selected = comboBox.getSelectedItem();
        if (selected != null) {
            setText(selected.toString());
        }
        highlightCompletedText(0);

        // Handle focus
        editor.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(final FocusEvent e) {
                clearError();
                if (comboBox.isDisplayable()) {
                    comboBox.setPopupVisible(true);
                }
                highlightCompletedText(0);
            }

            @Override
            public void focusLost(final FocusEvent e) {
                clearError();
                comboBox.setPopupVisible(false);
            }
        });
    }

    @Override
    public void insertString(final int offset, final String string, final AttributeSet attributes)
            throws BadLocationException {
        if (selecting) {
            return;
        }
        clearError();
        super.insertString(offset, string, attributes);
        final var pattern = getText(0, offset + 1);
        final var item = lookupItem(pattern);
        if (item != null) {
            setSelected(item);
            setText(item.toString());
            highlightCompletedText(offset + string.length());
        } else {
            notifyError();
            setText(Objects.toString(comboBox.getSelectedItem()));
            highlightCompletedText(offset);
        }
    }

    @Override
    public void remove(final int offset, final int length) throws BadLocationException {
        if (selecting) {
            return;
        }
        switch (backspaceState) {
            case HIT_ON_SELECTION:
                if (offset > 0) {
                    highlightCompletedText(offset - 1);
                }
                break;
            case HIT:
                if (offset > 0) {
                    highlightCompletedText(offset);
                }
                break;
            case ERROR:
                break;
            default:
                super.remove(offset, length);
        }
    }

    private void clearError() {
        comboBox.putClientProperty(FlatClientProperties.OUTLINE, null);
    }

    private void highlightCompletedText(int offset) {
        editor.setSelectionStart(offset);
        editor.setSelectionEnd(getLength());
        editor.setCaretPosition(getLength());
        editor.moveCaretPosition(offset);
    }

    private Object lookupItem(final String pattern) {
        final var pat = pattern.toLowerCase(Locale.US);
        for (var index = 0; index < model.getSize(); index++) {
            final var element = model.getElementAt(index);
            if (element != null && element.toString().toLowerCase(Locale.US).startsWith(pat)) {
                return element;
            }
        }
        return null;
    }

    private void notifyError() {
        comboBox.putClientProperty(FlatClientProperties.OUTLINE, FlatClientProperties.OUTLINE_ERROR);
        comboBox.getToolkit().beep();
    }

    private void setSelected(final Object item) {
        selecting = true;
        model.setSelectedItem(item);
        selecting = false;
    }

    private void setText(final String text) {
        try {
            super.remove(0, getLength());
            super.insertString(0, text, null);
        } catch (BadLocationException e) {
            System.err.println(e.getMessage());
        }
    }
}
