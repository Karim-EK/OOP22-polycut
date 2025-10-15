package mvc.view.api;

import java.io.IOException;
import java.awt.geom.Point2D;

import mvc.model.api.SliceableTypeEnum;
import mvc.view.api.SliceableFactoryView;

@FunctionalInterface
public interface SliceableFactoryView {
    /**
     * Creates a SliceableView instance.
     * @param point2d position.
     * @param sliceableTypeEnum type of sliceable.
     * @return a new {@link mvc.view.impl.SliceableViewImpl}.
     * @throws IOException 
     */
    SliceableView createSliceable(Point2D point2d, SliceableTypeEnum sliceableTypeEnum) throws IOException;
}