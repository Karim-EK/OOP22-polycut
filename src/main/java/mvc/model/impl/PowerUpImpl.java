package mvc.model.impl;

import mvc.model.api.PowerUpEnum;
import mvc.model.api.PowerUpModel;
import mvc.model.api.SliceableTypeEnum;
/**
 * Instanciable class for PowerUps.
 */
public class PowerUpImpl extends SliceableModelImpl implements PowerUpModel {

    private final PowerUpEnum type;
    private double timeRemeaning;
    /**
     * {@inheritDoc}.
     * @param type
     * @param position
     * @param velocity
     * @param sliceableId
     */
    // TODO: type è un'informazione inutile sicuramente posso ometterlo
    public PowerUpImpl(
    final SliceableTypeEnum type, final PositionXY position,
    final PositionXY velocity, final PowerUpEnum powerUp) {
        super(type, position, velocity);
        this.type = powerUp;
        this.timeRemeaning = this.type.getDuration();
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public double getTimeReameaning() {
        return this.timeRemeaning;
    }
    /**
     * {@inheritDoc}.
     */
    @Override
    public PowerUpEnum getPowerUp() {
        return this.type;
    }
    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean isEnded() {
        return timeRemeaning <= 0;
    }
}
