package pixelj.models;

public interface HasId {

    /**
     * hashCode is an int, not wide enough to not get collisions. This returns a long.
     *
     * @return Long hash code.
     */
    long getId();
}
