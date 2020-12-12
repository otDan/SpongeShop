package animation;

@Deprecated
public interface Animatable<T> {

    /**
     * Tells the holder to process the next frame.
     *
     * @param frame the frame
     */
    void nextFrame(T frame);

}