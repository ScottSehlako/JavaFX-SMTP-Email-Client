package com.example.mailer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The main entry point for the JavaFX Mailer application.
 * <p>
 * This class builds the graphical user interface, handles user input validation, 
 * and manages the background threads required to send emails without freezing the UI.
 * </p>
 * 
 * @author Your Name
 * @version 1.0
 */
public class MainApp extends Application {

    /**
     * Initializes and displays the primary application window.
     *
     * @param primaryStage The primary stage provided by the JavaFX runtime.
     */
    @Override
    public void start(Stage primaryStage) {

        // 1. Header Section
        Label title = new Label("SMTP Mailer");
        title.getStyleClass().add("title-label");
        title.setMaxWidth(Double.MAX_VALUE);

        // 2. Form Input Fields
        TextField hostField = new TextField();
        hostField.setPromptText("SMTP Host (e.g. smtp.gmail.com)");

        TextField portField = new TextField("465");
        portField.setPromptText("Port (465 for SSL)");

        TextField userField = new TextField();
        userField.setPromptText("Username / Email");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password / App Code");

        TextField toField = new TextField();
        toField.setPromptText("Recipient email");

        TextField subjectField = new TextField();
        subjectField.setPromptText("Subject");

        TextArea bodyArea = new TextArea();
        bodyArea.setPromptText("Write your message here...");
        bodyArea.setPrefRowCount(7);
        bodyArea.setWrapText(true);

        // 3. Form Layout
        VBox form = new VBox(6,
                new Label("SMTP Server"), hostField, portField,
                new Label("Credentials"), userField, passField,
                new Label("Recipient"), toField,
                new Label("Subject"), subjectField,
                new Label("Message"), bodyArea
        );
        form.setPadding(new Insets(18));
        form.getStyleClass().add("form-box");

        // 4. Footer (Button & Status)
        Button sendBtn = new Button("Send Email");
        sendBtn.getStyleClass().add("send-button");

        Label statusLabel = new Label("Ready");
        statusLabel.getStyleClass().add("status-label");

        // Action handler for the Send button
        sendBtn.setOnAction(e -> {
            String host = hostField.getText().trim();
            String portText = portField.getText().trim();
            String user = userField.getText().trim();
            String pass = passField.getText();
            String to   = toField.getText().trim();

            // Validation: Check for empty fields
            if (host.isEmpty() || user.isEmpty() || pass.isEmpty() || to.isEmpty()) {
                statusLabel.setText("❌ Fill host, credentials, and recipient first.");
                return;
            }

            // Validation: Check port format
            int port;
            try {
                port = Integer.parseInt(portText);
            } catch (NumberFormatException ex) {
                statusLabel.setText("❌ Port must be a number.");
                return;
            }

            statusLabel.setText("⏳ Sending...");
            sendBtn.setDisable(true);

            // Run email sending on a background thread to keep the UI responsive
            new Thread(() -> {
                try {
                    EmailSender.sendEmail(
                            host, port,
                            user, pass,
                            user, to,
                            subjectField.getText(),
                            bodyArea.getText(),
                            port == 465  // Use SSL by default if port is 465
                    );
                    // Update UI on the JavaFX Application Thread upon success
                    Platform.runLater(() -> {
                        statusLabel.setText("✅ Sent successfully.");
                        sendBtn.setDisable(false);
                    });
                } catch (Exception ex) {
                    // Update UI on the JavaFX Application Thread upon failure
                    Platform.runLater(() -> {
                        statusLabel.setText("❌ " + ex.getMessage());
                        sendBtn.setDisable(false);
                    });
                }
            }).start();
        });

        HBox bottom = new HBox(15, sendBtn, statusLabel);
        bottom.setAlignment(Pos.CENTER_LEFT);
        bottom.setPadding(new Insets(12, 18, 18, 18));

        // 5. Root Layout
        BorderPane root = new BorderPane();
        root.setTop(title);
        root.setCenter(form);
        root.setBottom(bottom);

        Scene scene = new Scene(root, 540, 720);

        // Safely load the CSS file
        var css = MainApp.class.getResource("/style.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        } else {
            System.err.println("WARNING: style.css not found on classpath.");
        }

        primaryStage.setTitle("SMTP Mailer");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(480);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    /**
     * The main method, required to launch the JavaFX application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        launch(args);
    }
}