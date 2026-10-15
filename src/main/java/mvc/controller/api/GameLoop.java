package mvc.controller.api;

/**
 * Gestisce il ciclo di vita del motore di gioco.
 * Estende Runnable per poter essere eseguito su un Thread separato.
 */
public interface GameLoop extends Runnable {

    /**
     * Prepares the loop to run: call it before starting the thread.
     */
    void start();

    /**
     * Asks the loop to end at the end of the current frame.
     */
    void stop();

    /**
     * @return true while the loop is running
     */
    boolean isRunning();
}
