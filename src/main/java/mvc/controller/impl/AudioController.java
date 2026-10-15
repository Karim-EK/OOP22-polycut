package mvc.controller.impl;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import mvc.controller.api.AudioManager;
import mvc.model.api.PowerUpEnum;
import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableTypeEnum;

/**
 * Implementation of AudioManager with javax.sound Clips loaded from the resources.
 * A missing or broken file only disables its own sound: the game keeps working.
 */
public class AudioController implements AudioManager {
    private final Optional<Clip> bgMusic;
    private final Optional<Clip> slashSound;
    private final Optional<Clip> explosionSound;
    private final Optional<Clip> freezeSound;
    private final Optional<Clip> x2Sound;
    private final Optional<Clip> oneUpSound;
    private final Optional<Clip> safeZoneSound;
    private final Optional<Clip> deactivateSound;
    private final Optional<Clip> gameOverSound;
    private final Optional<Clip> clickSound;

    // Le opzioni si cambiano dal menu (thread di Swing) e si leggono dal game loop
    private volatile boolean musicEnabled = true;
    private volatile boolean effectsEnabled = true;

    /**
     * Loads every sound from /audio in the resources.
     */
    public AudioController() {
        this.bgMusic = loadClip("/audio/background.wav");
        this.slashSound = loadClip("/audio/slash.wav");
        this.explosionSound = loadClip("/audio/explosion.wav");
        this.freezeSound = loadClip("/audio/freeze.wav");
        this.x2Sound = loadClip("/audio/x2.wav");
        this.oneUpSound = loadClip("/audio/oneup.wav");
        this.safeZoneSound = loadClip("/audio/safezone.wav");
        this.deactivateSound = loadClip("/audio/deactivate.wav");
        this.gameOverSound = loadClip("/audio/gameover.wav");
        this.clickSound = loadClip("/audio/click.wav");
    }

    @Override
    public void onSliceableCut(final SliceableModel sliceable) {
        if (sliceable.getType() == SliceableTypeEnum.BOMB) {
            playSound(explosionSound);
        } else {
            playSound(slashSound);
        }
    }

    @Override
    public void onGameOver(final int score) {
        stopMusic();
        playSound(gameOverSound);
    }

    @Override
    public void onPowerUpDeactivate(final PowerUpEnum powerUp) {
        playSound(deactivateSound);
    }

    @Override
    public void onPowerUpActivate(final PowerUpEnum powerUp) {
        switch (powerUp) {
            case FREEZE -> playSound(freezeSound);
            case ONE_UP -> playSound(oneUpSound);
            case SAFE_ZONE -> playSound(safeZoneSound);
            case X2 -> playSound(x2Sound);
        }
    }

    @Override
    public void startMusic() {
        if (this.musicEnabled) {
            this.bgMusic.filter(clip -> !clip.isRunning()).ifPresent(clip -> clip.loop(Clip.LOOP_CONTINUOUSLY));
        }
    }

    @Override
    public void stopMusic() {
        this.bgMusic.ifPresent(Clip::stop);
    }

    @Override
    public void playClick() {
        playSound(clickSound);
    }

    @Override
    public void setMusicEnabled(final boolean enabled) {
        this.musicEnabled = enabled;
        if (enabled) {
            startMusic();
        } else {
            stopMusic();
        }
    }

    @Override
    public boolean isMusicEnabled() {
        return this.musicEnabled;
    }

    @Override
    public void setEffectsEnabled(final boolean enabled) {
        this.effectsEnabled = enabled;
    }

    @Override
    public boolean isEffectsEnabled() {
        return this.effectsEnabled;
    }

    private Optional<Clip> loadClip(final String path) {
        final URL url = AudioController.class.getResource(path);
        if (url == null) {
            System.err.println("Audio non trovato: " + path);
            return Optional.empty();
        }
        try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(url)) {
            final Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            return Optional.of(clip);
        } catch (final UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Errore caricamento audio: " + path);
            return Optional.empty();
        }
    }

    // Riparte dall'inizio anche se il suono stava già suonando (tagli ravvicinati)
    private void playSound(final Optional<Clip> clip) {
        if (this.effectsEnabled) {
            clip.ifPresent(c -> {
                c.stop();
                c.setFramePosition(0);
                c.start();
            });
        }
    }
}
