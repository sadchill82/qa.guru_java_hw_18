package helpers;

import java.util.Random;

public class ConfigManager {
    private static final String SELENOID_URL = System.getProperty("selenoidUrl");
    private static final String CREDENTIALS_USR = System.getProperty("username");
    private static final String CREDENTIALS_PSW = System.getProperty("password");
    private static final String BROWSER = System.getProperty("browser", "chrome");
    private static final String BROWSER_VERSION = System.getProperty("browserVersion", "");
    private static final String SCREEN_RESOLUTION = System.getProperty("browserSize", "1920x1080");
    private static final boolean ENABLE_VNC = Boolean.parseBoolean(System.getProperty("enableVNC", "true"));
    private static final boolean ENABLE_VIDEO = Boolean.parseBoolean(System.getProperty("enableVideo", "true"));

    public static final String TEST_USERNAME = System.getProperty("test.username", "TOOLSQA-Test");
    public static final String TEST_PASSWORD = System.getProperty("test.password", "Test@@123");

    public static final String[] TEST_BOOK_ISBNS = {
            "9781449325862", // Git Pocket Guide
            "9781449331818", // Learning JavaScript Design Patterns
            "9781449337711", // Designing Evolvable Web APIs with ASP.NET
            "9781449365035", // Speaking JavaScript
            "9781491904244", // You Don't Know JS
            "9781491950296", // Programming JavaScript Applications
            "9781593275846", // Eloquent JavaScript, Second Edition
            "9781593277574"  // Understanding ECMAScript 6
    };

    public static String getSelenoidUrl() {
        if (SELENOID_URL != null && CREDENTIALS_USR != null && CREDENTIALS_PSW != null) {
            return "https://" + CREDENTIALS_USR + ":" + CREDENTIALS_PSW + "@" + SELENOID_URL + "/wd/hub";
        }
        return null;
    }

    public static String getBrowser() {
        return BROWSER;
    }

    public static String getBrowserVersion() {
        return BROWSER_VERSION;
    }

    public static String getScreenResolution() {
        return SCREEN_RESOLUTION;
    }

    public static boolean isEnableVNC() {
        return ENABLE_VNC;
    }

    public static boolean isEnableVideo() {
        return ENABLE_VIDEO;
    }

    public static String getRandomBookIsbn() {
        Random random = new Random();
        return TEST_BOOK_ISBNS[random.nextInt(TEST_BOOK_ISBNS.length)];
    }

    public static String getBookIsbn(int index) {
        if (index >= 0 && index < TEST_BOOK_ISBNS.length) {
            return TEST_BOOK_ISBNS[index];
        }
        return TEST_BOOK_ISBNS[0];
    }

    public static String getDefaultTestBookIsbn() {
        return TEST_BOOK_ISBNS[1];
    }
}