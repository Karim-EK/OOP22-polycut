package mvc.controller.impl;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mvc.controller.api.AudioManager;
import mvc.model.api.PowerUpEnum;
import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableTypeEnum;

/**
 * Implementation of AudioManager with javax.sound Clips loaded from the resources.
 * A missing or broken file only disables its own sound: the game keeps working.
 */
public final class AudioController implements AudioManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AudioController.class);
    // Volume iniziale: musica un po' sotto gli effetti, per sentire bene i tagli
    private static final int DEFAULT_MUSIC_VOLUME = 70;
    private static final int DEFAULT_EFFECTS_VOLUME = 90;
    // Curva del volume: 40 * log10 rende il cursore più naturale all'orecchio di 20 * log10
    private static final double DECIBEL_CURVE = 40.0;

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
    private final List<Optional<Clip>> effects;

    // Le opzioni si cambiano dal menu (thread di Swing) e si leggono dal game loop
    private volatile int musicVolume = DEFAULT_MUSIC_VOLUME;
    private volatile int effectsVolume = DEFAULT_EFFECTS_VOLUME;

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
        this.effects = List.of(slashSound, explosionSound, freezeSound, x2Sound, oneUpSound,
            safeZoneSound, deactivateSound, gameOverSound, clickSound);
        this.bgMusic.ifPresent(clip -> applyVolume(clip, this.musicVolume));
        this.effects.forEach(clip -> clip.ifPresent(c -> applyVolume(c, this.effectsVolume)));
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
        if (this.musicVolume > 0) {
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

    // Con volume 0 la musica si ferma; appena il volume torna sopra 0 riparte
    @Override
    public void setMusicVolume(final int volume) {
        this.musicVolume = clamp(volume);
        this.bgMusic.ifPresent(clip -> applyVolume(clip, this.musicVolume));
        if (this.musicVolume == 0) {
            stopMusic();
        } else {
            startMusic();
        }
    }

    @Override
    public int getMusicVolume() {
        return this.musicVolume;
    }

    @Override
    public void setEffectsVolume(final int volume) {
        this.effectsVolume = clamp(volume);
        this.effects.forEach(clip -> clip.ifPresent(c -> applyVolume(c, this.effectsVolume)));
    }

    @Override
    public int getEffectsVolume() {
        return this.effectsVolume;
    }

    private static int clamp(final int volume) {
        return Math.max(0, Math.min(MAX_VOLUME, volume));
    }

    // Il volume si applica come guadagno in decibel: 0 dB al massimo, sempre più negativo verso 0
    private static void applyVolume(final Clip clip, final int volume) {
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            return;
        }
        final FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        final double decibel = volume == 0
            ? gain.getMinimum()
            : DECIBEL_CURVE * Math.log10((double) volume / MAX_VOLUME);
        gain.setValue((float) Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), decibel)));
    }

    private Optional<Clip> loadClip(final String path) {
        final URL url = AudioController.class.getResource(path);
        if (url == null) {
            LOGGER.warn("Audio non trovato: {}", path);
            return Optional.empty();
        }
        try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(url)) {
            final Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            return Optional.of(clip);
        } catch (final UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            LOGGER.warn("Errore caricamento audio: {}", path, e);
            return Optional.empty();
        }
    }

    // Riparte dall'inizio anche se il suono stava già suonando (tagli ravvicinati)
    private void playSound(final Optional<Clip> clip) {
        if (this.effectsVolume > 0) {
            clip.ifPresent(c -> {
                c.stop();
                c.setFramePosition(0);
                c.start();
            });
        }
    }
}
