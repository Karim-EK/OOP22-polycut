package mvc;

import java.nio.file.Path;

import javax.swing.SwingUtilities;

import mvc.controller.api.AudioManager;
import mvc.controller.api.GameWorldController;
import mvc.controller.impl.AppControllerImpl;
import mvc.controller.impl.AudioController;
import mvc.controller.impl.GameWorldControllerImpl;
import mvc.model.api.GameWorld;
import mvc.model.impl.GameStateImpl;
import mvc.model.impl.GameWorldImpl;
import mvc.model.impl.PhysicsImpl;
import mvc.model.impl.RecordsImpl;
import mvc.model.impl.SliceHandlerImpl;
import mvc.model.impl.SliceableModelFactoryImpl;
import mvc.model.impl.SpawnerImpl;
import mvc.view.impl.GameFrameImpl;

/**
 * Entry point: creates model, controllers and view and connects them.
 * It is the only class that knows the concrete implementations.
 */
public final class App {

    // I record si salvano nella cartella dell'utente, così restano tra una partita e l'altra
    private static final Path RECORDS_FILE =
        Path.of(System.getProperty("user.home"), ".polycut", "records.properties");

    private App() {
    }

    /**
     * @param args unused
     */
    public static void main(final String[] args) {
        final GameWorld model = new GameWorldImpl(
            new PhysicsImpl(), new SpawnerImpl(), new SliceHandlerImpl(),
            new SliceableModelFactoryImpl(), new GameStateImpl()
        );
        final GameWorldController controller = new GameWorldControllerImpl(model);
        final AudioManager audio = new AudioController();
        final AppControllerImpl app = new AppControllerImpl(controller, audio, new RecordsImpl(RECORDS_FILE));

        // La view si crea sul thread di Swing, come richiede la libreria
        SwingUtilities.invokeLater(() -> {
            final GameFrameImpl frame = new GameFrameImpl(controller, app);
            app.setView(frame);
            model.addListener(frame.getGameArea());
            model.addListener(audio);
            model.addListener(app);
            frame.setVisible(true);
            app.backToMenu();
        });
    }
}
