package hr.algebra.xml;

public class AppConfig {
    private final String dbUrl;
    private final String dbUsername;
    private final String dbPassword;
    private final int windowWidth;
    private final int windowHeight;
    private final String windowTitle;

    public AppConfig(String dbUrl, String dbUsername, String dbPassword, int windowWidth, int windowHeight, String windowTitle) {
        this.dbUrl = dbUrl;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        this.windowTitle = windowTitle;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public String getWindowTitle() {
        return windowTitle;
    }
}
