package pixelj.views.projectwindow;

import org.lwjgl.system.MemoryUtil;
import org.lwjgl.util.nfd.NativeFileDialog;
import pixelj.actions.ApplicationAction;
import pixelj.resources.Resources;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOError;
import java.nio.BufferUnderflowException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SvgExportDialog extends SvgExportDialogBase {

    private Path result;

    public SvgExportDialog(final Frame owner) {
        super(owner);

        selectPathButton.addActionListener((e) -> {
            final var path = showSelectDirectoryDialog();
            if (path != null) {
                pathField.setText(path.toString());
            }
        });

        cancelButton.addActionListener((e) -> setVisible(false));

        exportButton.addActionListener((e) -> {
            final var dir = Paths.get(pathField.getText());
            if (dir.toFile().isDirectory()) {
                result = dir;
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, Resources.get().getString("notDirectoryErrorMessage"));
            }
        });

        pathField.getDocument().addDocumentListener(
            new DocumentListener() {
                @Override
                public void insertUpdate(final DocumentEvent e) {
                    check();
                }

                @Override
                public void removeUpdate(final DocumentEvent e) {
                    check();
                }

                @Override
                public void changedUpdate(final DocumentEvent e) {
                    check();
                }

                private void check() {
                    final var text = pathField.getText();
                    exportButton.setEnabled(text != null && !text.isBlank());
                }
            }
        );
    }


    private Path showSelectDirectoryDialog() {
        final var outPath = MemoryUtil.memAllocPointer(1);
        try {
            if (NativeFileDialog.NFD_PickFolder("", outPath) == NativeFileDialog.NFD_OKAY) {
                final var pathStr = outPath.getStringUTF8();
                NativeFileDialog.nNFD_Free(outPath.get(0));
                return Path.of(pathStr);
            } else {
                return null;
            }
        } catch (IOError | SecurityException | BufferUnderflowException | InvalidPathException e) {
            e.printStackTrace();
            return null;
        } finally {
            MemoryUtil.memFree(outPath);
        }
    }

    public Path getResult() {
        return result;
    }
}