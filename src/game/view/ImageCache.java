package game.view;

import javafx.scene.image.Image;
import java.util.*;

public final class ImageCache {
    private static final Map<String, Image> CACHE = new HashMap<>();

    public static Image get(String path) {
        return CACHE.computeIfAbsent(path, p -> new Image(ImageCache.class.getResourceAsStream(p)));
    }
}
