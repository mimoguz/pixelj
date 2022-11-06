package pixelj.resources;


import com.formdev.flatlaf.extras.FlatSVGIcon;

public enum Icon {

    CLIPBOARD_COPY("pxf_clipboard_copy.svg"),
    CLIPBOARD_CUT("pxf_clipboard_cut.svg"),
    CLIPBOARD_IMPORT("pxf_clipboard_import.svg"),
    CLIPBOARD_PASTE("pxf_clipboard_paste.svg"),
    ELLIPSIS("pxf_ellipsis.svg"),
    ERASE("pxf_erase.svg"),
    EXIT("pxf_exit.svg"),
    EYE("pxf_eye.svg"),
    FILE_EXPORT("pxf_file_export.svg"),
    FILE_OPEN("pxf_file_open.svg"),
    FILE_SAVE_AS("pxf_file_save_as.svg"),
    FILE_SAVE("pxf_file_save.svg"),
    FLIP_HORIZONTAL("pxf_flip_horizontal.svg"),
    FLIP_VERTICAL("pxf_flip_vertical.svg"),
    HELP("pxf_help.svg"),
    HISTORY_REDO("pxf_history_redo.svg"),
    HISTORY_UNDO("pxf_history_undo.svg"),
    HOME("pxf_home.svg"),
    KERNING_WIDE("pxf_kerning_wide.svg"),
    LIST("pxf_list.svg"),
    METRICS("pxf_metrics.svg"),
    MOVE_DOWN("pxf_move_down.svg"),
    MOVE_LEFT("pxf_move_left.svg"),
    MOVE_RIGHT("pxf_move_right.svg"),
    MOVE_UP("pxf_move_up.svg"),
    NUMBER("pxf_number.svg"),
    REMOVE_ITEM("pxf_remove_item.svg"),
    ROTATE_LEFT("pxf_rotate_left.svg"),
    ROTATE_RIGHT("pxf_rotate_right.svg"),
    SETTINGS("pxf_settings.svg"),
    SYMMETRY("pxf_symmetry.svg"),
    ZOOM_IN("pxf_zoom_in.svg"),
    ZOOM_OUT("pxf_zoom_out.svg");

    private final String file;

    Icon(final String path) {
        this.file = path;
    }

    public String getPath() {
        return file;
    }

    public FlatSVGIcon createIcon(boolean isDark) {
        return new FlatSVGIcon("pixelj/resources/svg/" + (isDark ? "dark/" : "light/") +  file, 16, 16);
    }


}
