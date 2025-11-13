package mvc.model.impl;

import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableModelFactory;
import mvc.model.api.SliceableTypeEnum;
import mvc.model.api.XY;

/**
 * Implementation of SliceableModelFactory.
 */
public class SliceableModelFactoryImpl implements SliceableModelFactory {

    /**
     * {@inheritDoc}
     */
    @Override
    public SliceableModel createSliceable(
        final SliceableTypeEnum type, 
        final XY<Double> positionXY, 
        final XY<Double> velocityXY
        ) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'createSliceable'");
    }
}
