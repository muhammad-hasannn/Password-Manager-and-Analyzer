package gui;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import vaulthandler.VaultManager;
import vaulthandler.FileOperations;
import security.CryptoHandler;
import model.VaultData;

import java.util.ArrayList;

public class VaultView {

    private String username;

    // ✅ Your existing classes - NO CHANGES!
    private VaultManager vaultManager;
    private FileOperations fileOps = new FileOperations();

    private TableView<PasswordEntry> table;
    private ObservableList<PasswordEntry> passwordList = FXCollections.observableArrayList();


    public VaultView(String username) {
        this.username = username;

        // ✅ Load user's passwords using YOUR existing method!
        ArrayList<VaultData> data = fileOps.getUserData(username);
        vaultManager = new VaultManager(data);
    }


    public void show(Stage stage) {
        // Main container
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: #f5f5f5;");


        // TOP: Title
        HBox topBar = new HBox(20);
        topBar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10));

        Label titleLabel = new Label("🔐 Password Vault");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label("User: " + username);
        userLabel.setStyle("-fx-font-size: 14px;");

        topBar.getChildren().addAll(titleLabel, spacer, userLabel);
        root.setTop(topBar);


        // CENTER: Table
        table = new TableView<>();
        table.setItems(passwordList);

        TableColumn<PasswordEntry, String> platformCol = new TableColumn<>("Platform");
        platformCol.setCellValueFactory(new PropertyValueFactory<>("platform"));
        platformCol.setPrefWidth(200);

        TableColumn<PasswordEntry, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setPrefWidth(250);

        TableColumn<PasswordEntry, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("maskedPassword"));
        passwordCol.setPrefWidth(300);

        table.getColumns().addAll(platformCol, usernameCol, passwordCol);

        VBox centerBox = new VBox(table);
        centerBox.setPadding(new Insets(10, 0, 10, 0));
        root.setCenter(centerBox);


        // BOTTOM: Buttons
        HBox buttonBar = new HBox(15);
        buttonBar.setAlignment(javafx.geometry.Pos.CENTER);
        buttonBar.setPadding(new Insets(10));

        Button addButton = createButton("➕ Add", "#4CAF50");
        Button viewButton = createButton("👁️ View", "#2196F3");
        Button deleteButton = createButton("🗑️ Delete", "#f44336");
        Button refreshButton = createButton("🔄 Refresh", "#9E9E9E");

        Region buttonSpacer = new Region();
        HBox.setHgrow(buttonSpacer, Priority.ALWAYS);

        Button backButton = createButton("⬅️ Back", "#607D8B");

        buttonBar.getChildren().addAll(
                addButton, viewButton, deleteButton, refreshButton,
                buttonSpacer, backButton
        );
        root.setBottom(buttonBar);


        // Load initial data
        loadPasswords();


        // Button actions
        addButton.setOnAction(e -> addPassword());
        viewButton.setOnAction(e -> viewPassword());
        deleteButton.setOnAction(e -> deletePassword());
        refreshButton.setOnAction(e -> loadPasswords());
        backButton.setOnAction(e -> {
            // ✅ Save before going back!
            fileOps.updateVault(username, vaultManager.getAllPasswords());

            DashboardView dashboard = new DashboardView(username);
            dashboard.show(stage);
        });


        // Scene
        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
    }


    private void loadPasswords() {
        passwordList.clear();

        // ✅ Call YOUR existing method!
        ArrayList<VaultData> passwords = vaultManager.getAllPasswords();

        for (VaultData vd : passwords) {
            String masked = maskPassword(vd.getPasswordEncrypted());
            passwordList.add(new PasswordEntry(
                    vd.getPlatform(),
                    vd.getUsername(),
                    vd.getPasswordEncrypted(),
                    masked
            ));
        }
    }


    private String maskPassword(String password) {
        if (password.length() <= 4) return "****";
        return password.substring(0, 2) + "****" + password.substring(password.length() - 2);
    }


    private void addPassword() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Password");
        dialog.setHeaderText("Add New Password");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField platformField = new TextField();
        platformField.setPromptText("e.g., Netflix");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        grid.add(new Label("Platform:"), 0, 0);
        grid.add(platformField, 1, 0);
        grid.add(new Label("Username:"), 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(new Label("Password:"), 0, 2);
        grid.add(passwordField, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String platform = platformField.getText().trim();
                String user = usernameField.getText().trim();
                String pass = passwordField.getText();

                if (platform.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                    showAlert("Error", "All fields are required!");
                    return;
                }

                // ✅ Call YOUR existing method to encrypt!
                String encrypted = CryptoHandler.encrypt(pass);
                VaultData newEntry = new VaultData(user, platform, encrypted);

                // ✅ Call YOUR existing method!
                if (vaultManager.addNewPassword(newEntry)) {
                    showAlert("Success", "Password added!");
                    loadPasswords();
                } else {
                    showAlert("Error", "Password already exists!");
                }
            }
        });
    }


    private void viewPassword() {
        PasswordEntry selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Error", "Please select a password!");
            return;
        }

        // ✅ Call YOUR existing method to decrypt!
        String decrypted = CryptoHandler.decrypt(selected.getEncryptedPassword());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Password Details");
        alert.setHeaderText("Password for " + selected.getPlatform());
        alert.setContentText(
                "Platform: " + selected.getPlatform() + "\n" +
                        "Username: " + selected.getUsername() + "\n" +
                        "Password: " + decrypted
        );
        alert.showAndWait();
    }


    private void deletePassword() {
        PasswordEntry selected = table.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert("Error", "Please select a password!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete password for " + selected.getPlatform() + "?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                VaultData toDelete = new VaultData(
                        selected.getUsername(),
                        selected.getPlatform(),
                        selected.getEncryptedPassword()
                );

                // ✅ Call YOUR existing method!
                if (vaultManager.deletePassword(toDelete)) {
                    showAlert("Success", "Password deleted!");
                    loadPasswords();
                } else {
                    showAlert("Error", "Failed to delete!");
                }
            }
        });
    }


    private Button createButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefSize(110, 35);
        button.setStyle(
                "-fx-font-size: 13px; " +
                        "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 5;"
        );
        return button;
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    // Inner class for TableView
    public static class PasswordEntry {
        private String platform;
        private String username;
        private String encryptedPassword;
        private String maskedPassword;

        public PasswordEntry(String platform, String username, String encryptedPassword, String maskedPassword) {
            this.platform = platform;
            this.username = username;
            this.encryptedPassword = encryptedPassword;
            this.maskedPassword = maskedPassword;
        }

        public String getPlatform() { return platform; }
        public String getUsername() { return username; }
        public String getEncryptedPassword() { return encryptedPassword; }
        public String getMaskedPassword() { return maskedPassword; }
    }
}
