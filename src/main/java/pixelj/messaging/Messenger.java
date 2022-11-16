package pixelj.messaging;

import pixelj.util.Receiver;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * A very simple in-thread message dispatcher that uses weak references to the receivers.
 **/
public class Messenger<T> {

    private static final Map<Class<?>, Messenger<?>> MESSENGERS = new HashMap<>();

    private final WeakHashMap<Receiver<T>, Void> receivers = new WeakHashMap<>();

    public void register(Receiver<T> receiver) {
        receivers.put(receiver, null);
    }

    public void unregister(Receiver<T> receiver, Class<T> messageType) {
        receivers.remove(receiver);
        removeUnused(messageType);
    }

    public void send(T message) {
        receivers.keySet().forEach(receiver -> {
            if (receiver != null) {
                receiver.receive(message);
            }
        });
    }

    public static <U> Messenger<U> forClass(final Class<U> messageClass) {
        final var messenger = MESSENGERS.get(messageClass);
        if (messenger != null) {
            return (Messenger<U>) messenger;
        } else {
            final var messengerU = new Messenger<U>();
            MESSENGERS.put(messageClass, messengerU);
            return messengerU;
        }
    }

    private static void removeUnused(Class<?> messageClass) {
        if (MESSENGERS.get(messageClass).receivers.isEmpty()) {
            MESSENGERS.remove(messageClass);
        }
    }
}
