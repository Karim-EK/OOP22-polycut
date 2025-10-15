package mvc.view.api;

import mvc.model.api.PowerUpEnum;

public interface PowerUpView extends SliceableView {
    
    /** TODO: metodo che colora lo schermo in base al powerUp.
     * @return the type of powerUp.
     */
    PowerUpEnum getPowerUpType();
}
