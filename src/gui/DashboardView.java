package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DashboardView {

    private String username;

    public DashboardView(String username) {
        this.username = username;
    }


    public void show(Stage stage) {
        // Main container
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Welcome message
        Label welcomeLabel = new Label("Welcome, " + username + "!");
        welcomeLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Password Vault Button
        Button vaultButton = createButton(" My Password Vault", "#4CAF50");
        vaultButton.setOnAction(e -> {
            VaultView vaultView = new VaultView(username);
            vaultView.show(stage);
        });

        // Strength Analyzer Button
        Button analyzerButton = createButton("Password Strength Analyzer", "#2196F3");
        analyzerButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Strength Analyzer");
            dialog.setHeaderText("Analyze Password Strength");
            dialog.setContentText("Enter password:");

            dialog.showAndWait().ifPresent(password -> {
                if (!password.isEmpty()) {
                    // You can integrate your PasswordAnalyzer here!
                    showAlert("Analysis", "Password Strength Analyzer\n\nFeature coming soon!");
                }
            });
        });

        // Logout Button
        Button logoutButton = createButton("🚪 Logout", "#f44336");
        logoutButton.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Logout");
            confirm.setHeaderText("Are you sure you want to logout?");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    LoginView loginView = new LoginView();
                    loginView.show(stage);
                }
            });
        });

        // Add all
        root.getChildren().addAll(
                welcomeLabel,
                vaultButton,
                analyzerButton,
                logoutButton
        );

        // Scene
        Scene scene = new Scene(root, 550, 450);
        stage.setScene(scene);
    }


    private Button createButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefSize(350, 70);
        button.setStyle(
                "-fx-font-size: 18px; " +
                        "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        );

        // Hover effect
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-font-size: 18px; " +
                        "-fx-background-color: derive(" + color + ", -10%); " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-font-size: 18px; " +
                        "-fx-background-color: " + color + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        ));

        return button;
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}