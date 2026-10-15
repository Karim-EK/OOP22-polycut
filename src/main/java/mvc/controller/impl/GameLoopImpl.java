package mvc.controller.impl;

import mvc.controller.api.GameLoop;
import mvc.controller.api.GameWorldController;
import mvc.view.api.GameAreaPanel;

/**
 * Implementation of GameLoop: updates the model and redraws the view about 60 times per second.
 */
public final class GameLoopImpl implements GameLoop {

    private static final int TARGET_FPS = 60;
    private static final long NANOS_PER_SECOND = 1_000_000_000;
    private static final long NANOS_PER_MILLI = 1_000_000;
    private static final long OPTIMAL_TIME_NS = NANOS_PER_SECOND / TARGET_FPS; // ~16.66 ms in nanosecondi

    private volatile boolean running;
    private final GameWorldController controller;
    private final GameAreaPanel viewPanel;

    /**
     * @param controller the controller of the game world, updated at every frame
     * @param viewPanel the game area, redrawn at every frame
     */
    public GameLoopImpl(final GameWorldController controller, final GameAreaPanel viewPanel) {
        this.controller = controller;
        this.viewPanel = viewPanel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        long lastLoopTime = System.nanoTime();
        while (running) {
            if (controller.isGameOver()) {
                this.running = false;
            }
            final long now = System.nanoTime();
            final long updateLength = now - lastLoopTime;
            lastLoopTime = now;

            // 1. Calcolo del deltaTime in secondi (fondamentale per la fisica indipendente dal framerate)
            final double delta = (double) updateLength / NANOS_PER_SECOND;
            this.controller.update(delta);
            this.viewPanel.render();

            // 4. Thread Sleep (Risparmio CPU e limitazione FPS)
            // Calcoliamo quanto tempo ci è rimasto prima di dover generare il prossimo frame
            final long timeTaken = System.nanoTime() - lastLoopTime;
            final long timeLeftNs = OPTIMAL_TIME_NS - timeTaken;

            if (timeLeftNs > 0) {
                try {
                    // Convertiamo i nanosecondi rimanenti in millisecondi per il Thread.sleep
                    Thread.sleep(timeLeftNs / NANOS_PER_MILLI);
                } catch (final InterruptedException e) {
                    Thread.currentThread().interrupt();
                    this.running = false;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        this.running = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        this.running = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRunning() {
        return this.running;
    }

}
