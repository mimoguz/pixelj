package pixelj.util;

import java.util.WeakHashMap;

/**
 * A very simple in-thread message dispatcher that uses weak references to the receivers.
 **/
public class Messenger {
    private static Messenger DEFAULT;

    private final WeakHashMap<Receiver, Void> receivers = new WeakHashMap<>();

    public void register(Receiver receiver) {
        receivers.put(receiver, null);
    }

    public void unregister(Receiver receiver, Class<?> messageType) {
        receivers.remove(receiver);
    }

    public void send(Object message) {
        receivers.keySet().forEach(receiver -> {
            if (receiver != null && message.getClass() == receiver.messageType()) {
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
