package mvc.controller.impl;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mvc.controller.api.GameWorldController;
import mvc.model.api.SliceableModelFactory;
import mvc.model.api.SliceableModel;
import mvc.model.impl.SliceableModelFactoryImpl;
import mvc.view.impl.GameScreenImpl;
import mvc.view.api.SliceableView;
import mvc.view.api.SliceableFactoryView;
import mvc.view.impl.SliceableFactoryViewImpl;;


/**
 * Implementation class of the GameWorld controller.
 * Check GameWorldController documentation.
 */
public class GameWorldControllerImpl implements GameWorldController {

    private final SliceableModelFactory factory;
    private final SliceableFactoryView factor;
    private Map<SliceableModel, SliceableView> sliceableMap;

    /**
     * Constructor of the game world.
     */
    public GameWorldControllerImpl() {
        this.factory = new SliceableModelFactoryImpl();
        this.factor = new SliceableFactoryViewImpl();
        this.sliceableMap = new HashMap<>();
    }

    @Override
    public Map<SliceableModel, SliceableView> getSliceableMap() {
        return this.sliceableMap;
    }

    @Override
    public void updateSliceablePosition(Map<SliceableModel, SliceableView> sliceableMap, Point2D newPosition) {
        // TODO: Calcola/Delega calcolo nuova posizione
    }

    @Override
    public void createSliceable() {
        sliceableMap.put(factory.createSliceable(),null );
    }

    @Override
    public void startLoop() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'startLoop'");
    }

    @Override
    public void deleteSliceable(Pair<SliceableModel, SliceableView> sliceable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteSliceable'");
    }
}
