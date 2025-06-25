package config;

public class TestConfig {
    public static final String BASE_URL = System.getProperty("demoqa.base.url", "https://demoqa.com");

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
}