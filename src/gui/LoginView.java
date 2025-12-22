package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import logindao.UserDAO;
import vaulthandler.FileOperations;
import security.CryptoHandler;
import model.User;

public class LoginView {

    // ✅ Your existing classes - NO CHANGES!
    private UserDAO userDAO = new UserDAO();
    private FileOperations fileOps = new FileOperations();


    public void show(Stage stage) {
        // Main container
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label titleLabel = new Label("PASSWORD MANAGER");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Username
        VBox usernameBox = new VBox(5);
        Label usernameLabel = new Label("Username:");
        usernameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setPrefWidth(300);
        usernameField.setStyle("-fx-font-size: 13px; -fx-padding: 8;");
        usernameBox.getChildren().addAll(usernameLabel, usernameField);

        // Password
        VBox passwordBox = new VBox(5);
        Label passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setPrefWidth(300);
        passwordField.setStyle("-fx-font-size: 13px; -fx-padding: 8;");
        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        // Status label
        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 12px;");

        // Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button loginButton = new Button("Login");
        loginButton.setPrefSize(120, 40);
        loginButton.setStyle("-fx-font-size: 14px; -fx-background-color: #4CAF50; " +
                "-fx-text-fill: white; -fx-background-radius: 5;");

        Button signupButton = new Button("Sign Up");
        signupButton.setPrefSize(120, 40);
        signupButton.setStyle("-fx-font-size: 14px; -fx-background-color: #2196F3; " +
                "-fx-text-fill: white; -fx-background-radius: 5;");

        buttonBox.getChildren().addAll(loginButton, signupButton);

        // Add all to root
        root.getChildren().addAll(
                titleLabel,
                usernameBox,
                passwordBox,
                buttonBox,
                statusLabel
        );


        // Login button action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter username and password!");
                statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
                return;
            }

            // ✅ Call YOUR existing method!
            User user = userDAO.getUser(username);

            if (user == null) {
                statusLabel.setText("User not found!");
                statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
                return;
            }

            // ✅ Call YOUR existing method!
            String hashedPassword = CryptoHandler.encrypt(password);

            if (hashedPassword.equals(user.getPasswordHash())) {
                statusLabel.setText("Login successful!");
                statusLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");

                // Open dashboard
                DashboardView dashboard = new DashboardView(username);
                dashboard.show(stage);

            } else {
                statusLabel.setText("Incorrect password!");
                statusLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
            }
        });


        // Signup button action
        signupButton.setOnAction(e -> {
            // Create signup dialog
            Dialog<String[]> dialog = new Dialog<>();
            dialog.setTitle("Sign Up");
            dialog.setHeaderText("Create New Account");

            // Fields
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20));

            TextField newUsername = new TextField();
            newUsername.setPromptText("Username");
            PasswordField newPassword = new PasswordField();
            newPassword.setPromptText("Password");
            PasswordField confirmPassword = new PasswordField();
            confirmPassword.setPromptText("Confirm Password");

            grid.add(new Label("Username:"), 0, 0);
            grid.add(newUsername, 1, 0);
            grid.add(new Label("Password:"), 0, 1);
            grid.add(newPassword, 1, 1);
            grid.add(new Label("Confirm:"), 0, 2);
            grid.add(confirmPassword, 1, 2);

            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.showAndWait().ifPresent(response -> {
                String newUser = newUsername.getText().trim();
                String pass1 = newPassword.getText();
                String pass2 = confirmPassword.getText();

                if (newUser.isEmpty() || pass1.isEmpty()) {
                    showAlert("Error", "All fields are required!");
                    return;
                }

                if (!pass1.equals(pass2)) {
                    showAlert("Error", "Passwords don't match!");
                    return;
                }

                // ✅ Call YOUR existing method!
                if (userDAO.searchUser(newUser)) {
                    showAlert("Error", "Username already exists!");
                    return;
                }

                // ✅ Call YOUR existing methods!
                String hashedPassword = CryptoHandler.encrypt(pass1);
                User user = new User(newUser, hashedPassword);

                if (userDAO.addUser(user)) {
                    fileOps.createNewUser(newUser);
                    showAlert("Success", "Account created! You can now login.");
                } else {
                    showAlert("Error", "Failed to create account!");
                }
            });
        });


        // Allow Enter key to login
        passwordField.setOnAction(e -> loginButton.fire());


        // Scene
        Scene scene = new Scene(root, 450, 500);
        stage.setScene(scene);
        stage.show();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}