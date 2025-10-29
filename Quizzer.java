package Quizzproject;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Quizzer extends Application {
    
    // Constants for styling
    private static final String BACKGROUND_COLOR = "-fx-background-color:rgb(255, 238, 222);";
    private static final Font TITLE_FONT = Font.font("Arial", FontWeight.BOLD, 24);
    private static final String QUESTIONS_FILE = "C:\\Users\\motte\\gcis123\\ProjetpourFX\\FX\\src\\Quizzproject\\questionsBase.txt";
    private static final String LEADERBOARD_FILE = "C:\\Users\\motte\\gcis123\\ProjetpourFX\\FX\\src\\Quizzproject\\leaderboard.txt";

    // Method for fading transition between scenes
    public static void fadeSceneTransition(Stage stage, Scene newScene) {
        Pane currentRoot = (Pane) stage.getScene().getRoot();
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), currentRoot);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            stage.setScene(newScene);
            Pane newRoot = (Pane) newScene.getRoot();
            newRoot.setOpacity(0.0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), newRoot);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
    });
    fadeOut.play();
}
    @Override
    public void start(Stage stage) {
        // Initialize game state
        ArrayList<Integer> scoreAndIndex = new ArrayList<>();
        scoreAndIndex.add(0); // Score
        scoreAndIndex.add(0); // Current question index
        
        final Timer[] timer = new Timer[1]; // Timer reference

        // ===== SCENE 1: Welcome Screen =====
        VBox welcomeScreen = new VBox(20);
        welcomeScreen.setPadding(new Insets(30));
        welcomeScreen.setStyle(BACKGROUND_COLOR);
        welcomeScreen.setAlignment(javafx.geometry.Pos.CENTER);
        

        // Title
        Label titleLabel = new Label("Java Quiz Challenge");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setTextFill(Color.web("#4a6fa5"));

        // Username Input
        TextField txtuserName = new TextField();
        txtuserName.setPromptText("Enter your name");
        txtuserName.setMaxWidth(300);

        // Rules (in BOLD as requested)
        Label rulesLabel = new Label(
            "1. Choose your difficulty level\n" +
            "2. Answer before time runs out\n" +
            "3. Type answers exactly as shown\n" +
            "4. Have fun learning Java!"
        );
        rulesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // Difficulty Buttons
        HBox difficultyBox = new HBox(15);
        Button easy = createStyledButton("Easy (5 min)", "#4CAF50");
        Button medium = createStyledButton("Medium (3 min)", "#FFC107");
        Button hard = createStyledButton("Hard (1 min)", "#F44336");
        difficultyBox.getChildren().addAll(easy, medium, hard);
        difficultyBox.setAlignment(javafx.geometry.Pos.CENTER);

        welcomeScreen.getChildren().addAll(titleLabel, txtuserName, rulesLabel, difficultyBox);
        Scene welcomeScene = new Scene(welcomeScreen, 800, 600);

        // ===== SCENE 2: Quiz Screen =====
        VBox quizScreen = new VBox(20);
        quizScreen.setPadding(new Insets(30));
        quizScreen.setStyle(BACKGROUND_COLOR);

        // Timer
        Label timerLabel = new Label("Time: ");
        timerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Question Display
        Label questionLabel = new Label();
        questionLabel.setFont(Font.font("Arial", 16));
        questionLabel.setWrapText(true);

        Label answersLabel = new Label();
        answersLabel.setFont(Font.font("Arial", 14));

        // Answer Input
        TextField answerField = new TextField();
        answerField.setPromptText("Type your answer here...");
        answerField.setMaxWidth(400);

        Button submitButton = createStyledButton("Submit Answer", "#4a6fa5");

        quizScreen.getChildren().addAll(timerLabel, questionLabel, answersLabel, answerField, submitButton);
        Scene quizScene = new Scene(quizScreen, 800, 600);

        // ===== SCENE 3: Results Screen =====
        VBox resultsScreen = new VBox(30);
        resultsScreen.setPadding(new Insets(40));
        resultsScreen.setStyle(BACKGROUND_COLOR);
        resultsScreen.setAlignment(javafx.geometry.Pos.CENTER);

        Label resultLabel = new Label();
        resultLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Button leaderboardButton = createStyledButton("View Leaderboard", "#4a6fa5");
        resultsScreen.getChildren().addAll(resultLabel, leaderboardButton);
        Scene resultsScene = new Scene(resultsScreen, 800, 600);

        // ===== SCENE 4: Leaderboard =====
        VBox leaderboardScreen = new VBox(30);
        leaderboardScreen.setPadding(new Insets(40));
        leaderboardScreen.setStyle(BACKGROUND_COLOR);
        leaderboardScreen.setAlignment(javafx.geometry.Pos.CENTER);

        Label leaderboardLabel = new Label("Leaderboard");
        leaderboardLabel.setFont(TITLE_FONT);

        Button closeButton = createStyledButton("Close", "#757575");
        Button clearButton = createStyledButton("Clear Leaderboard", "#f44336");
        HBox buttonBox = new HBox(20, closeButton, clearButton);

        leaderboardScreen.getChildren().addAll(leaderboardLabel, buttonBox);
        Scene leaderboardScene = new Scene(leaderboardScreen, 800, 600);

        // ===== Load Questions =====
        QuizQuestions quizQuestions = new QuizQuestions();
        quizQuestions.load(QUESTIONS_FILE);
        ArrayList<Question> selectedQuestions = quizQuestions.select(10);

        // Initialize first question
        if (!selectedQuestions.isEmpty()) {
            Question currentQuestion = selectedQuestions.get(0);
            questionLabel.setText(currentQuestion.getQuestion());
            answersLabel.setText(String.join(", ", currentQuestion.getAllAnswers()));
        }

        // ===== Button Handlers =====
        // Difficulty buttons
        EventHandler<ActionEvent> difficultyHandler = e -> {
            Button source = (Button)e.getSource();
            String difficulty = source.getText().contains("Easy") ? "easy" : 
                              source.getText().contains("Medium") ? "medium" : "hard";
            int[] time = difficulty.equals("easy") ? new int[]{300} : 
                         difficulty.equals("medium") ? new int[]{180} : new int[]{60};
            
            timer[0] = new Timer(timerLabel, time, stage, resultsScene);
            timer[0].start();
            fadeSceneTransition(stage, quizScene);
        };
        
        easy.setOnAction(difficultyHandler);
        medium.setOnAction(difficultyHandler);
        hard.setOnAction(difficultyHandler);

        // Submit button
        submitButton.setOnAction(_ -> {
            String answer = answerField.getText();
            Question currentQuestion = selectedQuestions.get(scoreAndIndex.get(1));
            
            if (answer.equals(currentQuestion.getCorrectAnswer())) {
                scoreAndIndex.set(0, scoreAndIndex.get(0) + 1);
            }
            
            scoreAndIndex.set(1, scoreAndIndex.get(1) + 1);
            
            if (scoreAndIndex.get(1) < selectedQuestions.size()) {
                currentQuestion = selectedQuestions.get(scoreAndIndex.get(1));
                questionLabel.setText(currentQuestion.getQuestion());
                answersLabel.setText(String.join(", ", currentQuestion.getAllAnswers()));
                answerField.clear();
            } else {
                resultLabel.setText("Your score: " + scoreAndIndex.get(0) + "/" + selectedQuestions.size());
                stage.setScene(resultsScene);
            }
        });

        // Leaderboard button
        leaderboardButton.setOnAction(e -> {
            String username = txtuserName.getText();
            int score = scoreAndIndex.get(0);
            int timeTaken = timer[0] != null ? timer[0].getTimeTaken() : 0;
            
            new Gotoleaderboard(leaderboardLabel, leaderboardScene, stage, username, score, timeTaken, LEADERBOARD_FILE).handle(e);
        });

        // Clear leaderboard button
        clearButton.setOnAction(e -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LEADERBOARD_FILE))) {
                writer.write("");
                leaderboardLabel.setText("Leaderboard cleared!");
            } catch (IOException ex) {
                System.out.println("Error clearing leaderboard: " + ex.getMessage());
            }
        });

        // Close button
        closeButton.setOnAction(e -> stage.close());

        // Start application
        stage.setScene(welcomeScene);
        stage.setTitle("MyQuizzes");
        stage.getIcons().add(new Image("file:C:\\Users\\motte\\gcis123\\ProjetpourFX\\FX\\src\\Quizzproject\\LOGO_MYQUIZZES.png")); // Add your icon path here
        stage.show();

    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 8px 16px;" +
            "-fx-background-radius: 5px;" +
            "-fx-font-size: 14px;"
        );
        button.setOnMouseEntered(_ -> button.setStyle(
            "-fx-background-color: derive(" + color + ", -20%);" +
            "-fx-cursor: hand;" +
            button.getStyle()
        ));
        button.setOnMouseExited(_ -> button.setStyle(
            "-fx-background-color: " + color + ";" +
            button.getStyle().replace("-fx-cursor: hand;", "")
        ));
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}