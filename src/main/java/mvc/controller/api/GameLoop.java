package mvc.controller.api;

/**
 * Gestisce il ciclo di vita del motore di gioco.
 * Estende Runnable per poter essere eseguito su un Thread separato.
 */
public interface GameLoop extends Runnable {
    void start();
    void stop();
    boolean isRunning();
}