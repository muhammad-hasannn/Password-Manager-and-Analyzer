package model;

public class VaultData {
    private String platform;
    private String username;
    private String passwordEncrypted;

    public VaultData(String username, String platform, String passwordEncrypted) {
        this.username = username;
        this.platform = platform;
        this.passwordEncrypted = passwordEncrypted;
    }

    public String getPasswordEncrypted() {
        return passwordEncrypted;
    }
    public String getPlatform() {
        return platform;
    }
    public String getUsername() {
        return username;
    }

    // no need for setters, as update feature is not available yet
}
