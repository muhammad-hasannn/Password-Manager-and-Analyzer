package model;

import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VaultData vaultData = (VaultData) o;
        return Objects.equals(platform, vaultData.platform) && Objects.equals(username, vaultData.username) && Objects.equals(passwordEncrypted, vaultData.passwordEncrypted);
    }


}
