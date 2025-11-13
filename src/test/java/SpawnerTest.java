package mvc.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableModelFactory;
import mvc.model.api.XY;
import mvc.model.impl.SliceableModelImpl;
import mvc.model.impl.SpawnerImpl;

public class SpawnerTest {
    private final SpawnerImpl spawner = new SpawnerImpl();
    private final SliceableModelFactory factory = SliceableModelImpl::new;
    Optional<SliceableModel> sliceable;

    private record VelocityTestCase(double width, double height, double startingPoint, double angle, double minVel, double maxVel) {}

    static List<XY<Double>> coordinatesProvider() {
        return List.of(
            new XY<>(600.0, 600.0),
            new XY<>(301.0, 466.0),
            new XY<>(500.0, 300.0)
            );
    }

    static List<VelocityTestCase> velocityProvider() {
        return List.of(
            new VelocityTestCase(600.0, 600.0, 50.0, Math.toRadians(15), 209.60, 419.21),
            new VelocityTestCase(301.0, 466.0, 170.0, Math.toRadians(15), 49.50, 506.15),
            new VelocityTestCase(500.0, 300.0, 251.0, Math.toRadians(75), 308.53, 257.78)
            );
    }

    @ParameterizedTest
    @MethodSource("coordinatesProvider")
    void positionTest(final XY<Double> testCase){
        sliceable = spawner.spawn(0, testCase.x(), testCase.y(), factory);
        assertTrue(sliceable.isPresent());
        assertTrue(sliceable.get().getPosition().x() <= testCase.x());
        assertTrue(sliceable.get().getPosition().x() >= 0);
        assertEquals(0, testCase.y());
    }

    @ParameterizedTest
    @MethodSource("velocityProvider")
    void positionTest(final VelocityTestCase testCase){
        sliceable = spawner.spawn(0, testCase.width, testCase.height,  factory);
        assertTrue(sliceable.isPresent());
        assertTrue(sliceable.get().getPosition().y() == 0);
        sliceable.get().setPosition(new XY<>(testCase.startingPoint, 0.0));
        assertTrue(Math.sqrt(
            Math.pow(sliceable.get().getVelocity().x(), 2)
            + Math.pow(sliceable.get().getVelocity().y(), 2)) >= testCase.minVel);
        assertTrue(Math.sqrt(
            Math.pow(sliceable.get().getVelocity().x(), 2)
            + Math.pow(sliceable.get().getVelocity().y(), 2)) <= testCase.maxVel());
    }

}
