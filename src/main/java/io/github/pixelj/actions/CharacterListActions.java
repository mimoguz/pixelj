package io.github.pixelj.actions;

import io.github.pixelj.resources.Icons;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.List;

public class CharacterListActions {
    public static final ApplicationAction showAddDialogAction = new ApplicationAction(
            "showAddDialog",
            (e) -> System.out.println("Show add dialog action"),
            "showAddDialogAction",
            null,
            Icons.FILE_NEW,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)
    );

    public static final ApplicationAction showRemoveDialogAction = new ApplicationAction(
            "showRemoveDialog",
            (e) -> System.out.println("Show remove dialog action"),
            "showRemoveDialogAction",
            null,
            Icons.FILE_NEW,
            null,
            null,
            KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)
    );

    public static final Collection<ApplicationAction> all = List.of(showAddDialogAction, showRemoveDialogAction);
}
