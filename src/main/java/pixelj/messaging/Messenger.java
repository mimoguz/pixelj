package pixelj.messaging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * A very simple in-thread message dispatcher that uses weak references to the receivers.
 **/
public class Messenger<M> {

    private static final Map<Class<?>, Messenger<?>> MESSENGERS = new HashMap<>();

    private final WeakHashMap<Receiver<M>, Void> receivers = new WeakHashMap<>();
    private final WeakHashMap<Responder<M>, Void> responders = new WeakHashMap<>();

    public void register(Receiver<M> receiver) {
        receivers.put(receiver, null);
    }

    public void register(Responder<M> responder) {
        responders.put(responder, null);
    }

    public void unregister(Receiver<M> receiver) {
        receivers.remove(receiver);
    }

    public void unregister(Responder<M> responder) {
        responders.remove(responder);
    }

    public void send(final M message) {
        receivers.keySet().forEach(receiver -> {
            if (receiver != null) {
                receiver.receive(message);
            }
        });
    }

    public List<Object> ask(final M question) {
        return responders.keySet().stream().map(r -> r.answer(question)).toList();
    }

    public static <T> Messenger<T> messengerFor(final Class<T> messageClass) {
        final var messenger = MESSENGERS.get(messageClass);
        if (messenger != null) {
            return (Messenger<T>) messenger;
        } else {
            final var messengerU = new Messenger<T>();
            MESSENGERS.put(messageClass, messengerU);
            return messengerU;
        }
    }

    public static <Q> Messenger<Q> responderFor(final Class<Q> questionClass) {
        final var responder = MESSENGERS.get(questionClass);
        if (responder != null) {
            return (Messenger<Q>) responder;
        } else {
            final var responderQ = new Messenger<Q>();
            MESSENGERS.put(questionClass, responderQ);
            return responderQ;
        }
    }

    public static <T> void sendTo(T message, final Class<T> messageClass) {
        final var messenger = messengerFor(messageClass);
        messenger.send(message);
    }

    public static <Q> List<Object> askTo(Q question, final Class<Q> messageClass) {
        final var messenger = responderFor(messageClass);
        return messenger.ask(question);
    }

    public static void removeUnused() {
        final var messengerClasses = MESSENGERS.keySet().stream().toList();
        for (var cls :messengerClasses) {
            if (MESSENGERS.get(cls).receivers.isEmpty() && MESSENGERS.get(cls).responders.isEmpty()) {
                MESSENGERS.remove(cls);
            }
        }
    }
}
