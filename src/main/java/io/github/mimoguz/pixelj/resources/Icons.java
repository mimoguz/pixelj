package io.github.mimoguz.pixelj.resources;

public enum Icons {
    CLIPBOARD_COPY(0xe900),
    CLIPBOARD_CUT(0xe901),
    CLIPBOARD_IMPORT(0xe902),
    CLIPBOARD_PASTE(0xe903),
    ELLIPSIS(0xe919),
    ERASE(0xe910),
    EXIT(0xe904),
    EYE(0xe91c),
    FILE_EXPORT(0xe905),
    FILE_NEW(0xe906),
    FILE_OPEN(0xe907),
    FILE_SAVE(0xe908),
    FILE_SAVE_AS(0xe909),
    FILTER(0xe91d),
    FLIP_HORIZONTAL(0xe90a),
    FLIP_VERTICAL(0xe90b),
    HELP(0xe90c),
    HISTORY_REDO(0xe90d),
    HISTORY_UNDO(0xe90e),
    KERNING(0xe91b),
    LIST(0xe91a),
    METRICS(0xe90f),
    MOVE_DOWN(0xe911),
    MOVE_LEFT(0xe912),
    MOVE_RIGHT(0xe913),
    MOVE_UP(0xe914),
    ROTATE_LEFT(0xe915),
    ROTATE_RIGHT(0xe916),
    SETTINGS(0xe917),
    SYMMETRY(0xe918);

    public final int codePoint;

    Icons(int codePoint) {
        this.codePoint = codePoint;
    }
}
