package game.audio;

import java.io.File;
import java.util.ArrayList;
import java.net.URL;
import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public final class SoundManager {

    private static final String SOUNDTRACK_PATH = "/game/assets/soundTrack/monsters_inc_theme.mp3";
    private static final String BUTTON_SOUND_PATH = "/game/assets/soundTrack/Button.mp3";
    private static final String DICE_SOUND_PATH = "/game/assets/soundTrack/diceRoll.mp3";
    private static final String POWER_UP_SOUND_PATH = "/game/assets/soundTrack/PowerUp.mp3";
    private static final String INVALID_SOUND_PATH = "/game/assets/soundTrack/PowerUpInvalid.mp3";
    private static final String BELT_SOUND_PATH = "/game/assets/soundTrack/Belt.mp3";
    private static final String SOCK_SOUND_PATH = "/game/assets/soundTrack/Sock.mp3";
    private static MediaPlayer soundtrackPlayer;
    private static final List<MediaPlayer> activeEffects = new ArrayList<>();
    private static boolean soundOn = true;
    private static double musicVolume = 0.35;
    private static double effectsVolume = 0.75;

    private SoundManager() {
    }

    public static void startSoundtrack() {
        if (soundtrackPlayer != null) {
            if (soundOn) {
                soundtrackPlayer.play();
            }
            return;
        }

        try {
            String soundtrackUrl = findSoundUrl(SOUNDTRACK_PATH);

            if (soundtrackUrl == null) {
                System.err.println("Soundtrack not found: " + SOUNDTRACK_PATH);
                return;
            }

            soundtrackPlayer = new MediaPlayer(new Media(soundtrackUrl));
            soundtrackPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            soundtrackPlayer.setVolume(musicVolume);

            if (soundOn) {
                soundtrackPlayer.play();
            }
        } catch (Throwable error) {
            System.err.println("Could not load soundtrack. Make sure javafx.media is in the run configuration.");
            System.err.println("Missing or failed audio support for: " + SOUNDTRACK_PATH);
        }
    }

    private static String findSoundtrackUrl() {
        return findSoundUrl(SOUNDTRACK_PATH);
    }

    private static String findSoundUrl(String resourcePath) {
        URL resource = SoundManager.class.getResource(resourcePath);

        if (resource != null) {
            return resource.toExternalForm();
        }

        File sourceAsset = new File("src" + resourcePath);

        if (sourceAsset.exists()) {
            return sourceAsset.toURI().toString();
        }

        return null;
    }

    public static void setSoundOn(boolean enabled) {
        soundOn = enabled;

        if (soundtrackPlayer == null) {
            return;
        }

        if (soundOn) {
            soundtrackPlayer.play();
        } else {
            soundtrackPlayer.pause();
        }
    }

    public static boolean isSoundOn() {
        return soundOn;
    }

    public static void setMusicVolume(double newVolume) {
        musicVolume = clampVolume(newVolume);

        if (soundtrackPlayer != null) {
            soundtrackPlayer.setVolume(musicVolume);
        }
    }

    public static double getMusicVolume() {
        return musicVolume;
    }

    public static void setEffectsVolume(double newVolume) {
        effectsVolume = clampVolume(newVolume);
    }

    public static double getEffectsVolume() {
        return effectsVolume;
    }

    public static void playButtonSound() {
        playEffect(BUTTON_SOUND_PATH);
    }

    public static void playDiceRollSound() {
        playEffect(DICE_SOUND_PATH);
    }

    public static void playPowerUpSound() {
        playEffect(POWER_UP_SOUND_PATH);
    }

    public static void playInvalidSound() {
        playEffect(INVALID_SOUND_PATH);
    }

    public static void playBeltSound() {
        playEffect(BELT_SOUND_PATH);
    }

    public static void playSockSound() {
        playEffect(SOCK_SOUND_PATH);
    }

    private static void playEffect(String path) {
        if (!soundOn) {
            return;
        }

        try {
            String soundUrl = findSoundUrl(path);

            if (soundUrl == null) {
                System.err.println("Sound effect not found: " + path);
                return;
            }

            MediaPlayer effectPlayer = new MediaPlayer(new Media(soundUrl));
            effectPlayer.setVolume(effectsVolume);
            activeEffects.add(effectPlayer);
            effectPlayer.setOnEndOfMedia(() -> {
                effectPlayer.dispose();
                activeEffects.remove(effectPlayer);
            });
            effectPlayer.play();
        } catch (Throwable error) {
            System.err.println("Could not play sound effect: " + path);
        }
    }

    private static double clampVolume(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }
}
