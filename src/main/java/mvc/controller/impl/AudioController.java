package mvc.controller.impl;

import mvc.model.api.GameWorldListener;

import javax.sound.sampled.*;

import mvc.model.api.PowerUpEnum;
import mvc.model.api.SliceableModel;
import mvc.model.api.SliceableTypeEnum;

import java.net.URL;

public class AudioController implements GameWorldListener {
    private Clip bgMusic;
    private Clip slashSound;
    private Clip explosionSound;
    private Clip freezeSound;
    private Clip x2Sound;
    private Clip oneUpSound;
    private Clip safeZoneSound;
    private Clip deactivateSound;
    private Clip gameOverSound;

    public AudioController() {
        this.bgMusic = loadClip("/audio/background.wav");
        this.slashSound = loadClip("/audio/slash.wav");
        this.explosionSound = loadClip("/audio/explosion.wav");

        this.freezeSound = loadClip("/audio/explosion.wav");
        this.x2Sound = loadClip("/audio/explosion.wav");
        this.oneUpSound = loadClip("/audio/explosion.wav");
        this.safeZoneSound = loadClip("/audio/explosion.wav");

        this.deactivateSound = loadClip("/audio/explosion.wav");
        
        if (this.bgMusic != null) {
            this.bgMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    @Override
    public void onSliceableCut(SliceableModel sliceable) {
        if (sliceable.getType() == SliceableTypeEnum.BOMB) {
            playSound(explosionSound);
        } else {
            playSound(slashSound);
        }
    }

    @Override
    public void onGameOver(int score) {
        if (bgMusic != null) bgMusic.stop();
        playSound(gameOverSound);
    }

    @Override
    public void onPowerUpDeactivate(PowerUpEnum powerUp) {
        playSound(deactivateSound);
    }

    @Override
    public void onPowerUpActivate(PowerUpEnum powerUp) {
        if (powerUp.equals(PowerUpEnum.FREEZE)) {
            playSound(freezeSound);
        } else if (powerUp.equals(PowerUpEnum.ONE_UP)) {
            playSound(oneUpSound);
        } else if (powerUp.equals(PowerUpEnum.SAFE_ZONE)) {
            playSound(safeZoneSound);
        } else if (powerUp.equals(PowerUpEnum.X2)) {
            playSound(x2Sound);
        }
    }

    private Clip loadClip(String path) {
        try {
            URL url = getClass().getResource(path);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            return clip;
        } catch (Exception e) {
            System.err.println("Errore caricamento audio: " + path);
            return null;
        }
    }

    private void playSound(Clip clip) {
        if (clip != null) {
            clip.setFramePosition(0); 
            clip.start();
        }
    }
    
}