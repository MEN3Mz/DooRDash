package game.view;

import javafx.scene.image.Image;
import java.util.*;

public final class ImageCache {
    private static final Map<String, Image> CACHE = new HashMap<>();

    public static Image get(String path) {
        String cacheKey = ThemeManager.getCurrentTheme().name() + ":" + path;
        return CACHE.computeIfAbsent(cacheKey, key -> ThemeManager.loadImage(path));
    }

    public static void clear() {
        CACHE.clear();
    }
}
