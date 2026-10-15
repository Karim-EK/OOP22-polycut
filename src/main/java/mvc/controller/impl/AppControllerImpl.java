package mvc.controller.impl;

import java.util.Optional;

import mvc.controller.api.AppController;
import mvc.controller.api.AudioManager;
import mvc.controller.api.GameLoop;
import mvc.controller.api.GameWorldController;
import mvc.model.api.DifficultyEnum;
import mvc.model.api.GameWorldListener;
import mvc.model.api.PowerUpEnum;
import mvc.model.api.SliceableModel;
import mvc.view.api.GameFrame;

/**
 * Implementation of AppController. Every game has its own game loop thread:
 * it is created when the game starts and stopped when the player leaves it.
 * It observes the model only to know when the game is over.
 */
public class AppControllerImpl implements AppController, GameWorldListener {

    private static final long LOOP_JOIN_TIMEOUT_MS = 500;

    private final GameWorldController gameController;
    private final AudioManager audio;
    private Optional<GameFrame> view = Optional.empty();
    private Optional<GameLoop> loop = Optional.empty();
    private Optional<Thread> loopThread = Optional.empty();
    private DifficultyEnum difficulty = DifficultyEnum.NORMAL;

    /**
     * @param gameController the controller of the game world
     * @param audio the music and sound effects
     */
    public AppControllerImpl(final GameWorldController gameController, final AudioManager audio) {
        this.gameController = gameController;
        this.audio = audio;
    }

    /**
     * The view is set after the construction because it needs this controller for its buttons.
     * @param view the main window
     */
    public void setView(final GameFrame view) {
        this.view = Optional.of(view);
    }

    @Override
    public void startGame() {
        stopLoop();
        this.gameController.newGame(this.difficulty);
        this.audio.startMusic();
        this.view.ifPresent(v -> {
            v.showGame();
            final GameLoop newLoop = new GameLoopImpl(this.gameController, v.getGameArea());
            final Thread thread = new Thread(newLoop, "game-loop");
            thread.setDaemon(true);
            newLoop.start();
            thread.start();
            this.loop = Optional.of(newLoop);
            this.loopThread = Optional.of(thread);
        });
    }

    @Override
    public void backToMenu() {
        stopLoop();
        this.audio.startMusic();
        this.view.ifPresent(GameFrame::showMenu);
    }

    @Override
    public void quit() {
        stopLoop();
        this.audio.stopMusic();
        System.exit(0);
    }

    @Override
    public DifficultyEnum getDifficulty() {
        return this.difficulty;
    }

    @Override
    public void setDifficulty(final DifficultyEnum difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public void setMusicEnabled(final boolean enabled) {
        this.audio.setMusicEnabled(enabled);
    }

    @Override
    public boolean isMusicEnabled() {
        return this.audio.isMusicEnabled();
    }

    @Override
    public void setEffectsEnabled(final boolean enabled) {
        this.audio.setEffectsEnabled(enabled);
    }

    @Override
    public boolean isEffectsEnabled() {
        return this.audio.isEffectsEnabled();
    }

    @Override
    public void playClick() {
        this.audio.playClick();
    }

    // Chiamato dal game loop quando le vite finiscono: la view mostra la schermata finale
    @Override
    public void onGameOver(final int score) {
        this.view.ifPresent(v -> v.showGameOver(score));
    }

    @Override
    public void onSliceableCut(final SliceableModel sliceable) {
        // Non serve al flusso dell'applicazione
    }

    @Override
    public void onPowerUpActivate(final PowerUpEnum powerUp) {
        // Non serve al flusso dell'applicazione
    }

    @Override
    public void onPowerUpDeactivate(final PowerUpEnum powerUp) {
        // Non serve al flusso dell'applicazione
    }

    // Ferma il game loop della partita precedente e aspetta che il suo thread finisca,
    // così due loop non aggiornano mai il model insieme
    private void stopLoop() {
        this.loop.ifPresent(GameLoop::stop);
        this.loopThread.ifPresent(thread -> {
            try {
                thread.join(LOOP_JOIN_TIMEOUT_MS);
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        this.loop = Optional.empty();
        this.loopThread = Optional.empty();
    }
}
