package mvc.model.api;

/**
 * Models the geometrical shape of the sliceable.
 */
@FunctionalInterface
public interface Shape {
    /**
     * compute the apothem for the SliceHandler.
     *
     * @return the apothem or just the radius of the Shape
     */
    double getApothem();
}
