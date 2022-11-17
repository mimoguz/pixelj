package pixelj.messaging;

public interface Responder<Q> {
    Object answer(Q message);
}
