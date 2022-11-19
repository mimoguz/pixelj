package pixelj.messaging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * A very simple in-thread message dispatcher that uses weak references to the receivers.
 **/
public class Messenger<M, R> {

    private static final Map<Class<?>, Messenger<?, ?>> MESSENGERS = new HashMap<>();

    private final WeakHashMap<Receiver<M, R>, Void> receivers = new WeakHashMap<>();

    public void register(Receiver<M, R> receiver) {
        receivers.put(receiver, null);
    }

    public void unregister(Receiver<M, ?> receiver) {
        receivers.remove(receiver);
    }

    public void send(final M message) {
        receivers.keySet().forEach(receiver -> {
            if (receiver != null) {
                receiver.receive(message);
            }
        });
    }

    @SuppressWarnings("unused")
    public List<R> askAll(final M question) {
        return receivers
            .keySet()
            .stream()
            .map(r -> r.receive(question))
            .toList();
    }

    public R askOne(final M question) {
        return receivers
            .keySet()
            .stream()
            .findFirst()
            .map(r -> r.receive(question))
            .orElse(null);
    }

    public static <Msg> Messenger<Msg, Void> get(final Class<Msg> messageClass) {
        final var messenger = MESSENGERS.get(messageClass);
        if (messenger != null) {
            //noinspection unchecked
            return (Messenger<Msg, Void>) messenger;
        } else {
            final var messengerMsg = new Messenger<Msg, Void>();
            MESSENGERS.put(messageClass, messengerMsg);
            return messengerMsg;
        }
    }

    public static <Msg, Res> Messenger<Msg, Res> get(
        final Class<Msg> messageClass,
        final Class<Res> ignoredResponseClass
    ) {
        final var messenger = MESSENGERS.get(messageClass);
        if (messenger != null) {
            //noinspection unchecked
            return (Messenger<Msg, Res>) messenger;
        } else {
            final var messengerMsg = new Messenger<Msg, Res>();
            MESSENGERS.put(messageClass, messengerMsg);
            return messengerMsg;
        }
    }

    @SuppressWarnings("unused")
    public static void removeUnused() {
        final var messengerClasses = MESSENGERS.keySet().stream().toList();
        for (var cls : messengerClasses) {
            if (MESSENGERS.get(cls).receivers.isEmpty()) {
                MESSENGERS.remove(cls);
            }
        }
    }
}
