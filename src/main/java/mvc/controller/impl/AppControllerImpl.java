package mvc.controller.impl;

import java.util.Optional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import mvc.controller.api.AppController;
import mvc.controller.api.AudioManager;
import mvc.controller.api.GameLoop;
import mvc.controller.api.GameWorldController;
import mvc.model.api.DifficultyEnum;
import mvc.model.api.GameWorldListener;
import mvc.model.api.PowerUpEnum;
import mvc.model.api.Records;
import mvc.model.api.SliceableModel;
import mvc.view.api.GameFrame;

/**
 * Implementation of AppController. Every game has its own game loop thread:
 * it is created when the game starts and stopped when the player leaves it.
 * It observes the model only to know when the game is over.
 */
@SuppressFBWarnings(
    value = "EI_EXPOSE_REP2",
    justification = "Controllers, audio and view are shared on purpose: App connects them once at startup"
)
public final class AppControllerImpl implements AppController, GameWorldListener {

    private static final long LOOP_JOIN_TIMEOUT_MS = 500;

    private final GameWorldController gameController;
    private final AudioManager audio;
    private final Records records;
    private Optional<GameFrame> view = Optional.empty();
    private Optional<GameLoop> loop = Optional.empty();
    private Optional<Thread> loopThread = Optional.empty();
    private DifficultyEnum difficulty = DifficultyEnum.NORMAL;

    /**
     * @param gameController the controller of the game world
     * @param audio the music and sound effects
     * @param records the best score of every difficulty level
     */
    public AppControllerImpl(final GameWorldController gameController, final AudioManager audio,
            final Records records) {
        this.gameController = gameController;
        this.audio = audio;
        this.records = records;
    }

    /**
     * The view is set after the construction because it needs this controller for its buttons.
     *
     * @param view the main window
     */
    public void setView(final GameFrame view) {
        this.view = Optional.of(view);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void backToMenu() {
        stopLoop();
        this.audio.startMusic();
        this.view.ifPresent(GameFrame::showMenu);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void quit() {
        stopLoop();
        this.audio.stopMusic();
        // La finestra riceve l'evento di chiusura: è Swing a terminare l'applicazione
        this.view.ifPresent(GameFrame::close);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DifficultyEnum getDifficulty() {
        return this.difficulty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDifficulty(final DifficultyEnum difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRecord(final DifficultyEnum level) {
        return this.records.getRecord(level);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMusicVolume(final int volume) {
        this.audio.setMusicVolume(volume);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMusicVolume() {
        return this.audio.getMusicVolume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setEffectsVolume(final int volume) {
        this.audio.setEffectsVolume(volume);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getEffectsVolume() {
        return this.audio.getEffectsVolume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playClick() {
        this.audio.playClick();
    }

    /**
     * Chiamato dal game loop quando le vite finiscono: si salva il record del livello giocato
     * e la view mostra la schermata finale.
     */
    @Override
    public void onGameOver(final int score) {
        final boolean newRecord = this.records.submit(this.difficulty, score);
        final int record = this.records.getRecord(this.difficulty);
        this.view.ifPresent(v -> v.showGameOver(score, record, newRecord));
    }

    /**
     * Non serve al flusso dell'applicazione.
     */
    @Override
    public void onSliceableCut(final SliceableModel sliceable) {
        // Nessuna azione
    }

    /**
     * Non serve al flusso dell'applicazione.
     */
    @Override
    public void onPowerUpActivate(final PowerUpEnum powerUp) {
        // Nessuna azione
    }

    /**
     * Non serve al flusso dell'applicazione.
     */
    @Override
    public void onPowerUpDeactivate(final PowerUpEnum powerUp) {
        // Nessuna azione
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
