package mvc.controller.impl;

import java.util.HashSet;
import java.util.Set;

import mvc.controller.api.GameWorldController;
import mvc.model.api.GameWorld;
import mvc.model.api.SliceableModel;
import mvc.model.api.XY;


public class GameWorldControllerImpl implements GameWorldController {

    public static final double WIDTH = 160.0;
    public static final double HEIGHT = 90.0;

    private final GameWorld model;

    public GameWorldControllerImpl(GameWorld model) {
        this.model = model;
    }

    @Override
    public void update(double deltaTime) {
        this.model.update(deltaTime);
    }

    @Override
    public void processSwipe(XY<Double> initialXy, XY<Double> finalXy) {
        this.model.processSwipe(initialXy, finalXy);
    }

    @Override
    public Set<SliceableModel> getVisibleSliceables() {
        return new HashSet<>(this.model.getActiveSliceables());
    }

    @Override
    public boolean isGameOver() {
        return model.isGameover();
    }

    @Override
    public int getLives() {
        return this.model.getLives();
    }
}