package pixelj.util;

import java.util.EventListener;

public interface ChangeListener<S, A> extends EventListener {

    /**
     * @param sender Event source
     * @param args   Event arguments
     */
    void onChange(S sender, A args);
}
