package mvc;

import mvc.controller.api.AudioManager;
import mvc.controller.api.GameWorldController;
import mvc.controller.impl.AudioController;
import mvc.controller.api.GameLoop;
import mvc.controller.impl.GameLoopImpl;
import mvc.controller.impl.GameWorldControllerImpl;
import mvc.model.impl.GameStateImpl;
import mvc.model.impl.GameWorldImpl;
import mvc.model.impl.PhysicsImpl;
import mvc.model.impl.SliceHandlerImpl;
import mvc.model.impl.SliceableModelFactoryImpl;
import mvc.model.impl.SpawnerImpl;
import mvc.model.api.GameWorld;
import mvc.view.impl.GameFrameImpl;

public class App {
    public static void main(String[] args) {
        GameWorld model = new GameWorldImpl(
            new PhysicsImpl(), new SpawnerImpl(), new SliceHandlerImpl(), 
            new SliceableModelFactoryImpl(), new GameStateImpl()
        );
        GameWorldController controller = new GameWorldControllerImpl(model);
        GameFrameImpl gameframe = new GameFrameImpl(controller);
        model.addListener(gameframe.getGameArea());
        final AudioManager audio = new AudioController();
        model.addListener(audio);
        audio.startMusic();
        gameframe.setVisible(true);
        GameLoop gameLoop = new GameLoopImpl(controller, gameframe.getGameArea());
        Thread loopThread = new Thread(gameLoop);
        gameLoop.start();
        loopThread.start();
    }
}
  