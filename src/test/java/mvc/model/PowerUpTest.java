package mvc.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.geom.Point2D;

import org.junit.jupiter.api.Test;

import mvc.model.api.PowerUpEnum;
import mvc.model.api.PowerUpModel;
import mvc.model.impl.SliceableModelFactoryImpl;

public class PowerUpTest {

    PowerUpModel powerUp = SliceableModelFactoryImpl.createPowerUp();

    @Test
    void powerUpCreation() {
        assertEquals(PowerUpEnum.class, powerUp.getClass());
        assertEquals(new Point2D.Double(0, 0), powerUp.getPosition());
        assertEquals(powerUp.getPowerUp().getDuration(), powerUp.getTimeReameaning());
    }

    @Test
    void creationProbability() {
        var countX2 = 0;
        var countFreeze = 0;
        var countSafe = 0;
        var count1UP = 0;

        for (int i = 0; i < 10_000; i++) {
            var newPowerUp = SliceableModelFactoryImpl.createPowerUp().getPowerUp();
            
            switch (newPowerUp) {
                case FREEZE :
                    countFreeze ++;
                    break;
                case X2 :
                    countX2 ++;
                    break;
                case SAFE_ZONE :
                    countSafe ++;
                    break;
                case ONE_UP :
                    count1UP ++;
                    break;
                default:
                    break;
            }
        }
        var total = count1UP + countFreeze + countSafe + countX2;
        System.out.println(count1UP / total);
        System.out.println(countFreeze / total);
        System.out.println(countX2 / total);
        System.out.println(countSafe / total);
    }
}
