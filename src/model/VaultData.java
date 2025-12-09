package model;

public class VaultData {
    private String platform;
    private String username;
    private String passwordEncrypted;

    public VaultData(String passwordEncrypted, String platform, String username) {
        this.passwordEncrypted = passwordEncrypted;
        this.platform = platform;
        this.username = username;
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
