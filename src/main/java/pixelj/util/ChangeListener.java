package pixelj.util;

import java.util.EventListener;

public interface ChangeListener<S, A> extends EventListener {
    void onChange(S sender, A args);
}
