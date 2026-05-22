package game.view;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public final class ThemeManager {
    public enum Theme {
        DEFAULT("", "Theme: Monstropolis"),
        RETRO("/retro", "Theme: Vice City"),
        ANCIENT_EGYPT("/ancientEgypt", "Theme: Giza");

        private final String assetPrefix;
        private final String label;

        Theme(String assetPrefix, String label) {
            this.assetPrefix = assetPrefix;
            this.label = label;
        }
    }

    private static final String ASSETS_ROOT = "/game/assets";
    private static final String EGYPTIAN_FONT_PATH = "/game/assets/ancientEgypt/fonts/Almendra/Almendra-Regular.ttf";
    private static final String RETRO_FONT_PATH = "/game/assets/retro/fonts/PressStart2P-Regular.ttf";
    private static final String DEFAULT_FONT_FAMILY = "\"Arial Rounded MT Bold\", Arial, Helvetica, sans-serif";
    private static String egyptianFontFamily;
    private static String retroFontFamily;
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
        if (isAncientEgyptian()) {
            loadEgyptianFont();
        } else if (isRetro()) {
            loadRetroFont();
        }
        ImageCache.clear();
    }

    public static String getThemeFontFamily() {
        if (isRetro()) {
            String family = loadRetroFont();
            if (family != null && !family.isBlank()) {
                return "\"" + family + "\", " + DEFAULT_FONT_FAMILY;
            }
        }

        if (isAncientEgyptian()) {
            String family = loadEgyptianFont();
            if (family != null && !family.isBlank()) {
                return "\"" + family + "\", " + DEFAULT_FONT_FAMILY;
            }
        }

        return DEFAULT_FONT_FAMILY;
    }

    public static String getThemeFontName() {
        if (isRetro()) {
            String family = loadRetroFont();
            if (family != null && !family.isBlank()) {
                return family;
            }
        }

        if (isAncientEgyptian()) {
            String family = loadEgyptianFont();
            if (family != null && !family.isBlank()) {
                return family;
            }
        }

        return "Arial Rounded MT Bold";
    }

    public static String getThemeFontInlineStyle() {
        return " -fx-font-family: " + getThemeFontFamily() + ";";
    }

    private static String loadEgyptianFont() {
        if (egyptianFontFamily != null) {
            return egyptianFontFamily;
        }

        InputStream fontStream = ThemeManager.class.getResourceAsStream(EGYPTIAN_FONT_PATH);
        if (fontStream != null) {
            Font font = Font.loadFont(fontStream, 12);
            if (font != null) {
                egyptianFontFamily = font.getFamily();
                return egyptianFontFamily;
            }
        }

        File sourceFont = new File("src" + EGYPTIAN_FONT_PATH);
        if (sourceFont.exists()) {
            Font font = Font.loadFont(sourceFont.toURI().toString(), 12);
            if (font != null) {
                egyptianFontFamily = font.getFamily();
                return egyptianFontFamily;
            }
        }

        egyptianFontFamily = "";
        return egyptianFontFamily;
    }

    private static String loadRetroFont() {
        if (retroFontFamily != null) {
            return retroFontFamily;
        }

        InputStream fontStream = ThemeManager.class.getResourceAsStream(RETRO_FONT_PATH);
        if (fontStream != null) {
            Font font = Font.loadFont(fontStream, 12);
            if (font != null) {
                retroFontFamily = font.getFamily();
                return retroFontFamily;
            }
        }

        File sourceFont = new File("src" + RETRO_FONT_PATH);
        if (sourceFont.exists()) {
            Font font = Font.loadFont(sourceFont.toURI().toString(), 12);
            if (font != null) {
                retroFontFamily = font.getFamily();
                return retroFontFamily;
            }
        }

        retroFontFamily = "";
        return retroFontFamily;
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
                + " -fx-background-radius: 10; -fx-padding: 2 5 2 4;" + getRetroNeonInlineEffect()
                + getThemeFontInlineStyle();
    }

    public static String getRetroNeonInlineEffect() {
        if (!isRetro()) {
            return "";
        }

        return " -fx-effect: dropshadow(three-pass-box, rgba(0, 229, 255, 0.95), 8, 0, 0, 1);";
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

    public static String getFullscreenButtonStyle() {
        if (isRetro()) {
            return """
                    -fx-background-color:
                        linear-gradient(#ff8df0 0%, #ff3fd0 46%, #9b2cff 47%, #1824b8 100%),
                        linear-gradient(#160826 0%, #050714 100%),
                        linear-gradient(#00e5ff, #ff4fd8);
                    -fx-background-insets: 0, 1, 2;
                    -fx-background-radius: 10, 9, 8;
                    -fx-text-fill: white;
                    -fx-font-size: 13px;
                    -fx-font-weight: bold;
                    -fx-border-color: #00e5ff;
                    -fx-border-radius: 10;
                    -fx-effect: dropshadow(three-pass-box, rgba(255, 79, 216, 0.82), 14, 0, 0, 1);
                    """ + getThemeFontInlineStyle();
        }

        if (isAncientEgyptian()) {
            return """
                    -fx-background-color:
                        linear-gradient(#fff2a8 0%, #d59a24 48%, #7a4c08 49%, #a8690d 100%),
                        linear-gradient(#3b2205, #1c1003);
                    -fx-background-insets: 0, 2;
                    -fx-background-radius: 10, 8;
                    -fx-text-fill: white;
                    -fx-font-size: 17px;
                    -fx-font-weight: bold;
                    -fx-border-color: #f6ca58;
                    -fx-border-radius: 10;
                    -fx-effect: dropshadow(three-pass-box, rgba(246, 202, 88, 0.75), 12, 0, 0, 1);
                    """ + getThemeFontInlineStyle();
        }

        return """
                -fx-background-color:
                    linear-gradient(#70b1ff 0%, #1a5cad 50%, #0a3b75 51%, #114b91 100%),
                    linear-gradient(#202020 0%, #111111 100%),
                    linear-gradient(#3e5e8e, #2e4a77);
                -fx-background-insets: 0, 1, 2;
                -fx-background-radius: 10, 9, 8;
                -fx-text-fill: white;
                -fx-font-size: 17px;
                -fx-font-weight: bold;
                -fx-border-color: #78beff;
                -fx-border-radius: 10;
                -fx-effect: dropshadow(three-pass-box, rgba(120, 190, 255, 0.65), 10, 0, 0, 1);
                """ + getThemeFontInlineStyle();
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

    public static URL resolveThemeAwareUrl(String defaultPath) {
        String resolvedPath = resolveThemeAwarePath(defaultPath);
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

        File caseInsensitiveSourceAsset = findCaseInsensitiveSourceAsset(resolvedPath);
        if (caseInsensitiveSourceAsset != null) {
            try {
                return caseInsensitiveSourceAsset.toURI().toURL();
            } catch (Exception e) {
                return ThemeManager.class.getResource(defaultPath);
            }
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

    private static String resolveThemeAwarePath(String defaultPath) {
        if (currentTheme == Theme.DEFAULT || defaultPath == null || !defaultPath.startsWith(ASSETS_ROOT + "/")) {
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

        return new File("src" + path).exists() || findCaseInsensitiveSourceAsset(path) != null;
    }

    private static File findCaseInsensitiveSourceAsset(String resourcePath) {
        File exactFile = new File("src" + resourcePath);
        File parent = exactFile.getParentFile();

        if (parent == null || !parent.isDirectory()) {
            return null;
        }

        File[] matches = parent.listFiles(file -> file.getName().equalsIgnoreCase(exactFile.getName()));
        if (matches == null || matches.length == 0) {
            return null;
        }

        return matches[0];
    }
}
