package vaulthandler;

import model.VaultData;
import java.util.ArrayList;
import security.CryptoHandler;

public class VaultManager {
    private ArrayList<VaultData> data;

    public VaultManager(ArrayList<VaultData> data) {
        this.data = data;
    }

    // this method will check if the passed platform name and it's password is unique or not
    public boolean matchesPlatformAndUsername(String platform, String username){
        for(VaultData v : data){
            if(v.getPlatform().equals(platform)){
                if(v.getUsername().equals(username)) return true;
            }
        }
        return false;
    }

    // 1. method to add new password (vault data)
    public boolean addNewPassword(VaultData v){

        // if it is true, means the same platform with the same password already exists
        // (in this case we will ask to update)
        if(matchesPlatformAndUsername(v.getPlatform(), v.getUsername())) return false;

        data.add(v);
        return true;
    }

    // 2. delete password
    public boolean deletePassword(VaultData v){

        // checking that the password which is to be deleted actually exists
        if(!matchesPlatformAndUsername(v.getPlatform(), v.getUsername())) return false;

        data.remove(v);
        return true;
    }

    // 3. update password
    public boolean updatePassword(VaultData oldData, VaultData newData){

        // checking that the password which is to be updated actually exists
        if(!matchesPlatformAndUsername(oldData.getPlatform(), oldData.getUsername())) return false;

        // deleting the old data
        data.remove(oldData);

        // adding the new data
        data.add(newData);

        return true;
    }

    // 4. get all passwords (because it is required for gui)
    public ArrayList<VaultData> getAllPasswords() {

        // Check if empty
        if (data.isEmpty()) {
            return null;
        }

        return data;
    }

    public void viewAllPasswords(){
        // Check if empty
        if (data.isEmpty()) {
            System.out.println("\nNo passwords saved yet!");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║            YOUR SAVED PASSWORDS                ║");
        System.out.println("╚════════════════════════════════════════════════╝");
        System.out.println("\nTotal passwords: " + data.size());
        System.out.println("─".repeat(50));

        int count = 1;
        for (VaultData v : data) {
            System.out.println("\nEntry #" + count);
            System.out.println("   Platform: " + v.getPlatform());
            System.out.println("   Username: " + v.getUsername());

            String plainPassword = CryptoHandler.decrypt(v.getPasswordEncrypted());
            System.out.println("   Password: " + plainPassword);
            System.out.println("─".repeat(50));
            count++;
        }
    }

}
