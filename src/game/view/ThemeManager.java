package game.view;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public final class ThemeManager {
    public enum Theme {
        DEFAULT("", "Theme: Default"),
        RETRO("/retro", "Theme: Back to the 80's"),
        ANCIENT_EGYPT("/ancientEgypt", "Theme: Ancient Egyptian");

        private final String assetPrefix;
        private final String label;

        Theme(String assetPrefix, String label) {
            this.assetPrefix = assetPrefix;
            this.label = label;
        }
    }

    private static final String ASSETS_ROOT = "/game/assets";
    private static Theme currentTheme = Theme.DEFAULT;

    private ThemeManager() {
    }

    public static Theme getCurrentTheme() {
        return currentTheme;
    }

    public static String getCurrentThemeLabel() {
        return currentTheme.label;
    }

    public static boolean isAncientEgyptian() {
        return currentTheme == Theme.ANCIENT_EGYPT;
    }

    public static boolean isRetro() {
        return currentTheme == Theme.RETRO;
    }

    public static void setTheme(Theme theme) {
        if (theme == null || currentTheme == theme) {
            return;
        }

        currentTheme = theme;
        ImageCache.clear();
    }

    public static Color getLaugherAccentColor() {
        if (isRetro()) {
            return Color.web("#00e5ff");
        }

        return isAncientEgyptian() ? Color.web("#f6c74a") : Color.web("#2F80ED");
    }

    public static Color getTransportForwardColor() {
        if (isRetro()) {
            return Color.web("#ff4fd8");
        }

        return isAncientEgyptian() ? Color.web("#6FCF97") : Color.web("#6FCF97");
    }

    public static String getLaugherTextColor() {
        return "#2F80ED";
    }

    public static String getLaugherLabelStyle() {
        return "-fx-background-color: #2F80ED; -fx-text-fill: white; -fx-font-size: 9px; -fx-font-weight: bold;"
                + " -fx-background-radius: 10; -fx-padding: 2 5 2 4;";
    }

    public static String getDefaultCellStyle() {
        if (isRetro()) {
            return "-fx-border-color: #ff4fd8; -fx-border-width: 2;"
                    + " -fx-background-color: linear-gradient(to bottom, #18224d, #11162f);";
        }

        if (isAncientEgyptian()) {
            return "-fx-border-color: #7b5617; -fx-border-width: 2;"
                    + " -fx-background-color: linear-gradient(to bottom, #fff2c8, #c9aa62);";
        }

        return "-fx-border-color: #3C4148; -fx-border-width: 2;"
                + " -fx-background-color: linear-gradient(to bottom, #F4F7FA, #AEB7C1);";
    }

    public static String getDoorCellStyle() {
        if (isRetro()) {
            return "-fx-border-color: #00e5ff; -fx-border-width: 2;"
                    + " -fx-background-color: linear-gradient(to bottom, #2b9cff, #7c2cff);";
        }

        if (isAncientEgyptian()) {
            return "-fx-border-color: #8a5a12; -fx-border-width: 2;"
                    + " -fx-background-color: linear-gradient(to bottom, #f7d77a, #9b6a18);";
        }

        return "-fx-border-color: #1E4F73; -fx-border-width: 2;"
                + " -fx-background-color: linear-gradient(to bottom, #8FD3FF, #3D79A6);";
    }

    public static String getBottomPanelStyle() {
        if (isRetro()) {
            return """
                    -fx-background-color:
                        linear-gradient(to bottom, rgba(35, 17, 78, 0.94), rgba(8, 13, 38, 0.94));

                    -fx-background-radius: 14;

                    -fx-border-color: #00e5ff;
                    -fx-border-width: 2;
                    -fx-border-radius: 14;
                    -fx-effect: dropshadow(three-pass-box, rgba(255, 79, 216, 0.55), 14, 0, 0, 0);
                    """;
        }

        if (isAncientEgyptian()) {
            return """
                    -fx-background-color:
                        linear-gradient(to bottom, #6b4712, #3d2608);

                    -fx-background-radius: 14;

                    -fx-border-color: #f7d77a;
                    -fx-border-width: 2;
                    -fx-border-radius: 14;
                    """;
        }

        return """
                -fx-background-color:
                    linear-gradient(to bottom, #1f2933, #3e4c59);

                -fx-background-radius: 14;

                -fx-border-color: #bcccdc;
                -fx-border-width: 2;
                -fx-border-radius: 14;
                """;
    }

    public static void cycleTheme() {
        Theme[] themes = Theme.values();
        currentTheme = themes[(currentTheme.ordinal() + 1) % themes.length];
        ImageCache.clear();
    }

    public static Image loadImage(String defaultPath) {
        String resolvedPath = resolveImagePath(defaultPath);
        InputStream imageStream = ThemeManager.class.getResourceAsStream(resolvedPath);

        if (imageStream != null) {
            return new Image(imageStream);
        }

        File sourceAsset = new File("src" + resolvedPath);
        if (sourceAsset.exists()) {
            return new Image(sourceAsset.toURI().toString());
        }

        return new Image(ThemeManager.class.getResourceAsStream(defaultPath));
    }

    public static URL resolveImageUrl(String defaultPath) {
        String resolvedPath = resolveImagePath(defaultPath);
        URL themedResource = ThemeManager.class.getResource(resolvedPath);

        if (themedResource != null) {
            return themedResource;
        }

        return ThemeManager.class.getResource(defaultPath);
    }

    public static String loadStylesheet(String defaultPath) {
        URL stylesheet = resolveAssetUrl(defaultPath);

        if (stylesheet == null) {
            return "";
        }

        return stylesheet.toExternalForm();
    }

    public static String resolveImagePath(String defaultPath) {
        if (currentTheme == Theme.DEFAULT || !isThemeableImagePath(defaultPath)) {
            return defaultPath;
        }

        String themedPath = ASSETS_ROOT + currentTheme.assetPrefix + defaultPath.substring(ASSETS_ROOT.length());

        if (resourceExists(themedPath)) {
            return themedPath;
        }

        return defaultPath;
    }

    private static URL resolveAssetUrl(String defaultPath) {
        String resolvedPath = resolveAssetPath(defaultPath);
        URL themedResource = ThemeManager.class.getResource(resolvedPath);

        if (themedResource != null) {
            return themedResource;
        }

        File sourceAsset = new File("src" + resolvedPath);
        if (sourceAsset.exists()) {
            try {
                return sourceAsset.toURI().toURL();
            } catch (Exception e) {
                return ThemeManager.class.getResource(defaultPath);
            }
        }

        return ThemeManager.class.getResource(defaultPath);
    }

    private static String resolveAssetPath(String defaultPath) {
        if (currentTheme == Theme.DEFAULT || !isThemeableAssetPath(defaultPath)) {
            return defaultPath;
        }

        String themedPath = ASSETS_ROOT + currentTheme.assetPrefix + defaultPath.substring(ASSETS_ROOT.length());

        if (resourceExists(themedPath)) {
            return themedPath;
        }

        return defaultPath;
    }

    private static boolean isThemeableImagePath(String path) {
        return path != null
                && path.startsWith(ASSETS_ROOT + "/")
                && !path.startsWith(ASSETS_ROOT + "/css/")
                && !path.startsWith(ASSETS_ROOT + "/soundTrack/");
    }

    private static boolean isThemeableAssetPath(String path) {
        return path != null
                && path.startsWith(ASSETS_ROOT + "/")
                && !path.startsWith(ASSETS_ROOT + "/soundTrack/");
    }

    private static boolean resourceExists(String path) {
        if (ThemeManager.class.getResource(path) != null) {
            return true;
        }

        return new File("src" + path).exists();
    }
}
