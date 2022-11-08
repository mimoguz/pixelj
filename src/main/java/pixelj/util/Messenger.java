package pixelj.util;

import java.util.WeakHashMap;

/**
 * A very simple in-thread message dispatcher that uses weak references to the receivers.
 **/
public class Messenger {
    private static Messenger DEFAULT;

    private final WeakHashMap<Receiver, Class<?>> receivers = new WeakHashMap<>();

    public void register(Receiver receiver, Class<?> messageType) {
        receivers.put(receiver, messageType);
    }

    public void unregister(Receiver receiver, Class<?> messageType) {
        receivers.remove(receiver);
    }

    public void send(Object message) {
        receivers.forEach((receiver, messageType) -> {
            if (receiver != null && message.getClass() == messageType) {
                receiver.receive(message);
            }
        });
    }

    public static Messenger getDefault() {
        if (DEFAULT == null) {
            DEFAULT = new Messenger();
        }
        return DEFAULT;
    }
}
