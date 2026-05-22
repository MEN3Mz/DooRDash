package game.audio;

import java.io.File;
import java.util.ArrayList;
import java.net.URL;
import java.util.List;

import game.view.ThemeManager;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public final class SoundManager {

    private static final String MAIN_MENU_MUSIC_PATH = "/game/assets/soundTrack/MainMenu.mp3";
    private static final String GAME_MUSIC_PATH = "/game/assets/soundTrack/Game.mp3";
    private static final String BUTTON_SOUND_PATH = "/game/assets/soundTrack/Button.mp3";
    private static final String HOVER_SOUND_PATH = "/game/assets/soundTrack/hover.mp3";
    private static final String DICE_SOUND_PATH = "/game/assets/soundTrack/diceRoll.mp3";
    private static final String POWER_UP_SOUND_PATH = "/game/assets/soundTrack/PowerUp.mp3";
    private static final String INVALID_SOUND_PATH = "/game/assets/soundTrack/PowerUpInvalid.mp3";
    private static final String BELT_SOUND_PATH = "/game/assets/soundTrack/Belt.mp3";
    private static final String SOCK_SOUND_PATH = "/game/assets/soundTrack/Sock.mp3";
    private static final String DOOR_SOUND_PATH = "/game/assets/soundTrack/door";
    private static final String DAMAGE_SOUND_PATH = "/game/assets/soundTrack/damage.mp3";
    private static final String ENERGY_INCREASE_SOUND_PATH = "/game/assets/soundTrack/energyIncrease.mp3";
    private static final String FALLING_SOUND_PATH = "/game/assets/soundTrack/falling.mp3";
    private static final String LAUGH_WIN_SOUND_PATH = "/game/assets/soundTrack/LaughWin.mp3";
    private static final String SCARE_WIN_SOUND_PATH = "/game/assets/soundTrack/ScareWin.mp3";
    private static final String SHIELD_ADD_SOUND_PATH = "/game/assets/soundTrack/ShieldAdd.mp3";
    private static final String SHIELD_REMOVE_SOUND_PATH = "/game/assets/soundTrack/ShieldRemove.mp3";
    private static final String FREEZE_SOUND_PATH = "/game/assets/soundTrack/freeze.mp3";
    private static final String UNFREEZE_SOUND_PATH = "/game/assets/soundTrack/unfreeze.mp3";
    private static final String CONFUSION_SOUND_PATH = "/game/assets/soundTrack/Confusion.mp3";
    private static MediaPlayer soundtrackPlayer;
    private static String currentMusicPath;
    private static String currentMusicUrl;
    private static final List<MediaPlayer> activeEffects = new ArrayList<>();
    private static boolean soundOn = true;
    private static double musicVolume = 0.35;
    private static double effectsVolume = 0.75;
    private static double previousMusicVolume = musicVolume;
    private static double previousEffectsVolume = effectsVolume;

    private SoundManager() {
    }

    public static void startSoundtrack() {
        playMainMenuMusic();
    }

    public static void playMainMenuMusic() {
        playMusic(MAIN_MENU_MUSIC_PATH);
    }

    public static void playGameMusic() {
        playMusic(GAME_MUSIC_PATH);
    }

    public static void stopMusic() {
        stopCurrentMusic();
    }

    private static void playMusic(String musicPath) {
        String musicUrl = findSoundUrl(musicPath);

        if (musicPath.equals(currentMusicPath)
                && musicUrl != null
                && musicUrl.equals(currentMusicUrl)
                && soundtrackPlayer != null) {
            if (soundOn) {
                soundtrackPlayer.play();
            }
            return;
        }

        stopCurrentMusic();

        try {
            if (musicUrl == null) {
                System.err.println("Music not found: " + musicPath);
                return;
            }

            soundtrackPlayer = new MediaPlayer(new Media(musicUrl));
            soundtrackPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            soundtrackPlayer.setVolume(musicVolume);
            currentMusicPath = musicPath;
            currentMusicUrl = musicUrl;

            if (soundOn) {
                soundtrackPlayer.play();
            }
        } catch (Throwable error) {
            System.err.println("Could not load soundtrack. Make sure javafx.media is in the run configuration.");
            System.err.println("Missing or failed audio support for: " + musicPath);
        }
    }

    private static void stopCurrentMusic() {
        if (soundtrackPlayer == null) {
            return;
        }

        soundtrackPlayer.stop();
        soundtrackPlayer.dispose();
        soundtrackPlayer = null;
        currentMusicPath = null;
        currentMusicUrl = null;
    }

    private static String findSoundUrl(String resourcePath) {
        URL resource = ThemeManager.resolveThemeAwareUrl(resourcePath);

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
        if (soundOn == enabled) {
            return;
        }

        soundOn = enabled;

        if (soundOn) {
            musicVolume = previousMusicVolume > 0 ? previousMusicVolume : 0.35;
            effectsVolume = previousEffectsVolume > 0 ? previousEffectsVolume : 0.75;
        } else {
            if (musicVolume > 0) {
                previousMusicVolume = musicVolume;
            }
            if (effectsVolume > 0) {
                previousEffectsVolume = effectsVolume;
            }
            musicVolume = 0.0;
            effectsVolume = 0.0;
        }

        applyCurrentMusicState();
        updateActiveEffectsVolume();
    }

    private static void applyCurrentMusicState() {
        if (soundtrackPlayer == null) {
            return;
        }

        soundtrackPlayer.setVolume(musicVolume);

        if (soundOn && musicVolume > 0) {
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

        if (musicVolume > 0) {
            previousMusicVolume = musicVolume;
            soundOn = true;
        }

        updateSoundOnFromVolumes();
        applyCurrentMusicState();
    }

    public static double getMusicVolume() {
        return musicVolume;
    }

    public static void setEffectsVolume(double newVolume) {
        effectsVolume = clampVolume(newVolume);

        if (effectsVolume > 0) {
            previousEffectsVolume = effectsVolume;
            soundOn = true;
        }

        updateSoundOnFromVolumes();
        updateActiveEffectsVolume();
    }

    public static double getEffectsVolume() {
        return effectsVolume;
    }

    public static void playButtonSound() {
        playEffect(BUTTON_SOUND_PATH);
    }

    public static void playHoverSound() {
        playEffect(HOVER_SOUND_PATH);
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

    public static void playDoorSound() {
        int RandomDoorSoundNumber = (int) (Math.random() * 3) + 1;
        String doorSoundPath = DOOR_SOUND_PATH + RandomDoorSoundNumber + ".mp3";
        playEffect(doorSoundPath);
    }

    public static void playDamageSound() {
        playEffect(DAMAGE_SOUND_PATH);
    }

    public static void playEnergyIncreaseSound() {
        playEffect(ENERGY_INCREASE_SOUND_PATH);
    }

    public static void playFallingSound() {
        playEffect(FALLING_SOUND_PATH);
    }

    public static void playLaughWinSound() {
        playEffect(LAUGH_WIN_SOUND_PATH);
    }

    public static void playScareWinSound() {
        playEffect(SCARE_WIN_SOUND_PATH);
    }

    public static void playShieldAddSound() {
        playEffect(SHIELD_ADD_SOUND_PATH);
    }

    public static void playShieldRemoveSound() {
        playEffect(SHIELD_REMOVE_SOUND_PATH);
    }

    public static void playFreezeSound() {
        playEffect(FREEZE_SOUND_PATH);
    }

    public static void playUnfreezeSound() {
        playEffect(UNFREEZE_SOUND_PATH);
    }

    public static void playConfusionSound() {
        playEffect(CONFUSION_SOUND_PATH);
    }

    public static void stopAllEffects() {
        for (MediaPlayer effectPlayer : new ArrayList<>(activeEffects)) {
            effectPlayer.stop();
            effectPlayer.dispose();
        }

        activeEffects.clear();
    }

    private static void playEffect(String path) {
        if (!soundOn || effectsVolume <= 0) {
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

    private static void updateSoundOnFromVolumes() {
        soundOn = musicVolume > 0 || effectsVolume > 0;
    }

    private static void updateActiveEffectsVolume() {
        for (MediaPlayer effectPlayer : new ArrayList<>(activeEffects)) {
            effectPlayer.setVolume(effectsVolume);
        }
    }
}
