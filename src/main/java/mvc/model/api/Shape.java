package mvc.model.api;
/**
 * Models the geometrical shape of the sliceable.
 */
public interface Shape {
    /**
     * compute the apothem for the SliceHandler.
     * @return the apothem or just the radius of the Shape
     */
    double getApothem();

    /**
     * get the lenght of wich the Shapes are represented.
     * @return the lengh of the radius or side
     */
    double getLenght();

}
