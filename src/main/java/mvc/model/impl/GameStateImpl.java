package mvc.model.impl;

import mvc.model.api.GameState;
import mvc.model.api.GameStateEnum;

/**
 * Implementation of GameState.
 */
public class GameStateImpl implements GameState {

    private int lives;
    private int score;
    private GameStateEnum gamestate;
    private boolean invulnerability;
    private int multiplier;

    /**
     * sets up all the fields.
     */
    public GameStateImpl() {
        init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        init();
    }

    // Metodo privato: il costruttore non deve chiamare metodi sovrascrivibili
    private void init() {
        this.lives = MAX_LIVES;
        this.score = 0;
        this.gamestate = GameStateEnum.RUNNING;
        this.invulnerability = false;
        this.multiplier = 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getScore() {
        return this.score;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void gainScore(final int score) {
        this.score = this.score + score * multiplier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLives() {
        return this.lives;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addLife() {
        if (lives < MAX_LIVES) {
            lives++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decreseLife() {
        if (this.lives > 0) {
            this.lives --;
            if (lives == 0) {
                this.gamestate = GameStateEnum.GAMEOVER;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasPlayerLost() {
        return gamestate == GameStateEnum.GAMEOVER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GameStateEnum getGameState() {
        return gamestate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setGameState(final GameStateEnum gameState) {
        this.gamestate = gameState;
    }

    /**
     * TODO.
     * @param activate
     */
    public void setInvulnerability(final boolean activate) {
        this.invulnerability = activate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMultiplier(final int multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public boolean isInvulnerable() {
        return this.invulnerability;
    }
}
