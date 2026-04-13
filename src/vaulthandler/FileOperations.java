package vaulthandler;

import model.VaultData;
import java.io.*;
import java.util.ArrayList;

public class FileOperations {
    private static final String FILE_PATH = "database/passwords.txt";

    /**
     * 1. create new user:
     * whenever a new user will sign up, we will append "[username]" in the file
     */
    public void createNewUser(String username){

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))){
            bw.write("[" + username + "]\n");
        } catch (IOException e) {
            System.out.println("Error in creating user: " + e.getMessage());
        }
    }

    /* 2. get user data
       when a user will log in, we'll get all of his data in an array
     */
    public ArrayList<VaultData> getUserData(String username){
        ArrayList<VaultData> data = new ArrayList<>();
        String userMarker = "[" + username + "]";

        try(BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))){
            String line;

            while((line = br.readLine()) != null){
                if (line.isEmpty()) continue;

                // we hit the block of required user!
                if(line.equals(userMarker)){

                   while((line = br.readLine()) != null){
                       if (line.isEmpty()) continue;

                       if(line.charAt(0) == '[') break; // the starting of another user

                       String[] parts = line.split("\\|");

                       if(parts.length == 3) { // verifying that the line is in perfect format
                           String platform = parts[0];
                           String user = parts[1];
                           String passwordHash = parts[2];

                           data.add(new VaultData(platform, user, passwordHash));
                       }
                   }
                   break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error in reading file: " + e.getMessage());
        }
        return data;
    }

    /**
     * 3. update vault
     * when user will sign out, and if any change will be happened in the Vault[]
     * we will update the file with updated array
     */
    public boolean updateVault(String username, ArrayList<VaultData> updatedData){
        String userMarker = "[" + username + "]";

        // objects pointing on files(the original one and temp one), we created them because after we will rename temp_vault and also delete original file
        File originalFile = new File(FILE_PATH);
        File tempFile = new File("tempVaultFile.txt");

        try(BufferedReader br = new BufferedReader(new FileReader(originalFile));
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile))) {

            String line;

            while((line = br.readLine()) != null){
                if (line.isEmpty()) continue;

                // we hit the block of required user!
                if(line.equals(userMarker)){

                    // writing the user marker
                    bw.write(userMarker);
                    bw.newLine();

                    // now writing the updated vault data
                    for(VaultData v : updatedData){
                        bw.write(v.getPlatform() + "|" + v.getUsername() + "|" + v.getPasswordEncrypted());
                        bw.newLine();
                    }

                    // skipping old data lines (moving br)
                    while(((line = br.readLine()) != null) && !line.startsWith("[")){
                        // just skipping those lines
                    }
                    /**
                     * <pre>
                     * as the above loop has moved the br pointer have moved to next user's marker,
                     * so we should write that in temp file here, because there is continue statement at the end
                     *
                     * After the skipping loop, line could be:
                     *       null → reached the end of file, nothing more to write.
                     *       [NextUser] → the first line of the next user’s block.
                     *
                     * we can also remove continue and get done this by main loop (bw.write(line); bw.newLine();),
                     * but... what if the pointer reaches the end...
                     * </pre>
                     */

                    if(line != null){ // means it is a user marker, writing that...
                        bw.write(line);
                        bw.newLine();
                    }
                    continue;

                } // 'if we hit the required user' block ends

                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e){
            System.out.println("Exception: Error while opening file to write or read: " + e.getMessage());
            return false;
        }

        // NOTE: operations like deleting original file and renaming temp file should be performed after closing buffer reader and writer

        if (!originalFile.delete()) {
            System.out.println("Could not delete original vault file.");
            return false;
        }
        if (!tempFile.renameTo(originalFile)) {
            System.out.println("Could not rename temp vault file.");
            return false;
        }
        return true;
    }

}
